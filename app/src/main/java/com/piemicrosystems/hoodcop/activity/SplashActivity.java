package com.piemicrosystems.hoodcop.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.R;

import static java.lang.Thread.sleep;

/**
 * Created by aangjnr on 17/06/2017.
 */

public class SplashActivity extends BaseActivity {

    ImageView image;
    TextView text;

    String TAG = SplashActivity.class.getSimpleName();


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
        setContentView(R.layout.activity_splash);


        image = (ImageView) findViewById(R.id.image_1);


        image.setTranslationY(-100f);
        image.setAlpha(0f);


        text = (TextView) findViewById(R.id.text_1);

        text.setTranslationY(-100f);
        text.setAlpha(0f);


        startAnimations();


    }

    private void startAnimations() {


        image.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(800)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        text.animate()
                                .translationY(0)
                                .alpha(1f)
                                .setDuration(500)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        try {
                                            int waited = 0;
                                            // Splash screen pause time
                                            while (waited < 1000) {
                                                sleep(100);
                                                waited += 100;
                                            }


                                            moveToNextActivity();


                                        } catch (InterruptedException e) {
                                            // do nothing
                                        } finally {
                                            supportFinishAfterTransition();

                                        }


                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .setStartDelay(200)
                                .start();


                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void moveToNextActivity() {
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.IS_SIGNED_IN, false)) {
            Log.i(TAG, "Activity name was  null, It means user completed signup process");
            Log.i(TAG, "Is signed in? " + PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.IS_SIGNED_IN, false));

            startActivity(new Intent(this, WalkThroughActivity.class));

        } else {

            Log.i(TAG, "User is signed in, Start main activity");

            startActivity(new Intent(this, MainActivity.class));

        }

    }


}
