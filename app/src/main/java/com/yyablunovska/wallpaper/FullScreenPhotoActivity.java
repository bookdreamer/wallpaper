package com.yyablunovska.wallpaper;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.yyablunovska.wallpaper.adapters.PhotosAdapter;
import com.yyablunovska.wallpaper.model.Photo;
import com.yyablunovska.wallpaper.model.User;
import com.yyablunovska.wallpaper.utils.GlideApp;
import com.yyablunovska.wallpaper.utils.RealmController;
import com.yyablunovska.wallpaper.utils.Utils;
import com.yyablunovska.wallpaper.webservices.UnsplashApi;
import com.yyablunovska.wallpaper.webservices.ApiBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yuliia.yablunovska on 3/16/18.
 */

public class FullScreenPhotoActivity extends AppCompatActivity {

    private static final String TAG = FullScreenPhotoActivity.class.getSimpleName();

    @BindView(R.id.activity_photo_photo)
    ImageView mPhotoView;
    @BindView(R.id.activity_photo_user_avatar)
    CircleImageView mUserAvatar;
    @BindView(R.id.activity_photo_username)
    TextView mUsername;
    @BindView(R.id.activity_photo_actions)
    FloatingActionMenu mActionMenu;
    @BindView(R.id.activity_photo_favorite)
    FloatingActionButton mFavoriteButton;
    @BindView(R.id.activity_photo_wallpaper)
    FloatingActionButton mWallpaperButton;

    private static final int RES_LIKE = R.drawable.ic_action_like;
    private static final int RES_UNLIKE = R.drawable.ic_action_unlike;

    private Unbinder mUnbinder;
    private Bitmap mPhotoBitmap;
    private Photo mPhoto;

    private RealmController mRealmController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulscreen_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mUnbinder = ButterKnife.bind(this);

        final Bundle bundle = getIntent().getExtras();
        final String photoId = bundle.getString(PhotosAdapter.KEY_PHOTO_ID);
        loadPhoto(photoId);

        mRealmController = new RealmController();
    }

    @OnClick(R.id.activity_photo_favorite)
    public void onFavoriteClicked() {
        if (mPhoto != null) {
            if (mRealmController.isPhotoExist(mPhoto.getId())) {
                mRealmController.deletePhoto(mPhoto);
                Toast.makeText(this, R.string.favorite_removed, Toast.LENGTH_SHORT).show();
            } else {
                mRealmController.savePhoto(mPhoto);
                Toast.makeText(this, R.string.favorite_added, Toast.LENGTH_SHORT).show();
            }
            updateFavoriteButton();
        }

        mActionMenu.close(true);
    }

    @OnClick(R.id.activity_photo_wallpaper)
    public void onWallpaperClicked() {
        if (mPhotoBitmap != null) {
            if (Utils.setWallpaper(this, centerCropBitmap())) {
                Toast.makeText(this, getString(R.string.wallpaper_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.wallpaper_failed), Toast.LENGTH_SHORT).show();
            }
        }
        mActionMenu.close(true);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap centerCropBitmap() {
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        } else {
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        return ThumbnailUtils.extractThumbnail(mPhotoBitmap, size.x, size.y);
    }

    private void loadPhoto(String id) {
        Log.d(TAG, "loadPhoto...");
        final UnsplashApi api = ApiBuilder.getApi(UnsplashApi.class);
        Call<Photo> call = api.getPhoto(id);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()) {
                    mPhoto = response.body();
                    showPhoto();
                    updateFavoriteButton();
                } else {
                    Log.d(TAG, "Error on getting photo: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Log.d(TAG, "Error on getting photo: " + t.getMessage());
            }
        });
    }

    private void showPhoto() {
        GlideApp.with(this)
                .asBitmap()
                .load(mPhoto.getUrl().getRegular())
                .placeholder(R.drawable.placeholder)
                .into(new BitmapImageViewTarget(mPhotoView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        mPhotoBitmap = resource;
                    }
                });
        final User user = mPhoto.getUser();
        mUsername.setText(user.getUserName());
        GlideApp.with(this)
                .load(user.getProfileImage().getMedium())
                .into(mUserAvatar);
    }

    private void updateFavoriteButton() {
        final int resFavorite;
        if (mRealmController.isPhotoExist(mPhoto.getId())) {
            resFavorite = RES_UNLIKE;
        } else {
            resFavorite = RES_LIKE;
        }
        mFavoriteButton.setImageResource(resFavorite);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
