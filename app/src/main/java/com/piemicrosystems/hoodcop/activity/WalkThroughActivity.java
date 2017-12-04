package com.piemicrosystems.hoodcop.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.adapter.WalkThroughViewPagerAdapter;
import com.piemicrosystems.hoodcop.object.WalkThrough;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aangjnr on 13/11/2017.
 */

public class WalkThroughActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    ViewPager viewPager;
    WalkThroughViewPagerAdapter mAdapter;
    List<WalkThrough> introSlideItems = new ArrayList<>();
    int dotsCount;
    ImageView[] dots;
    TextView next;
    TextView skip;
    TextView done;
    private LinearLayout pager_indicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        setContentView(R.layout.activity_app_intro);


        next = (TextView) findViewById(R.id.next);
        skip = (TextView) findViewById(R.id.skip);

        done = (TextView) findViewById(R.id.done);
        done.setAlpha(0f);
        done.setVisibility(View.GONE);


        introSlideItems.add(new WalkThrough("Modern technology", "Modern crime calls for modern technologies. Unlock the power of real-time location aware emergency assistance on your mobile device.", R.drawable.modern_tech));
        introSlideItems.add(new WalkThrough("Location Awareness", "We know the importance of being accurate in emergency situations. Your help is assisted with accurate turn-by-turn navigation right to the spot where you are.", R.drawable.location_awareness));
        introSlideItems.add(new WalkThrough("Real-time Assistance", "Every second counts in emergency situations. We dispatch the right security terms and notify people in your contact list instantly.", R.drawable.realtime_assistance));
        introSlideItems.add(new WalkThrough("Know your community", "Post on issues around your community, create the awareness and discuss with your friends on possible solutions to solve them.", R.drawable.post_feed_2));


        viewPager = (ViewPager) findViewById(R.id.viewPager);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        mAdapter = new WalkThroughViewPagerAdapter(this, introSlideItems);

        viewPager.setPadding(64, 0, 64, 64);
        viewPager.setClipToPadding(false);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(32);

        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setPageTransformer(false, new LogInPageTransformer());


        viewPager.addOnPageChangeListener(this);
        setUiPageViewController();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(introSlideItems.size() - 1, true);

            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WalkThroughActivity.this, SignInActivity.class));
                supportFinishAfterTransition();
            }
        });


    }


    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_selected_item));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_item));

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_selected_item));
        }

        dots[position].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_item));


        if (viewPager.getCurrentItem() == introSlideItems.size() - 1) {

            skip.animate().alpha(0f).start();
            skip.setVisibility(View.GONE);

            next.animate().alpha(0f).start();
            next.setVisibility(View.GONE);

            done.animate().alpha(1f).start();
            done.setVisibility(View.VISIBLE);


        } else {
            skip.animate().alpha(1f).start();
            skip.setVisibility(View.VISIBLE);

            next.animate().alpha(1f).start();
            next.setVisibility(View.VISIBLE);


            done.animate().alpha(0f).start();
            done.setVisibility(View.GONE);


        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class LogInPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        View image;
        View header_text;
        View sub_text;


        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();
            float absPosition = Math.abs(position);

            image = page.findViewById(R.id.iv1);

            header_text = page.findViewById(R.id.header_text);
            sub_text = page.findViewById(R.id.sub_text);


            Log.i("absPosition", String.valueOf(position));


            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                //page.setAlpha(0);

            } else if (position < 0) {

                image.setTranslationX((float) -((absPosition) * 1.5 * pageWidth));
                header_text.setAlpha(1.0f - absPosition);
                sub_text.setAlpha((float) (1.0f - absPosition * 0.7));
            } else if (position < 1 && position > 0) {

            /*name.setTranslationX((float) ((absPosition) * 1.5 * pageWidth));
            email_2.setTranslationX((float) ((absPosition) * 1.2 * pageWidth));
            pass_2.setTranslationX((float) ((absPosition) * 1.0 * pageWidth));
            phone.setTranslationX((float) ((absPosition) * 0.75 * pageWidth));*/

                image.setTranslationX(0);
                header_text.setAlpha(-(-1.0f - absPosition * 2 * pageWidth));
                sub_text.setAlpha((float) -(-1.0f - absPosition * 1.5 * pageWidth));
                Log.i("absPosition", String.valueOf(absPosition));


            }
        }
    }
}
