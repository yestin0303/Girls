package yestin.girls.utils;

import java.util.List;

import io.realm.Realm;
import yestin.girls.beans.firstPage.FirstPageImageList;
import yestin.girls.beans.firstPage.FirstPageInfo;

/**
 * Created by yinlu on 2016/11/28.
 */
public class GirlsUtil {

    private static volatile GirlsUtil mCache;

    private GirlsUtil() {

    }

    public static GirlsUtil getInstance() {

        if (mCache == null) {
            synchronized (GirlsUtil.class) {
                if (mCache == null) {
                    mCache = new GirlsUtil();
                }
            }
        }

        return mCache;
    }

    public void putFirstImageCache(List<FirstPageInfo.ShowapiResBodyBean.NewslistBean> newslistBeen) {

        FirstPageImageList imageinfo;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (int i = 0; i < newslistBeen.size(); i++) {
            imageinfo = new FirstPageImageList();
            String title = newslistBeen.get(i).getTitle();
            String picurl = newslistBeen.get(i).getPicUrl();
            imageinfo.setPicUrl(picurl);
            imageinfo.setTitle(title);
            realm.copyToRealm(imageinfo);
        }
        realm.commitTransaction();
        realm.close();
    }
}
