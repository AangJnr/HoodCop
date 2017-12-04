package com.piemicrosystems.hoodcop.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amirarcane.recentimages.RecentImages;
import com.amirarcane.recentimages.thumbnailOptions.ImageAdapter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayGridView;
import com.piemicrosystems.hoodcop.Callbacks;
import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.Util;
import com.piemicrosystems.hoodcop.object.Address;
import com.piemicrosystems.hoodcop.object.FeedItem;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aangjnr on 13/11/2017.
 */

public class StatusUpdateActivity extends BaseActivity {


    static Callbacks.UpdateFeedAdapterListener updateFeedAdapterListener;
    RelativeLayout imageLayout;
    ImageView selectedImage;
    ImageView removeImage;
    Button postButton;
    EditText statusText;
    ProgressBar progressBar;
    Bitmap postImageBitmap = null;
    Boolean withImage = false;
    private Uri imageUri;
    private ContentResolver contentResolver;
    private File photoFile = null;
    private RecentImages recentImages;

    public static void OnFeedItemUpdatedistener(Callbacks.UpdateFeedAdapterListener listener) {

        updateFeedAdapterListener = listener;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_update);


        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        postButton = (Button) findViewById(R.id.postButton);
        postButton.setEnabled(false);


        Log.d("STATUS UPDATE", "NAME IS " + me.getName());

