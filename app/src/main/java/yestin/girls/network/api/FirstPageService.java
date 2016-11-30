package yestin.girls.network.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import yestin.girls.beans.firstPage.FirstPageInfo;

/**
 * Created by yinlu on 2016/11/26.
 */

public interface FirstPageService {
    @GET("197-1")
    Observable<FirstPageInfo> getHuaBanMeizi(@Query("num") String num,
                                             @Query("page") String page,
                                             @Query("showapi_appid") String appId,
                                             @Query("showapi_sign") String sign);
}
