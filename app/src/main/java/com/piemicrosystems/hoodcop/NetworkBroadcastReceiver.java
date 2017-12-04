package com.piemicrosystems.hoodcop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by aangjnr on 07/09/2017.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    Snackbar snackbar;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        boolean status = Util.checkInternetConnection(context);
        Log.d("NetworkReceiver", String.valueOf(status));
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            Log.d("NetworkReceiver", "connectivity change");


            if (!status) {

                View rootView = ((AppCompatActivity) context).getWindow().getDecorView().findViewById(android.R.id.content);

                snackbar = Snackbar.make(rootView, "No internet connection.", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();


            } else {

                if (snackbar != null) snackbar.dismiss();

            }

        } else Log.d("NetworkReceiver", "NOT Connectivity change");

    }


}
