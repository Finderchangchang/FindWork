package wai.findwork.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Finder丶畅畅 on 2017/2/4 22:28
 * QQ群481606175
 */

public class UserBuy extends BmobObject {
    int id;
    UserInfo user;
    UserInfo buyer;

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

    public UserInfo getBuyer() {
        return buyer;
    }

    public void setBuyer(UserInfo buyer) {
        this.buyer = buyer;
    }
}
