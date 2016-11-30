package yestin.girls.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import yestin.girls.network.api.FirstPageService;
import yestin.girls.utils.MyApplication;

/**
 * Created by yinlu on 2016/11/26.
 */

public class RetrofitUtils {

    public static final String FIRST_PAGE_URL = "http://route.showapi.com/";

    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }

    public static Boolean isNetworkReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }

    private static void initOkHttpClient() {
        File cacheFile = new File(MyApplication.getContext().getCacheDir().getAbsolutePath(), "HttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        int maxAge = 60 * 60; // 有网络时 设置缓存超时时间1小时
                        int maxStale = 60 * 60 * 24; // 无网络时，设置超时为24小时
                        Request request = chain.request();
                        if (isNetworkReachable(MyApplication.getContext())) {
                            request = request.newBuilder()
                                    .cacheControl(CacheControl.FORCE_NETWORK)//有网络时只从网络获取
                                    .build();
                        } else {
                            request = request.newBuilder()
                                    .cacheControl(CacheControl.FORCE_CACHE)//无网络时只从缓存中读取
                                    .build();
                        }
                        Response response = chain.proceed(request);
                        if (isNetworkReachable(MyApplication.getContext())) {
                            response = response.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public, max-age=" + maxAge)
                                    .build();
                        } else {
                            response = response.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }
                        return response;
                    }

                })
                .connectTimeout(20, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }


    public static FirstPageService getFirstPageService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FIRST_PAGE_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(FirstPageService.class);
    }


}
