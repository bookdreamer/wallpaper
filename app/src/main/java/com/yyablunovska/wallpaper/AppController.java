package com.yyablunovska.wallpaper;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author yuliia.yablunovska on 3/19/18.
 */

public class AppController extends Application {

    private static final String DB_NAME = "wallpaper.realm";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().name(DB_NAME).build());
    }
}
