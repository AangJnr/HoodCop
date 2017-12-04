package com.piemicrosystems.hoodcop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.NetworkBroadcastReceiver;
import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.object.Address;
import com.piemicrosystems.hoodcop.object.FeedItem;
import com.piemicrosystems.hoodcop.object.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aangjnr on 23/06/2017.
 */

public class BaseActivity extends AppCompatActivity {


    User me;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    String TAG = getClass().getSimpleName();
    NetworkBroadcastReceiver networkChangeReceiver = new NetworkBroadcastReceiver();

    SharedPreferences preferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            me = new User(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid()), preferences.getString(Constants.USER_NAME, ""),
                    preferences.getString(Constants.USER_PHOTO_LOCAL_URL, ""), preferences.getString(Constants.USERS_PHONE, ""),
                    preferences.getString(Constants.USER_EMAIL, ""),
                    320,
                    preferences.getString(Constants.TAGLINE, ""));


    }

    public void showPlacesDialog() {


        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            this.startActivityForResult(builder.build(this), Constants.PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "RequestCode " + resultCode + " resultCode " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

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

                        Log.i(TAG, "Address " + place.getAddress() + "inserted");


                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialogAndroid.dismiss();


                    }
                });


            } else {

                Toast.makeText(this, "You cancelled, didn't you?", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Something happened.");

            }
        }
    }


    public void showAlertDialog(Boolean cancelable, @Nullable String title, @Nullable String message,
                                @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                @NonNull String positiveText,
                                @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                @NonNull String negativeText, @Nullable int icon_drawable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppDialog);
        builder.setTitle(title);
        builder.setCancelable(cancelable);


        if (icon_drawable != 0) builder.setIcon(icon_drawable);
        builder.setMessage(message);

        if (onPositiveButtonClickListener != null)
            builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        if (onNegativeButtonClickListener != null)
            builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }


/*
    void showToast(String message) {
        // Toast.makeText(this, "Blimey! There's an issue :(", Toast.LENGTH_SHORT).show();
        //onDestroy();

        try {

            LayoutInflater inflater = getLayoutInflater();

            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_layout));

            // set a message
            TextView text = (TextView) layout.findViewById(R.id.toast_text);
            text.setText(message);

            // Toast...
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0,
                    (int) (Utils.getScreenHeight(getApplicationContext()) / 3));

            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }*/


    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "ON RESUME - setting broadcast");

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkChangeReceiver, filter);


    }


    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "ON PAUSE - removing  broadcast");

        unregisterReceiver(networkChangeReceiver);
    }


    String getLevel(int levelNo) {

        if (levelNo < 50)
            return Constants.ROOKIE;

        else if (levelNo >= 50 && levelNo < 100)
            return Constants.PADWAN;

        else if (levelNo >= 100 && levelNo < 200)
            return Constants.OFFICER;


        else if (levelNo >= 200 && levelNo < 300)
            return Constants.VIGILANTE;

        else if (levelNo >= 300)
            return Constants.SUPER_HERO;

        else
            return Constants.ROOKIE;

    }


    void someDummyData() {


        List<FeedItem> feedItemList = new ArrayList<>();


        feedItemList.add(new FeedItem(String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()),
                "The twelve most common letters in the English language are another popular choice when a designer just needs a meaningless phrase to test our typography and layout."
                , me, false, "", null, null));


        feedItemList.add(new FeedItem(String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()),
                "When testing out typography it’s a good idea to pick a pangram: a phrase that includes every letter in the alphabet. This phrase does just that, and makes an ideal choice for font selection."
                , me, false, "", null, null));


        feedItemList.add(new FeedItem(String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()),
                "If you’re thinking that filler text seems pretty boring and uncontroversial, you’d be wrong.\n" +
                        "\n" +
                        "Surprisingly, there is a very vocal faction of the design community that wants to see filler text banished to the original sources from whence it came. Perhaps not surprisingly, in an era of endless quibbling, there is an equally vocal contingent of designers leaping to defend the use of the time-honored tradition of greeking.\n" +
                        "\n" +
                        "The argument in favor of using filler text goes something like this: If you use real content in the design process, anytime you reach a review point you’ll end up reviewing and negotiating the content itself and not the design. This will just slow down the design process. Design first, with real content in mind (of course!), but don’t drop in the real content until the design is well on its way. Using filler text avoids the inevitable argumentation that accompanies the use of real content in the design process.\n" +
                        "\n" +
                        "Those opposed to using filler text of any sort counter by saying: The ultimate purpose of any digital product, whether a website, app, or HTML email, is to showcase real content, not to showcase great design. You can’t get a true sense for how your content plays with your design unless you use the real thing!."
                , me, true, "https://www.elastic.co/assets/bltada7771f270d08f6/enhanced-buzz-1492-1379411828-15.jpg", null, null));


        feedItemList.add(new FeedItem(String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()),
                "The twelve most common letters in the English language are another popular choice when a designer just needs a meaningless phrase to test our typography and layout."
                , me, true, "https://static.pexels.com/photos/247932/pexels-photo-247932.jpeg", null, null));


        feedItemList.add(new FeedItem(String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()),
                "The twelve most common letters in the English language are another popular choice when a designer just needs a meaningless phrase to test our typography and layout."
                , me, false, "", null, null));


        feedItemList.add(new FeedItem(String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()),
                "The twelve most common letters in the English language are another popular choice when a designer just needs a meaningless phrase to test our typography and layout."
                , me, false, "", null, null));


        firebaseDatabase.getReference().child(Constants.HOODCOD_DATABASE).child(Constants.FEEDS).setValue(feedItemList);


    }


    void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }


}
