package com.piemicrosystems.hoodcop;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jmpergar.awesometext.AwesomeTextHandler;


/**
 * Created by aangjnr on 09/03/2017.
 */

public class MentionsSpanRenderer implements AwesomeTextHandler.ViewSpanRenderer, AwesomeTextHandler.ViewSpanClickListener {

    private final static int backgroundResource = R.drawable.mentions_background;
    private final static int textColorResource = android.R.color.white;
    public static Boolean onLongClick = false;
    public static String _text = null;
    float density;
    String TAG = MentionsSpanRenderer.class.getSimpleName();
    private int textSizeInDips = 13;

    @Override
    public View getView(String text, Context context) {
        TextView view = new TextView(context);
        view.setText(text.substring(1));


        density = context.getResources().getDisplayMetrics().density;

        if (density <= 1.5) textSizeInDips = 10;


        view.setTextSize(Util.dipsToPixels(context, textSizeInDips));
        view.setBackgroundResource(backgroundResource);
        int textColor = ContextCompat.getColor(context, textColorResource);
        view.setTextColor(textColor);
        return view;
    }

    @Override
    public void onClick(String text, Context context) {

        _text = text;
        if (onLongClick != null && !onLongClick) {

            Log.i(TAG, "Tag selected is " + text.substring(1));
            //Start activity or just inflate a custom fragment with the Referals of the custom tags
/*

            Intent intent = new Intent(context, RefralByTagActivity.class);
            intent.putExtra("tagName", text.substring(1));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
*/


        }

        onLongClick = false;
        _text = text;
    }

}