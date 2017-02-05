package wai.findwork.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Finder丶畅畅 on 2017/2/4 22:52
 * QQ群481606175
 */

public class RiLi extends BmobObject {
    int id;
    UserInfo user;
    String content;//日记的内容

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
