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
import android.widget.TextView;

import com.yyablunovska.wallpaper.R;
import com.yyablunovska.wallpaper.adapters.PhotosAdapter;
import com.yyablunovska.wallpaper.model.Collection;
import com.yyablunovska.wallpaper.model.Photo;
import com.yyablunovska.wallpaper.model.User;
import com.yyablunovska.wallpaper.utils.GlideApp;
import com.yyablunovska.wallpaper.webservices.UnsplashApi;
import com.yyablunovska.wallpaper.webservices.ApiBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yuliia.yablunovska on 3/13/18.
 */

public class CollectionFragment extends Fragment {

    private final String TAG = CollectionFragment.class.getSimpleName();

    @BindView(R.id.fragment_photos_recyclerview)
    RecyclerView mPhotosView;
    @BindView(R.id.fragment_photos_progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.fragment_collection_title)
    TextView mTitleView;
    @BindView(R.id.fragment_collection_description)
    TextView mDescriptionView;
    @BindView(R.id.item_photo_user_name)
    TextView mUserNameView;
    @BindView(R.id.item_photo_user_avatar)
    CircleImageView mUserAvatarView;

    private Unbinder mUnbinder;

    private List<Photo> mPhotos = new ArrayList<>();
    private PhotosAdapter mPhotosAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_collection, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int id = getArguments().getInt(CollectionsFragment.KEY_ID);

        final Context context = getActivity();
        mPhotosView.setLayoutManager(new LinearLayoutManager(context));
        mPhotosAdapter = new PhotosAdapter(context, mPhotos);
        mPhotosView.setAdapter(mPhotosAdapter);

        showProgressBar(true);
        loadCollectionInfo(id);
        loadPhotos(id);
    }

    private void loadCollectionInfo(int collectionId) {
        final UnsplashApi api = ApiBuilder.getApi(UnsplashApi.class);
        final Call<Collection> callInfo = api.getCollectionInfo(collectionId);
        callInfo.enqueue(new Callback<Collection>() {
            @Override
            public void onResponse(Call<Collection> call, Response<Collection> response) {
                if (response.isSuccessful()) {
                    setupCollectionInfo(response.body());
                } else {
                    Log.e(TAG, "Failed to get collection info: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<Collection> call, Throwable t) {
                Log.e(TAG, "Failed to get photos: " + t.getMessage());
            }
        });
    }

    private void loadPhotos(int collectionId) {
        final UnsplashApi api = ApiBuilder.getApi(UnsplashApi.class);
        final Call<List<Photo>> callPhotos = api.getCollectionPhotos(collectionId);
        callPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    mPhotos.addAll(response.body());
                    mPhotosAdapter.notifyDataSetChanged();
                    showProgressBar(false);
                } else {
                    Log.e(TAG, "Failed to get photos: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                showProgressBar(false);
                Log.e(TAG, "Failed to get photos: " + t.getMessage());
            }
        });
    }

    private void setupCollectionInfo(Collection collection) {
        mTitleView.setText(collection.getTitle());
        mDescriptionView.setText(collection.getDescription());

        final User user = collection.getUser();
        mUserNameView.setText(user.getUserName());
        GlideApp.with(getActivity())
                .load(user.getProfileImage().getMedium())
                .into(mUserAvatarView);
    }

    private void showProgressBar(boolean shouldShow) {
        if (shouldShow) {
            mProgressBar.setVisibility(View.VISIBLE);
            mPhotosView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mPhotosView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
