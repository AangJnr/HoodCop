package com.piemicrosystems.hoodcop.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by aangjnr on 30/06/2017.
 */

public class MyProfileFragment extends Fragment {


    View rootView;
    SharedPreferences prefs;

    CircleImageView userImage;
    View background;
    View customTolbar;

    TextView rank;


    int rankValue = 350;


    public MyProfileFragment() {
        super();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_profile, container, false);

        userImage = (CircleImageView) rootView.findViewById(R.id.user_image);
        background = (View) rootView.findViewById(R.id.background);
        customTolbar = getActivity().findViewById(R.id.appBar);

        rank = (TextView) rootView.findViewById(R.id.rank);


        rank.setText(getLevel(rankValue));

        TextView name = (TextView) rootView.findViewById(R.id.name);
        TextView tagline = (TextView) rootView.findViewById(R.id.tagline);

        name.setText(prefs.getString(Constants.USER_NAME, "User Name"));
        tagline.setText(prefs.getString(Constants.TAGLINE, ""));

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(10);
        drawable.setColor(getLevelColorBAckground(getLevel(rankValue)));

        rank.setBackground(drawable);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Picasso.with(getActivity()).load(prefs.getString(Constants.USER_PHOTO_LOCAL_URL, ""))
                .into(userImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        //use your bitmap or something

                        Bitmap photo = ((BitmapDrawable) userImage.getDrawable()).getBitmap();

                        Palette.Builder builder = new Palette.Builder(photo);
                        builder.generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                                int mutedLight = palette.getDarkVibrantColor(getActivity().getResources().getColor(R.color.colorPrimary));

                                background.setBackgroundColor(mutedLight);
                                customTolbar.setBackgroundColor(mutedLight);

                                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
                                navigationView.getHeaderView(0).setBackgroundColor(mutedLight);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getActivity().getWindow().setStatusBarColor(mutedLight);

                                }
                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {// Always call the superclass so it can save the view hierarchy state
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    private void startFadeInAnimation(View v) {

        v.animate()
                .alpha(1)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setDuration(500)
                .start();

        v.setVisibility(View.VISIBLE);
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