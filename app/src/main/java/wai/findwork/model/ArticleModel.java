package wai.findwork.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Finder丶畅畅 on 2017/1/17 20:56
 * QQ群481606175
 */

public class ArticleModel extends BmobObject {
    int id;
    String title;
    String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
