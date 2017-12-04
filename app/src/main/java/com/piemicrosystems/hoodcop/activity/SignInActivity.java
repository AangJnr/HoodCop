package com.piemicrosystems.hoodcop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.object.User;

import java.util.Arrays;

/**
 * Created by aangjnr on 15/11/2017.
 */

public class SignInActivity extends BaseActivity {


    private static final String TAG = "LoginActivity";
    private static final int PHONE_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        setContentView(R.layout.activity_sign_in);


        mAuth = FirebaseAuth.getInstance();
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }


        findViewById(R.id.phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                phoneSignIn();
            }
        });


      /*  findViewById(R.id.google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        });


        findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        });


        findViewById(R.id.twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        });*/


    }


    void phoneSignIn() {


        if (mAuth.getCurrentUser() != null) {


            // already signed in
            Log.i(TAG, "Already signed in");

            Log.d(TAG, "Uid is " + mAuth.getCurrentUser().getUid());
            Log.d(TAG, "Phone number is " + mAuth.getCurrentUser().getPhoneNumber());

            preferences.edit()
                    .putString("firebaseUid", mAuth.getCurrentUser().getUid())
                    .apply();

            preferences.edit()
                    .putString(Constants.USERS_PHONE, mAuth.getCurrentUser().getPhoneNumber())
                    .apply();


            PreferenceManager.getDefaultSharedPreferences(SignInActivity.this).edit()
                    .putBoolean(Constants.IS_SIGNED_IN, true)
                    .apply();


            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        } else {
            // not signed in

            Log.i(TAG, "Not signed in");

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                    ))
                            .build(),
                    PHONE_SIGN_IN);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == PHONE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {


                Log.d(TAG, "Uid is " + mAuth.getCurrentUser().getUid());
                Log.d(TAG, "Phone number is " + mAuth.getCurrentUser().getPhoneNumber());

                //Dummy data
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.TAGLINE, "Never take a day off!!");
                editor.putString(Constants.USER_NAME, "Camara-laye");
                editor.putString(Constants.USER_EMAIL, "cion.aang@gmail.com");
                editor.putString(Constants.USER_PHOTO_LOCAL_URL, "https://static-cdn.123rf.com/images/v5/index-thumbnail/52175178-a.jpg");


                editor.putString("firebaseUid", mAuth.getCurrentUser().getUid());
                editor.putString(Constants.USERS_PHONE, mAuth.getCurrentUser().getPhoneNumber());
                editor.putBoolean(Constants.IS_SIGNED_IN, true);
                editor.apply();


                // someDummyData();

                User me = new User(preferences.getString(Constants.USER_UID, ""), preferences.getString(Constants.USER_NAME, ""),
                        preferences.getString(Constants.USER_PHOTO_LOCAL_URL, ""), preferences.getString(Constants.USERS_PHONE, ""),
                        preferences.getString(Constants.USER_EMAIL, ""),
                        320,
                        preferences.getString(Constants.TAGLINE, ""));

                firebaseDatabase.getReference().child(Constants.HOODCOD_DATABASE).child(Constants.USERS).child(mAuth.getCurrentUser().getUid()).setValue(me)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        e.printStackTrace();

                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });


                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e("Login", "Login canceled by User");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e("Login", "No Internet Connection");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login", "Unknown Error");
                    return;
                }
            }
            Log.e("Login", "Unknown sign in response");
        }
    }


}


