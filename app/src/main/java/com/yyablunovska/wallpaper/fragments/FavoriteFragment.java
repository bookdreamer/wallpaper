package com.yyablunovska.wallpaper.fragments;

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
import android.widget.TextView;

import com.yyablunovska.wallpaper.R;
import com.yyablunovska.wallpaper.adapters.PhotosAdapter;
import com.yyablunovska.wallpaper.model.Photo;
import com.yyablunovska.wallpaper.utils.RealmController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * @author yuliia.yablunovska on 3/10/18.
 */

public class FavoriteFragment extends Fragment {

    @BindView(R.id.fragment_favorites_empty_view)
    TextView mEmptyView;
    @BindView(R.id.fragment_favorites_list)
    RecyclerView mFavoritesView;

    private Unbinder mUnbinder;

    private List<Photo> mPhotosList = new ArrayList<>();
    private PhotosAdapter mPhotosAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mFavoritesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPhotosAdapter = new PhotosAdapter(getActivity(), mPhotosList);
        mFavoritesView.setAdapter(mPhotosAdapter);
        getActivity().setTitle(R.string.favorites_title);

        loadFavorites();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadFavorites();
    }

    private void loadFavorites() {
        final List<Photo> favorites = new RealmController().getPhotos();
        if (favorites.isEmpty()) {
            showEmptyView(true);
        } else {
            mPhotosList.clear();
            mPhotosList.addAll(favorites);
            mPhotosAdapter.notifyDataSetChanged();
            showEmptyView(false);
        }
    }

    private void showEmptyView(boolean show) {
        mEmptyView.setVisibility(show ? View.VISIBLE : View.GONE);
        mFavoritesView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
