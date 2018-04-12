package com.yyablunovska.wallpaper.webservices;

import com.yyablunovska.wallpaper.model.Collection;
import com.yyablunovska.wallpaper.model.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author yuliia.yablunovska on 3/11/18.
 */

public interface UnsplashApi {
    @GET("photos")
    Call<List<Photo>> getPhotos();

    @GET("collections/featured")
    Call<List<Collection>> getCollections();

    @GET("collections/{id}")
    Call<Collection> getCollectionInfo(@Path("id") int id);

    @GET("collections/{id}/photos")
    Call<List<Photo>> getCollectionPhotos(@Path("id") int id);

    @GET("photos/{id}")
    Call<Photo> getPhoto(@Path("id") String id);
}
