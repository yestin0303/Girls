package yestin.girls.utils;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by yinlu on 2016/11/26.
 */

public class MyApplication extends Application {

    public static Context context;

    public void onCreate() {
        super.onCreate();
        context = this;
        //realm 配置
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        //stetho 调试配置
        if (LogUtil.isShow()) {
            Stetho.initializeWithDefaults(this);
        }
    }


    public static Context getContext() {
        return context;
    }
}
