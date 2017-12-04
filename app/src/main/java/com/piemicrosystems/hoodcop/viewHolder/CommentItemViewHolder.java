package com.piemicrosystems.hoodcop.viewHolder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.DateUtil;
import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.object.Comment;
import com.piemicrosystems.hoodcop.object.PeopleWhoStarred;
import com.piemicrosystems.hoodcop.object.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aangjnr on 16/11/2017.
 */

public class CommentItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    Boolean didIStarThisPost = false;

    View mView;
    Context mContext;
    Comment mComment;

    List<String> options = new ArrayList<>();


    public CommentItemViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindFeedItem(Comment comment) {

        mComment = comment;

        ImageView commentersImage = (ImageView) mView.findViewById(R.id.commenters_image);
        TextView commentersName = (TextView) mView.findViewById(R.id.commenters_name);
        TextView commentText = (TextView) mView.findViewById(R.id.comment);

        TextView numberOfStars = (TextView) mView.findViewById(R.id.comment_stars_count);
        TextView datePosted = (TextView) mView.findViewById(R.id.date_posted);


        Picasso.with(mContext)
                .load(comment.getUser().getImageUrl())
                .resize(100, 100)
                .centerCrop()
                .into(commentersImage);


        commentersName.setText(comment.getUser().getName());
        commentText.setText(comment.getComment());


        datePosted.setText(DateUtil.convertStringToPrettyTime(comment.getDatePosted()));

        if (comment.getStars() != null && comment.getStars().size() > 0)
            numberOfStars.setText(comment.getStars().size());
        else numberOfStars.setVisibility(View.GONE);


        if (comment.getStars() != null && comment.getStars().size() > 0)


            for (HashMap.Entry<String, PeopleWhoStarred> star1 : comment.getStars().entrySet()) {

                String uid = PreferenceManager.getDefaultSharedPreferences(mContext).getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid());

                Log.d("FEED_FRAGMENT", "KEY " + star1.getKey());

                Log.d("FEED_FRAGMENT", "UID " + uid);


                if (star1.getKey().equals(uid)) {

                    options.add("Star comment");
                    options.add("Copy comment text");


                } else {

                    options.add("Remove star");
                    options.add("Copy comment text");

                }


            }


    }

    @Override
    public void onClick(View v) {


        showCommentOptionsDialog();

    }


    private void animateStarButton(final ImageView holder) {


        AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
        OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
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
                holder.setImageResource(R.drawable.ic_star_yellow_a700_18dp);
            }


            @Override
            public void onAnimationEnd(Animator animation) {
                //heartAnimationsMap.remove(holder);
                // dispatchChangeFinishedIfAllAnimationsEnded(holder);

                holder.clearAnimation();
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

    }


    void showCommentOptionsDialog() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext, R.style.AppDialog);
        builderSingle.setTitle("Comment Options");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice, options);


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selection = arrayAdapter.getItem(which);

                if (selection.equals("Star comment")) {
                    starComment();


                } else if (selection.equals("Remove star")) {

                    removeStarFromComment();
                }


            }
        });
        builderSingle.show();


    }


    void starComment() {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        User me = new User(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid()), preferences.getString(Constants.USER_NAME, ""),
                preferences.getString(Constants.USER_PHOTO_LOCAL_URL, ""), preferences.getString(Constants.USERS_PHONE, ""),
                preferences.getString(Constants.USER_EMAIL, ""),
                320,
                preferences.getString(Constants.TAGLINE, ""));


        firebaseDatabase.getReference().child(Constants.HOODCOD_DATABASE).child(Constants.FEEDS).child(mComment.getPostId()).child(Constants.COMMENTS).child(mComment.getId()).child(Constants.PEOPLE_WHO_STARRED)
                .child(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid())).setValue(me);

    }

    void removeStarFromComment() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        User me = new User(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid()), preferences.getString(Constants.USER_NAME, ""),
                preferences.getString(Constants.USER_PHOTO_LOCAL_URL, ""), preferences.getString(Constants.USERS_PHONE, ""),
                preferences.getString(Constants.USER_EMAIL, ""),
                320,
                preferences.getString(Constants.TAGLINE, ""));


        firebaseDatabase.getReference().child(Constants.HOODCOD_DATABASE).child(Constants.FEEDS).child(mComment.getPostId()).child(Constants.COMMENTS).child(mComment.getId()).child(Constants.PEOPLE_WHO_STARRED)
                .child(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid())).setValue(null);
    }
}

