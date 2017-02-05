package wai.findwork.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Finder丶畅畅 on 2017/1/17 20:59
 * QQ群481606175
 */

public class ItemModel extends BmobObject {
    UserInfo userinfo;
    String Price;
    CodeModel Code;
    String City;

    public UserInfo getUser() {
        return userinfo;
    }

    public void setUser(UserInfo user) {
        this.userinfo = user;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public CodeModel getCode() {
        return Code;
    }

    public void setCode(CodeModel code) {
        Code = code;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
