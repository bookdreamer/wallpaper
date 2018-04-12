package com.yyablunovska.wallpaper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yyablunovska.wallpaper.R;
import com.yyablunovska.wallpaper.model.Collection;
import com.yyablunovska.wallpaper.utils.GlideApp;
import com.yyablunovska.wallpaper.utils.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yuliia.yablunovska on 3/13/18.
 */

public class CollectionsAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Collection> mCollections;

    public CollectionsAdapter(Context context, List<Collection> collections) {
        mContext = context;
        mCollections = collections;
    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public Object getItem(int position) {
        return mCollections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCollections.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_collection, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Collection collection = mCollections.get(position);
        holder.mTitle.setText(collection.getTitle());
        holder.mTotal.setText(mContext.getString(R.string.collection_total_photos, collection.getTotalPhotos()));
        GlideApp.with(mContext)
                .load(collection.getCoverPhoto().getUrl().getRegular())
                .placeholder(R.drawable.placeholder)
                .into(holder.mCover);
        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.item_collection_photo)
        SquareImageView mCover;
        @BindView(R.id.item_collection_title)
        TextView mTitle;
        @BindView(R.id.item_collection_total_photos)
        TextView mTotal;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
