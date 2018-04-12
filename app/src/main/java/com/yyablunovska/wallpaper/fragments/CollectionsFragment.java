package com.yyablunovska.wallpaper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.yyablunovska.wallpaper.R;
import com.yyablunovska.wallpaper.adapters.CollectionsAdapter;
import com.yyablunovska.wallpaper.model.Collection;
import com.yyablunovska.wallpaper.utils.Utils;
import com.yyablunovska.wallpaper.webservices.UnsplashApi;
import com.yyablunovska.wallpaper.webservices.ApiBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yuliia.yablunovska on 3/10/18.
 */

public class CollectionsFragment extends Fragment {

    public static final String KEY_ID = "collection_id";

    private static final String TAG = CollectionsFragment.class.getSimpleName();

    @BindView(R.id.fragment_collections_progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.fragment_collections_gridview)
    GridView mCollectionsView;

    private Unbinder mUnbinder;
    private CollectionsAdapter mAdapter;
    private List<Collection> mCollections = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_collections, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        getActivity().setTitle(R.string.collections_title);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mAdapter = new CollectionsAdapter(getActivity(), mCollections);
        mCollectionsView.setAdapter(mAdapter);

        showProgressBar(true);
        loadCollections();
    }

    public void loadCollections() {
        UnsplashApi api = ApiBuilder.getApi(UnsplashApi.class);
        Call<List<Collection>> call = api.getCollections();
        call.enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                if (response.isSuccessful()) {
                    mCollections.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Failed to get photos: " + response.message());
                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {
                Log.e(TAG, "Failed to get collections: " + t.getMessage());
                showProgressBar(false);
            }
        });
    }

    @OnItemClick(R.id.fragment_collections_gridview)
    public void openCollection(int position) {
        final Collection collection = mCollections.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, collection.getId());

        final CollectionFragment collectionFragment = new CollectionFragment();
        collectionFragment.setArguments(bundle);
        Utils.changeMainFragmentWithBack(getActivity(), collectionFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void showProgressBar(boolean shouldShow) {
        if (shouldShow) {
            mProgressBar.setVisibility(View.VISIBLE);
            mCollectionsView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mCollectionsView.setVisibility(View.VISIBLE);
        }
    }

}
