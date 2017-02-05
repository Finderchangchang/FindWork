package wai.findwork.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Finder丶畅畅 on 2017/1/17 20:57
 * QQ群481606175
 */

public class TypeOfWorkModel extends BmobObject {
    String wages;
    UserInfo userinfo;
    CodeModel Type;
    String City;

    public String getWages() {
        return wages;
    }

    public void setWages(String wages) {
        this.wages = wages;
    }

    public UserInfo getUser() {
        return userinfo;
    }

    public void setUser(UserInfo user) {
        this.userinfo = user;
    }

    public CodeModel getType() {
        return Type;
    }

    public void setType(CodeModel type) {
        Type = type;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
