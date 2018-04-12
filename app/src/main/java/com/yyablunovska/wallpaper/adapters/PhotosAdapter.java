package com.yyablunovska.wallpaper.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyablunovska.wallpaper.FullScreenPhotoActivity;
import com.yyablunovska.wallpaper.R;
import com.yyablunovska.wallpaper.model.Photo;
import com.yyablunovska.wallpaper.utils.GlideApp;
import com.yyablunovska.wallpaper.utils.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author yuliia.yablunovska on 3/11/18.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    public static final String KEY_PHOTO_ID = "photo_id";

    private Context mContext;
    private List<Photo> mPhotos;

    public PhotosAdapter(Context context, List<Photo> photos) {
        mContext = context;
        mPhotos = photos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Photo photo = mPhotos.get(position);
        holder.userName.setText(photo.getUser().getUserName());
        GlideApp.with(mContext)
                .load(photo.getUser().getProfileImage().getMedium())
                .into(holder.userAvatar);
        GlideApp.with(mContext)
                .load(photo.getUrl().getRegular())
                .placeholder(R.drawable.placeholder)
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo_user_avatar)
        CircleImageView userAvatar;
        @BindView(R.id.item_photo_user_name)
        TextView userName;
        @BindView(R.id.item_photo_photo)
        SquareImageView photo;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick
        void onClick() {
            final Intent intent = new Intent(mContext, FullScreenPhotoActivity.class);
            intent.putExtra(KEY_PHOTO_ID, mPhotos.get(getAdapterPosition()).getId());
            mContext.startActivity(intent);
        }
    }
}
