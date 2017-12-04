package com.piemicrosystems.hoodcop.application;


import android.app.Application;
import android.os.StrictMode;

/**
 * Created by aangjnr on 14/11/2017.
 */

public class HoodCop extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }
}
