package com.piemicrosystems.hoodcop.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.object.FeedItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aangjnr on 07/06/2017.
 */

public class FeedItemRecyclerAdapter extends RecyclerView.Adapter<FeedItemRecyclerAdapter.FeedItemsViewHolder> {

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    static OnItemClickListener mItemClickListener;
    String TAG = FeedItemRecyclerAdapter.class.getSimpleName();
    List<FeedItem> feedItems;
    Context context;
    boolean isStarred = false;
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();


    public FeedItemRecyclerAdapter(Context c, List<FeedItem> _feedItems) {

        this.context = c;
        this.feedItems = _feedItems;

        hasStableIds();


    }

    @Override
    public FeedItemRecyclerAdapter.FeedItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item, viewGroup, false);

        return new FeedItemRecyclerAdapter.FeedItemsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedItemRecyclerAdapter.FeedItemsViewHolder holder, int position) {

        FeedItem feedItem = feedItems.get(position);


    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return feedItems.size();

    }


////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<FeedItem> getFeedItems() {
        return feedItems;
    }


    /*@Override
    public long getItemId(int position) {


        return position;


    }*/

    public void setFeedItems(List<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }

    private int getColor(int color) {
        return ContextCompat.getColor(context, color);
    }

    private void animateStarButton(final ImageView holder) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                holder.setImageResource(R.drawable.ic_star_black_24dp);
                holder.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
            }


            @Override
            public void onAnimationEnd(Animator animation) {
                //heartAnimationsMap.remove(holder);
                // dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

    }

    void showAlertDialog(@Nullable String title, @Nullable String message,
                         @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                         @NonNull String positiveText,
                         @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                         @NonNull String negativeText, @Nullable int icon_drawable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialog);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setIcon(icon_drawable);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class FeedItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        FeedItemsViewHolder(View itemView) {
            super(itemView);


        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {


                case R.id.starred:

/*

                    if (!isStarred) {


                        animateStarButton(starred);
                        isStarred = !isStarred;
                        Toast.makeText(context, "Item added to Favorites!", Toast.LENGTH_SHORT).show();


                    } else {

                        starred.setImageResource(R.drawable.ic_star_border_grey_400_24dp);
                        Toast.makeText(context, "Item removed from Favorites!", Toast.LENGTH_SHORT).show();

                        isStarred = !isStarred;

                    }


*/

                    break;


            }

        }


    }
}


