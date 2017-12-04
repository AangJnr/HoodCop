package com.piemicrosystems.hoodcop.viewHolder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.gson.Gson;
import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.DateUtil;
import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.activity.CommentsActivity;
import com.piemicrosystems.hoodcop.activity.ImageViewActivity;
import com.piemicrosystems.hoodcop.activity.UserProfileActivity;
import com.piemicrosystems.hoodcop.object.FeedItem;
import com.piemicrosystems.hoodcop.object.PeopleWhoStarred;
import com.piemicrosystems.hoodcop.object.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 16/11/2017.
 */

public class FeedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    Boolean didIStarThisPost = false;

    View mView;
    Context mContext;
    FeedItem mfeedItem;
    SharedPreferences preferences;


    public FeedItemViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        mContext = itemView.getContext();

        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        itemView.setOnClickListener(this);
    }


    public void bindFeedItem(final FeedItem feedItem) {

        mfeedItem = feedItem;


        TextView posteeName = (TextView) mView.findViewById(R.id.posters_name);
        TextView datePosted = (TextView) mView.findViewById(R.id.date_posted);
        TextView location = (TextView) mView.findViewById(R.id.location);
        TextView follow = (TextView) mView.findViewById(R.id.follow);
        TextView status = (TextView) mView.findViewById(R.id.status);

        TextView numberOfComments = (TextView) mView.findViewById(R.id.number_of_comments);
        TextView commentsText = (TextView) mView.findViewById(R.id.textview2);

        final TextView numberOfStars = (TextView) mView.findViewById(R.id.no_of_stars);


        final ImageView star = (ImageView) mView.findViewById(R.id.star);
        ImageView comments = (ImageView) mView.findViewById(R.id.comment);
        ImageView postImage = (ImageView) mView.findViewById(R.id.selected_image);
        CircleImageView postersImage = (CircleImageView) mView.findViewById(R.id.postee_image_view);

        if (feedItem.getPostee() != null)
            Picasso.with(mContext)
                    .load(feedItem.getPostee().getImageUrl())
                    .resize(100, 100)
                    .centerCrop()
                    .into(postersImage);


        if (feedItem.getWithImage()) {
            postImage.setVisibility(View.VISIBLE);

            Picasso.with(mContext)
                    .load(feedItem.getImageUrl())
                    .resize(400, 300)
                    .into(postImage);
        }


        posteeName.setText(feedItem.getPostee().getName());
        datePosted.setText(DateUtil.convertStringToPrettyTime(feedItem.getDatePosted()));
        status.setText(feedItem.getStatus());


        if (feedItem.getComments() != null && feedItem.getComments().size() > 0) {

            int commentsNo = feedItem.getComments().size();
            numberOfComments.setText(commentsNo + "");
            commentsText.setText((commentsNo > 1) ? "c o m m e n t s" : "c o m m e n t");

        }


        if (feedItem.getStars() != null && feedItem.getStars().size() > 0) {

            numberOfStars.setText(feedItem.getStars().size() + "");


            String uid = PreferenceManager.getDefaultSharedPreferences(mContext).getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid());

            Log.d("FEED_FRAGMENT", "UID " + uid);


            if (feedItem.getStars().containsKey(uid)) {
                star.setImageResource(R.drawable.ic_star_yellow_a700_18dp);

                Log.d("FEED_FRAGMENT", "STAR SET");
                didIStarThisPost = true;

            } else {
                Log.d("FEED_FRAGMENT", "DIDN'T SET STAR");

                didIStarThisPost = false;

            }

        }


        mView.findViewById(R.id.cardLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);


                intent.putExtra("isCommenting", false);
                intent.putExtra("feedItem", new Gson().toJson(mfeedItem));
                mContext.startActivity(intent);
            }
        });


        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!didIStarThisPost) {

                    starPost(star);


                } else {

                    removeStarFromPost(star);
                }

                didIStarThisPost = !didIStarThisPost;

            }
        });


        mView.findViewById(R.id.number_of_comments_layout).setOnClickListener(this);
        comments.setOnClickListener(this);
        follow.setOnClickListener(this);
        postImage.setOnClickListener(this);
        postersImage.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, CommentsActivity.class);

        switch (v.getId()) {


            case R.id.number_of_comments_layout:

                intent.putExtra("isCommenting", false);
                intent.putExtra("feedItem", new Gson().toJson(mfeedItem));
                mContext.startActivity(intent);


                break;
            case R.id.comment:

                intent.putExtra("isCommenting", true);
                intent.putExtra("feedItem", new Gson().toJson(mfeedItem));
                mContext.startActivity(intent);


                break;


            case R.id.follow:

                Toast.makeText(mContext, "Posters UID is " + mfeedItem.getPostee().getId(), Toast.LENGTH_SHORT).show();

                break;


            case R.id.selected_image:


                Intent intent2 = new Intent(mContext, ImageViewActivity.class);
                intent2.putExtra("feedItem", new Gson().toJson(mfeedItem));
                mContext.startActivity(intent2);


                break;


            case R.id.postee_image_view:

                User user = mfeedItem.getPostee();

                Intent intent3 = new Intent(mContext, UserProfileActivity.class);
                intent3.putExtra("user", new Gson().toJson(user));

                mContext.startActivity(intent3);


                break;

        }

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


    void starPost(ImageView v) {
        animateStarButton(v);


        User me = new User(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid()), preferences.getString(Constants.USER_NAME, ""),
                preferences.getString(Constants.USER_PHOTO_LOCAL_URL, ""), preferences.getString(Constants.USERS_PHONE, ""),
                preferences.getString(Constants.USER_EMAIL, ""),
                320,
                preferences.getString(Constants.TAGLINE, ""));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


        firebaseDatabase.getReference().child(Constants.HOODCOD_DATABASE).child(Constants.FEEDS).child(mfeedItem.getId()).child(Constants.PEOPLE_WHO_STARRED)
                .child(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid())).setValue(me);

    }

    void removeStarFromPost(ImageView star) {

        star.setImageResource(R.drawable.ic_star_border_grey_400_24dp);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseDatabase.getReference().child(Constants.HOODCOD_DATABASE).child(Constants.FEEDS).child(mfeedItem.getId()).child(Constants.PEOPLE_WHO_STARRED)
                .child(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid())).setValue(null);
    }


    private void onStarClicked(DatabaseReference postRef, final TextView textView, final ImageView imageView) {


        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                FeedItem p = mutableData.getValue(FeedItem.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.getStars().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    // Unstar the post and remove self from stars
                    int starCount = p.getStars().size() - 1;


                    didIStarThisPost = false;
                    p.getStars().remove(FirebaseAuth.getInstance().getCurrentUser().getUid());


                } else {
                    // Star the post and add self to stars

                    didIStarThisPost = true;
                    //textView.setText(starCount + "");

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);


                    User me = new User(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid()), preferences.getString(Constants.USER_NAME, ""),
                            preferences.getString(Constants.USER_PHOTO_LOCAL_URL, ""), preferences.getString(Constants.USERS_PHONE, ""),
                            preferences.getString(Constants.USER_EMAIL, ""),
                            320,
                            preferences.getString(Constants.TAGLINE, ""));


                    p.getStars().put(FirebaseAuth.getInstance().getCurrentUser().getUid(), new PeopleWhoStarred(me));


                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("FEEDITEM VH", "postTransaction:onComplete:" + databaseError);

            }
        });
    }
}
