package com.piemicrosystems.hoodcop.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.DateUtil;
import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.object.Comment;
import com.piemicrosystems.hoodcop.object.FeedItem;
import com.piemicrosystems.hoodcop.object.PeopleWhoStarred;
import com.piemicrosystems.hoodcop.viewHolder.CommentItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 15/11/2017.
 */

public class CommentsActivity extends BaseActivity {


    FeedItem feedItem = null;
    Boolean didIStarThisPost = false;
    EditText commentEditText;
    TextView postButton;
    CircleImageView usersImage;

    Boolean isCommenting;

    ProgressBar progressBar;
    RecyclerView commentsRecycler;

    FirebaseRecyclerAdapter commentsAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        Intent intent = getIntent();

        isCommenting = intent.getBooleanExtra("isCommenting", false);

        feedItem = new Gson().fromJson(intent.getStringExtra("feedItem"), FeedItem.class);

        if (feedItem != null) {
            setUpUi();


        }

    }


    void setUpUi() {

        postButton = (TextView) findViewById(R.id.post);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        //progressBar.setVisibility(View.GONE);


        commentEditText = (EditText) findViewById(R.id.comment_edittext);
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {

                    if (!postButton.isEnabled()) {
                        postButton.setEnabled(true);

                        postButton.setTextColor(ContextCompat.getColor(CommentsActivity.this, R.color.colorAccent));

                    }

                } else if (s.length() <= 0) {

                    if (postButton.isEnabled()) postButton.setEnabled(false);
                    postButton.setTextColor(ContextCompat.getColor(CommentsActivity.this, R.color.divider_2));


                }


            }
        });


        usersImage = (CircleImageView) findViewById(R.id.user_image);

        Picasso.with(this)
                .load(preferences.getString(Constants.USER_PHOTO_LOCAL_URL, ""))
                .resize(100, 100)
                .centerCrop()
                .into(usersImage);


        TextView posteeName = (TextView) findViewById(R.id.posters_name);
        TextView datePosted = (TextView) findViewById(R.id.date_posted);
        TextView location = (TextView) findViewById(R.id.location);
        TextView follow = (TextView) findViewById(R.id.follow);
        TextView status = (TextView) findViewById(R.id.status);

        TextView numberOfComments = (TextView) findViewById(R.id.number_of_comments);
        TextView commentsText = (TextView) findViewById(R.id.textview2);

        TextView numberOfStars = (TextView) findViewById(R.id.number_of_stars);


        final ImageView star = (ImageView) findViewById(R.id.star);
        ImageView share = (ImageView) findViewById(R.id.share);
        ImageView postImage = (ImageView) findViewById(R.id.selected_image);
        CircleImageView postersImage = (CircleImageView) findViewById(R.id.postee_image_view);

        Picasso.with(this)
                .load(feedItem.getPostee().getImageUrl())
                .resize(100, 100)
                .centerCrop()
                .into(postersImage);


        if (feedItem.getWithImage()) {
            postImage.setVisibility(View.VISIBLE);

            Picasso.with(this)
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

            for (HashMap.Entry<String, PeopleWhoStarred> star1 : feedItem.getStars().entrySet()) {

                String uid = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid());

                Log.d("FEED_FRAGMENT", "KEY " + star1.getKey());

                Log.d("FEED_FRAGMENT", "UID " + uid);


                if (star1.getKey().equals(uid)) {
                    star.setImageResource(R.drawable.ic_star_yellow_a700_18dp);
                    // star.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));

                    didIStarThisPost = true;

                }
            }
        }

        if (isCommenting)
            commentEditText.requestFocus();


        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!didIStarThisPost) {

                    starPost();

                    animateStarButton(star);
                    didIStarThisPost = !didIStarThisPost;
                    Toast.makeText(CommentsActivity.this, "Item starred!", Toast.LENGTH_SHORT).show();

                } else {

                    removeStarFromPost();
                    star.setImageResource(R.drawable.ic_star_border_grey_400_24dp);
                    didIStarThisPost = !didIStarThisPost;
                }
            }
        });


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);


                DatabaseReference commentReference = firebaseDatabase.getReference().child(Constants.HOODCOD_DATABASE)
                        .child(Constants.FEEDS)
                        .child(feedItem.getId())
                        .child(Constants.COMMENTS);

                String commentId = commentReference.push().getKey();
                final Comment comment = new Comment(feedItem.getId(), commentId, me, commentEditText.getText().toString(), String.valueOf(System.currentTimeMillis()));


                commentReference.child(commentId).setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressBar.setVisibility(View.GONE);
                        postButton.setVisibility(View.VISIBLE);

                        postButton.setEnabled(false);

                        if (task.isSuccessful()) {

                            commentEditText.setText(null);
                            hideSoftKeyboard();


                        } else {

                            Toast.makeText(CommentsActivity.this, "Comment got lost :( Please retry", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


            }
        });


        commentsRecycler = (RecyclerView) findViewById(R.id.comments_recycler);


        setUpFirebaseRecyclerAdapter();


    }


    void starPost() {

        firebaseDatabase.getReference().child(Constants.HOODCOD_DATABASE).child(Constants.FEEDS).child(feedItem.getId()).child(Constants.PEOPLE_WHO_STARRED)
                .child(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid())).setValue(me);

    }

    void removeStarFromPost() {
        firebaseDatabase.getReference().child(Constants.HOODCOD_DATABASE).child(Constants.FEEDS).child(feedItem.getId()).child(Constants.PEOPLE_WHO_STARRED)
                .child(preferences.getString(Constants.USER_UID, FirebaseAuth.getInstance().getCurrentUser().getUid())).setValue(null);
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


    void setUpFirebaseRecyclerAdapter() {

        DatabaseReference commentReference = firebaseDatabase.getReference()
                .child(Constants.HOODCOD_DATABASE)
                .child(Constants.FEEDS)
                .child(feedItem.getId()).child(Constants.COMMENTS);

        commentsAdapter = new FirebaseRecyclerAdapter<Comment, CommentItemViewHolder>
                (Comment.class, R.layout.comment_item_view, CommentItemViewHolder.class,
                        commentReference) {

            @Override
            protected void populateViewHolder(final CommentItemViewHolder viewHolder, Comment model, int position) {

                Log.d("FeedFragment", "BINDING VIEW " + position + " WITH ID " + model.getId());

                //viewHolder.bindFeedItem((FeedItem) mFirebaseAdapter.getItem(viewHolder.getAdapterPosition()));

                viewHolder.bindFeedItem(model);
                //id = Long.parseLong(model.getId());
            }


            @Override
            public long getItemId(int position) {


                return System.currentTimeMillis();
            }
        };


        commentsAdapter.setHasStableIds(true);

        commentsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        commentsRecycler.setAdapter(commentsAdapter);

        commentsRecycler.scrollToPosition(0);


        Log.d("FeedFragment", "SETTING ADAPTER");


    }

}
