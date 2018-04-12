package com.yyablunovska.wallpaper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yyablunovska.wallpaper.R;
import com.yyablunovska.wallpaper.adapters.PhotosAdapter;
import com.yyablunovska.wallpaper.model.Photo;
import com.yyablunovska.wallpaper.webservices.UnsplashApi;
import com.yyablunovska.wallpaper.webservices.ApiBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yuliia.yablunovska on 3/10/18.
 */

public class PhotosFragment extends Fragment {

    private static final String TAG = PhotosFragment.class.getSimpleName();

    @BindView(R.id.fragment_photos_progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.fragment_photos_recyclerview)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    private PhotosAdapter mPhotosAdapter;
    private List<Photo> photos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_photos, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Context context = getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mPhotosAdapter = new PhotosAdapter(context, photos);
        mRecyclerView.setAdapter(mPhotosAdapter);

        showProgressBar(true);
        loadPhotos();
    }

    private void loadPhotos() {
        UnsplashApi api = ApiBuilder.getApi(UnsplashApi.class);
        Call<List<Photo>> call = api.getPhotos();
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (!isVisible()) {
                    return;
                }
                if (response.isSuccessful()) {
                    photos.addAll(response.body());
                    mPhotosAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Failed to get photos: " + response.message());
                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (!isVisible()) {
                    return;
                }
                Log.e(TAG, "Failed to get photos: " + t.getMessage());
                showProgressBar(false);
            }
        });
    }

    private void showProgressBar(boolean shouldShow) {
        if (shouldShow) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
