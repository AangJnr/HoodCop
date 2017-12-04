package com.piemicrosystems.hoodcop.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.piemicrosystems.hoodcop.Callbacks;
import com.piemicrosystems.hoodcop.Constants;
import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.activity.StatusUpdateActivity;
import com.piemicrosystems.hoodcop.object.FeedItem;
import com.piemicrosystems.hoodcop.viewHolder.FeedItemViewHolder;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by aangjnr on 30/06/2017.
 */

public class FeedFragment extends Fragment implements Callbacks.UpdateFeedAdapterListener {


    static Callbacks.UpdateBottomSheetUiListener bottomSheetListener;
    View rootView;
    CircleImageView profilePhoto;
    FirebaseRecyclerAdapter mFirebaseAdapter;
    RecyclerView feedRecycler;
    DatabaseReference feedReference;

    SwipeRefreshLayout swipeRefreshLayout;
    long id = 0;

    public FeedFragment() {
        super();

    }

    public static FeedFragment newInstance() {

        return new FeedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            getActivity().findViewById(R.id.appBar).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
            navigationView.getHeaderView(0).setBackground(getResources().getDrawable(R.drawable.gradient_background_main));

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        profilePhoto = (CircleImageView) rootView.findViewById(R.id.profile_photo);
        feedRecycler = (RecyclerView) rootView.findViewById(R.id.feed_recycler);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setRefreshing(true);


        Picasso.with(getActivity())
                .load(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.USER_PHOTO_LOCAL_URL, ""))
                .resize(100, 100)
                .centerCrop()
                .into(profilePhoto);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        StatusUpdateActivity.OnFeedItemUpdatedistener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {// Always call the superclass so it can save the view hierarchy state
        super.onActivityCreated(savedInstanceState);


        feedReference = FirebaseDatabase.getInstance().getReference().child(Constants.HOODCOD_DATABASE).child(Constants.FEEDS);

        setUpFirebaseRecyclerAdapter();

        feedRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scrolling up

                    hideToolbarAndBottomBar();


                } else {
                    // Scrolling down

                    showToolbarAndBottomBar();

                }


            }
        });


        rootView.findViewById(R.id.whatsNewLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), StatusUpdateActivity.class);

                Pair<View, String> parent = Pair.create((View) rootView.findViewById(R.id.whatsNewCv), "whatsNewLayout");
                Pair<View, String> civPair = Pair.create((View) profilePhoto, "whatsNewCiv");


                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        parent, civPair);

                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpFirebaseRecyclerAdapter();
            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    private void startFadeInAnimation(View v) {

        v.animate()
                .alpha(1)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setDuration(500)
                .start();

        v.setVisibility(View.VISIBLE);
    }

    void setUpFirebaseRecyclerAdapter() {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<FeedItem, FeedItemViewHolder>
                (FeedItem.class, R.layout.feed_item, FeedItemViewHolder.class,
                        feedReference) {

            @Override
            protected void populateViewHolder(final FeedItemViewHolder viewHolder, FeedItem model, int position) {

                Log.d("FeedFragment", "BINDING VIEW " + position + " WITH ID " + model.getId());


                //viewHolder.bindFeedItem((FeedItem) mFirebaseAdapter.getItem(viewHolder.getAdapterPosition()));

                viewHolder.bindFeedItem(model);

            }
        };


        // mFirebaseAdapter.setHasStableIds(true);

        feedRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        feedRecycler.setAdapter(mFirebaseAdapter);

        feedRecycler.buildDrawingCache(true);

        feedRecycler.scrollToPosition(mFirebaseAdapter.getItemCount());


        Log.d("FeedFragment", "SETTING ADAPTER");


        swipeRefreshLayout.setRefreshing(false);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }


    void showToolbarAndBottomBar() {

        View customToolbar = getActivity().findViewById(R.id.appBar);
        View bottomNav = getActivity().findViewById(R.id.bottom_navigation);


        // customToolbar.animate().translationY(-Util.getScreenHeight(getActivity())).setDuration(500).start();
        bottomNav.animate().translationY(0).setDuration(300).setInterpolator(new AccelerateInterpolator())
                .start();


    }


    void hideToolbarAndBottomBar() {

        View customToolbar = getActivity().findViewById(R.id.appBar);
        View bottomNav = getActivity().findViewById(R.id.bottom_navigation);


        // customToolbar.animate().translationY(0f).setDuration(300).start();
        bottomNav.animate().translationY(bottomNav.getHeight() * 2)
                .setDuration(500)
                .setInterpolator(new BounceInterpolator())
                .start();


    }


    @Override
    public void onFeedItemAdded() {

        feedRecycler.smoothScrollToPosition(mFirebaseAdapter.getItemCount());

    }
}