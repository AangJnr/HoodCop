package com.piemicrosystems.hoodcop;


import android.os.Environment;

import java.io.File;

/**
 * Created by aangjnr on 11/06/2017.
 */

public class Constants {

    public static final int PLACE_PICKER_REQUEST = 101;
    public static final int PERMISSION_FINE_LOCATION = 102;
    public static final int PERMISSION_CALL = 222;
    public static final int CAMERA_REQUEST = 1888;
    public static final int PERMISSION_CAMERA = 4040;
    public static final int PERMISSION_STORAGE = 6060;
    public static final int TAKE_PICTURE_REQUEST = 103;
    public static final int SELECT_PHOTO_REQUEST = 104;
    public static final float NOT_AVAILABLE = -1;
    public static final float AVAILABLE = 1;
    public static final float NEUTRAL = 0;
    public static final int READ_SMS = 390;
    public static final String ROOKIE = "Rookie";
    public static final String PADWAN = "Padwan";
    public static final String OFFICER = "Officer";
    public static final String VIGILANTE = "Vigilante";
    public static final String SUPER_HERO = "Super Hero";
    public static String HOODCOD_DATABASE = "Hoodcop Database";
    public static String USERS = "users";
    public static String FEEDS = "feeds";
    public static String PEOPLE_WHO_STARRED = "stars";
    public static String COMMENTS = "comments";
    public static boolean IS_BOTTOM_NAVIGATION_VISIBLE = false;
    public static boolean IS_NAVIGATION_DRAWER_VISIBLE = false;
    public static String USERS_PHONE = "phoneNo";
    public static String USER_NAME = "userName";
    public static String USER_AGE = "userAge";
    public static String USER_EMAIL = "userEmail";
    public static String USER_UID = "userUid";
    public static String USER_TOKEN = "userToken";
    public static String USER_PHOTO_LOCAL_URL = "userProfilePhotoLocal";
    public static String USER_RANKING = "ranking";
    public static String TAGLINE = "tagline";
    public static String IS_SIGNED_IN = "sign_in";
    public static String WAS_SIGNING_UP = "wasSigningUp";
    public static int NUMBER_OF_DEPOTS = 3;
    public static String DEFAULT_ADDRESS_UID = "default_address";
    public static String SHOULD_RESTORE = "restore";
    public static String ROOT_PICTURES = Environment
            .getExternalStorageDirectory() + File.separator + "HoodCop" + File.separator + "Pictures";


}
