package com.piemicrosystems.hoodcop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.fragment.ChatsFragment;
import com.piemicrosystems.hoodcop.fragment.FeedFragment;
import com.piemicrosystems.hoodcop.fragment.MyProfileFragment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 13/11/2017.
 */

public class MainActivity extends BaseActivity {


    FrameLayout frameLayout;
    String SELECTED_FRAGMENT = "FeedFragment";
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    private DrawerLayout navigationDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);


        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                navigationDrawer,
                R.string.open,
                R.string.close
        )

        {

            @Override
            public void onDrawerSlide(View drawer, float slideOffset) {

                findViewById(R.id.main_content).setX(drawer.getWidth() * slideOffset);

            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();


            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();

            }


        };


        navigationDrawer.addDrawerListener(actionBarDrawerToggle);

        View hView = navigationView.getHeaderView(0);
        CircleImageView profile_photo = (CircleImageView) hView.findViewById(R.id.profile_photo);

        Picasso.with(this)
                .load(preferences.getString(Constants.USER_PHOTO_LOCAL_URL, ""))
                .resize(100, 100)
                .centerCrop()
                .into(profile_photo);


        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {
                            case R.id.my_profile:
                                selectedFragment = new MyProfileFragment();
                                break;
                            case R.id.feed:
                                selectedFragment = new FeedFragment();
                                break;
                            case R.id.chats:
                                selectedFragment = new ChatsFragment();
                                break;

                        }

                        if (navigationDrawer.isShown())
                            navigationDrawer.closeDrawers();
                        replaceFragment(selectedFragment);
                        return true;

                    }
                });


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new FeedFragment(), "FeedFragment");
        transaction.commit();


        bottomNavigationView.getMenu().getItem(1).setChecked(true);


    }


    public void toggleDrawer(View v) {

        if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
            navigationDrawer.closeDrawer(GravityCompat.START);
        } else navigationDrawer.openDrawer(GravityCompat.START);


    }


    @Override
    public void onBackPressed() {

        if (navigationDrawer.isDrawerOpen(GravityCompat.START))
            navigationDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    void replaceFragment(final Fragment selectedFragment) {

        if (!SELECTED_FRAGMENT.equals(selectedFragment.getClass().getSimpleName()))

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, selectedFragment, selectedFragment.getClass().getSimpleName());
                    transaction.commit();

                    SELECTED_FRAGMENT = selectedFragment.getClass().getSimpleName();

                }
            }, 300);


    }
}
