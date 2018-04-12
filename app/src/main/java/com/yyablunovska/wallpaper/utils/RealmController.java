package com.yyablunovska.wallpaper.utils;

import com.yyablunovska.wallpaper.model.Photo;

import java.util.List;

import io.realm.Realm;

/**
 * @author yuliia.yablunovska on 3/19/18.
 */

public class RealmController {

    private static final String PHOTO_ID = "id";

    private final Realm mRealm;

    public RealmController() {
        mRealm = Realm.getDefaultInstance();
    }

    public void savePhoto(Photo photo) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(photo);
        mRealm.commitTransaction();
    }

    public void deletePhoto(final Photo photo) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Photo resultPhoto = realm.where(Photo.class).equalTo(PHOTO_ID, photo.getId()).findFirst();
                resultPhoto.deleteFromRealm();
            }
        });
    }

    public boolean isPhotoExist(String photoId) {
        return mRealm.where(Photo.class).equalTo(PHOTO_ID, photoId).findFirst() != null;
    }

    public List<Photo> getPhotos() {
        return mRealm.where(Photo.class).findAll();
    }
}