        Log.d("STATUS UPDATE", "NAME IS " + me.getImageUrl());


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                postButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);


                Toast.makeText(StatusUpdateActivity.this, "Posting... ", Toast.LENGTH_SHORT).show();


                if (!withImage)
                    postFeedWithoutImage();


                else postFeedWithImage();


            }
        });


        findViewById(R.id.showimageGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.checkPermission(
                        StatusUpdateActivity.this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        },
                        new Util.OnPermissionCallback() {
                            @Override
                            public void onPermissionGranted() {

                                showBottomDialog();
                            }


                            @Override
                            public void onPermissionDenied() {


                            }
                        }
                );

            }
        });


        findViewById(R.id.ll2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Util.checkPermission(
                        StatusUpdateActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        new Util.OnPermissionCallback() {
                            @Override
                            public void onPermissionGranted() {

                                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                                if (!(manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {

                                    showAlertDialog(false, "GPS is disabled", "Would you like to enable it?", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 600);


                                        }
                                    }, "OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    }, "CANCEL", 0);


                                } else {
                                    showPlacesDialog();
                                }
                            }


                            @Override
                            public void onPermissionDenied() {


                            }
                        }
                );

            }
        });


        imageLayout = (RelativeLayout) findViewById(R.id.imageRl);
        selectedImage = (ImageView) findViewById(R.id.selectedImage);

        removeImage = (ImageView) findViewById(R.id.removeImage);
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageLayout.setVisibility(View.GONE);
                selectedImage.setImageBitmap(null);
                imageUri = null;

            }
        });


        statusText = (EditText) findViewById(R.id.status_edittext);
        statusText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {

                    if (!postButton.isEnabled()) postButton.setEnabled(true);

                } else if (s.length() <= 0)
                    if (postButton.isEnabled()) postButton.setEnabled(false);


            }
        });


    }


    void showBottomDialog() {

        final View bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        final TwoWayGridView twoWayGridView = (TwoWayGridView) bottomSheet.findViewById(R.id.gridview);

        final Dialog mBottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);

        mBottomSheetDialog.setContentView(bottomSheet);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        contentResolver = this.getContentResolver();
        recentImages = new RecentImages();
        final ImageAdapter adapter = recentImages.getAdapter(StatusUpdateActivity.this);


        mBottomSheetDialog.show();

        twoWayGridView.setAdapter(adapter);
        twoWayGridView.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {
            public void onItemClick(TwoWayAdapterView parent, View v, int position, long id) {
                imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                try {
                    int orientation = getOrientation(contentResolver, (int) id);
                    postImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
                    //d = getRotateDrawable(bitmap, orientation);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                withImage = true;

                imageLayout.animate().alpha(1f).setDuration(500).start();
                imageLayout.setVisibility(View.VISIBLE);
                Picasso.with(StatusUpdateActivity.this).load(imageUri).into(selectedImage);


                mBottomSheetDialog.dismiss();
            }
        });


        bottomSheet.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takeImage();
                mBottomSheetDialog.dismiss();


            }
        });


        bottomSheet.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("imageView/*");
                startActivityForResult(photoPickerIntent, Constants.SELECT_PHOTO_REQUEST);
                mBottomSheetDialog.dismiss();


            }
        });

    }


    @Override
    protected void onDestroy() {

        if (recentImages != null)
            recentImages.cleanupCache();
        super.onDestroy();
    }


    private Drawable getRotateDrawable(final Bitmap b, final float angle) {
        final BitmapDrawable drawable = new BitmapDrawable(getResources(), b) {
            @Override
            public void draw(final Canvas canvas) {
                canvas.save();
                canvas.rotate(angle, b.getWidth() / 2, b.getHeight() / 2);
                super.draw(canvas);
                canvas.restore();
            }
        };
        return drawable;
    }


    private void takeImage() {
        Intent capturePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        capturePhotoIntent.putExtra("return-data", true);


        imageUri = null;


        try {
            //photoFile = createImageFile();

            imageUri = Uri.fromFile(createImageFile());


        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (imageUri != null) {


            capturePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(capturePhotoIntent, Constants.TAKE_PICTURE_REQUEST);
           /*
            Uri photoURI = FileProvider
                    .getUriForFile(StatusUpdateActivity.this, "com.piemicrosystems.hoodcop", photoFile);
           */


        }
    }


    private File createImageFile() throws IOException {
        // Create an imageView file name
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeStamp = DATE_FORMAT.format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File cacheDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                cacheDir      /* directory */
        );
    }


    private int getOrientation(ContentResolver cr, int id) {

        String photoID = String.valueOf(id);

        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.ORIENTATION}, MediaStore.Images.Media._ID + "=?",
                new String[]{"" + photoID}, null);
        int orientation = -1;

        if (cursor.getCount() != 1) {
            return -1;
        }

        if (cursor.moveToFirst()) {
            orientation = cursor.getInt(0);
        }
        cursor.close();
        return orientation;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.SELECT_PHOTO_REQUEST:
                    imageUri = data.getData();
                    if (imageUri != null) {

                        Log.d("SELECT PHOTO REQUEST ", imageUri.toString());


                        try {
                            Log.d("ImageURI", String.valueOf(imageUri));
                            postImageBitmap = MediaStore.Images.Media.getBitmap(StatusUpdateActivity.this.getContentResolver(), imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        imageLayout.animate().alpha(1f).setDuration(500).start();
                        imageLayout.setVisibility(View.VISIBLE);

                        Picasso.with(StatusUpdateActivity.this).load(imageUri).into(selectedImage);

                        //selectedImage.setImageBitmap(bitmap);

                        withImage = true;


                    } else
                        Toast.makeText(this, "Could not load image, please try again", Toast.LENGTH_SHORT).show();

                    break;
                case Constants.TAKE_PICTURE_REQUEST:
                   /* Uri imageUri = null;

                    if (data.getData() != null) {
                        imageUri = data.getData();
                    }*/

                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        postImageBitmap = (Bitmap) extras.get("data");
                    }


                    if (imageUri != null) {

                        Log.d("CAMERA REQUEST ", imageUri.toString());


                        imageLayout.animate().alpha(1f).setDuration(500).start();
                        imageLayout.setVisibility(View.VISIBLE);
                        withImage = true;

                        Picasso.with(StatusUpdateActivity.this).load(imageUri).into(selectedImage);
                    } else
                        Toast.makeText(this, "Could not load image, please try again", Toast.LENGTH_SHORT).show();

                    break;


                case Constants.PLACE_PICKER_REQUEST:


                    final Place place = PlacePicker.getPlace(this, data);


                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
                    View mView = layoutInflaterAndroid.inflate(R.layout.custom_edittext, null);
                    android.app.AlertDialog.Builder alertDialogBuilderUserInput = new android.app.AlertDialog.Builder(this, R.style.AppAlertDialog);
                    alertDialogBuilderUserInput.setView(mView);

                    alertDialogBuilderUserInput
                            .setCancelable(false);
                    final android.app.AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();


                    final EditText edittext = (EditText) mView.findViewById(R.id.name_edittext);
                    edittext.setHint(place.getName().toString());
                    TextView ok = (TextView) mView.findViewById(R.id.ok);
                    TextView cancel = (TextView) mView.findViewById(R.id.cancel);


                    alertDialogAndroid.show();


                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String srt = edittext.getEditableText().toString();

                            String id = String.valueOf(System.currentTimeMillis());


                            Log.i("Base Activity", "Place name is " + place.getName());

                            Address newAddress = new Address(id, (srt.isEmpty()) ? place.getName().toString() : srt, place.getAddress().toString(),
                                    String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude));


                            alertDialogAndroid.dismiss();


                            TextView locationText = (TextView) findViewById(R.id.location);
                            locationText.setText(newAddress.getName());

                            locationText.setTextColor(ContextCompat.getColor(StatusUpdateActivity.this, R.color.colorAccent));


                            ImageView locationIcon = (ImageView) findViewById(R.id.locationImage);
                            locationIcon.setColorFilter(ContextCompat.getColor(StatusUpdateActivity.this, R.color.colorAccent));


                        }
                    });


                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialogAndroid.dismiss();


                        }
                    });


                    break;

            }
        }
    }


    @Override
    public void onBackPressed() {

        if (postButton.isEnabled())

            showAlertDialog(true, "", "Discard post?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            }, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }, "Cancel", 0);


        else finish();
    }


    void postFeedWithoutImage() {

        DatabaseReference feedsReference = firebaseDatabase.getReference()
                .child(Constants.HOODCOD_DATABASE)
                .child(Constants.FEEDS);

        String postId = feedsReference.push().getKey();

        DatabaseReference feedReference = feedsReference.child(postId);

        FeedItem item = new FeedItem(postId, String.valueOf(System.currentTimeMillis()),
                statusText.getText().toString(), me, withImage, "", null, null);


        feedReference.setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {


                    Toast.makeText(StatusUpdateActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();

                    updateFeedAdapterListener.onFeedItemAdded();

                    finish();

                } else {

                    Toast.makeText(StatusUpdateActivity.this, "There was a turbulence, try again?", Toast.LENGTH_SHORT).show();

                    postButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


    }


    void postFeedWithImage() {

        final DatabaseReference feedsReference = firebaseDatabase.getReference()
                .child(Constants.HOODCOD_DATABASE)
                .child(Constants.FEEDS);

        final String postId = feedsReference.push().getKey();

        Log.d("STATUS UPDATE", "POST ID IS " + postId);


        StorageReference storageReference = firebaseStorage.getReference(Constants.HOODCOD_DATABASE + "/" + Constants.FEEDS + "/" + postId + "/");

        Log.d("STATUS UPDATE", "STORAGE REF IS " + storageReference.getPath());


        Log.d("STATUS UPDATE", "IMAGE URL IS " + imageUri);

        // postImageBitmap = BitmapFactory.decodeFile(String.valueOf(imageUri));


        if (postImageBitmap != null) {

            Log.d("STATUS UPDATE", "BITMAP NOT NULL");


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            postImageBitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);

            byte[] imageData = byteArrayOutputStream.toByteArray();


            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("postId", postId)
                    .build();

            UploadTask uploadTask = storageReference.putBytes(imageData, metadata);
            uploadTask.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    withImage = true;

                    DatabaseReference feedReference = feedsReference.child(postId);

                    FeedItem item = new FeedItem(postId, String.valueOf(System.currentTimeMillis()),
                            statusText.getText().toString(), me, withImage, taskSnapshot.getDownloadUrl().toString(), null, null);


                    feedReference.setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(StatusUpdateActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                updateFeedAdapterListener.onFeedItemAdded();

                                finish();

                            } else {

                                Toast.makeText(StatusUpdateActivity.this, "There was a turbulence, try again?", Toast.LENGTH_SHORT).show();

                                postButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressBar.setVisibility(View.GONE);
                    postButton.setVisibility(View.VISIBLE);
                    Toast.makeText(StatusUpdateActivity.this, "Could not send post. Please retry", Toast.LENGTH_SHORT).show();

                }
            });

        } else {

            Log.d("STATUS UPDATE", "BITMAP IS NULL");


        }


    }


}
