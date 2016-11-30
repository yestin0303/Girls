package yestin.girls.beans.firstPage;

import io.realm.RealmObject;

/**
 * Created by yinlu on 2016/11/27.
 */
public class FirstPageImageList extends RealmObject {

    private String title;
    private String picUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }


}
