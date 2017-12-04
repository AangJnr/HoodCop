package com.piemicrosystems.hoodcop.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.object.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 15/11/2017.
 */

public class UserProfileActivity extends BaseActivity {

    User user;
    CircleImageView userImage;
    View background;
    View customTolbar;

    TextView rank;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();

        user = new Gson().fromJson(intent.getStringExtra("user"), User.class);

        if (user != null) {

            userImage = (CircleImageView) findViewById(R.id.user_image);
            background = findViewById(R.id.background);
            customTolbar = this.findViewById(R.id.appBar);
            rank = (TextView) findViewById(R.id.rank);

            TextView name = (TextView) findViewById(R.id.name);
            TextView tagline = (TextView) findViewById(R.id.tagline);


            name.setText(user.getName());
            tagline.setText(user.getTagline());

            rank.setText(getLevel(user.getLevelNo()));


            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(10);
            drawable.setColor(getLevelColorBAckground(getLevel(user.getLevelNo())));

            rank.setBackground(drawable);


            Picasso.with(this).load(PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.USER_PHOTO_LOCAL_URL, ""))
                    .into(userImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            //use your bitmap or something

                            Bitmap photo = ((BitmapDrawable) userImage.getDrawable()).getBitmap();

                            Palette.Builder builder = new Palette.Builder(photo);
                            builder.generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {

                                    int mutedLight = palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimary));

                                    background.setBackgroundColor(mutedLight);
                                    customTolbar.setBackgroundColor(mutedLight);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        getWindow().setStatusBarColor(mutedLight);

                                    }
                                }
                            });
                        }

                        @Override
                        public void onError() {

                        }
                    });


        } else {

            finish();

            Toast.makeText(this, "Blimey! An error has occured.", Toast.LENGTH_SHORT).show();
        }


        rank = (TextView) findViewById(R.id.rank);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(10);


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


    int getLevelColorBAckground(String level) {

        switch (level) {

            case Constants.ROOKIE:

                return R.color.rookie;

            case Constants.PADWAN:

                return R.color.padwan;

            case Constants.OFFICER:

                return R.color.officer;

            case Constants.VIGILANTE:

                return R.color.vigilante;

            case Constants.SUPER_HERO:

                return R.color.superhero;

            default:
                return 0;
        }

    }
}
