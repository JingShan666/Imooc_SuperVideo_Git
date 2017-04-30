package com.hejunlin.imooc_supervideo.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hejunlin.imooc_supervideo.R;
import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.AlbumList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejunlin on 17/4/30.
 */

public class FavoriteAdapter extends BaseAdapter {

    private static final int TYPE_COUNT = 2;
    private Context mContext;
    private AlbumList mAlbumList;
    private boolean mShowChecked;
    private List<FavoriteAlbum> mFavoriteList;

    public FavoriteAdapter(Context context, AlbumList list) {
        mAlbumList = list;
        mContext = context;
        mShowChecked = false;
        mFavoriteList = new ArrayList<>();
        for (Album album : mAlbumList) {
            mFavoriteList.add(new FavoriteAlbum(album));
        }
    }

    @Override
    public int getCount() {
        return mFavoriteList.size();
    }

    @Override
    public FavoriteAlbum getItem(int position) {
        return mFavoriteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return mShowChecked ? 1: 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.favorite_item, null);
            holder = new ViewHolder();
            holder.mAlbumName = (TextView)convertView.findViewById(R.id.tv_album_name);
            holder.mCbButton = (CheckBox) convertView.findViewById(R.id.cb_favorite);
            holder.mAlbumPost = (ImageView) convertView.findViewById(R.id.iv_album_poster);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        ImageView mAlbumPost;
        TextView mAlbumName;
        CheckBox mCbButton;
    }

    class FavoriteAlbum {
        private Album mAlbum;
        private boolean mIsChecked;

        public FavoriteAlbum(Album album) {
            mAlbum = album;
            mIsChecked = false;
        }

        public Album getAlbum() {
            return mAlbum;
        }

        public void setAlbum(Album album) {
            this.mAlbum = album;
        }

        public boolean isIsChecked() {
            return mIsChecked;
        }

        public void setIsChecked(boolean isChecked) {
            this.mIsChecked = isChecked;
        }
    }
}
