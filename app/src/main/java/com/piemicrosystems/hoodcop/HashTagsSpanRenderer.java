package com.piemicrosystems.hoodcop;


/**
 * Created by aangjnr on 09/03/2017.
 */


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.jmpergar.awesometext.AwesomeTextHandler;


public class HashTagsSpanRenderer implements AwesomeTextHandler.ViewSpanRenderer {

    private final static int backgroundResource = R.drawable.hashtags_background;
    private final static int textColorResource = android.R.color.background_dark;
    private int textSizeInDips = 13;
    private float density;


    @Override
    public View getView(String text, Context context) {

        density = context.getResources().getDisplayMetrics().density;
        if (density <= 1.5) textSizeInDips = 10;

        TextView view = new TextView(context);
        view.setText(text.substring(1));


        view.setTextSize(Util.dipsToPixels(context, textSizeInDips));
        view.setBackgroundResource(backgroundResource);
        int textColor = ContextCompat.getColor(context, textColorResource);
        view.setTextColor(textColor);
        return view;
    }

}