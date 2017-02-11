package wai.findwork.model;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by Finder丶畅畅 on 2017/1/17 20:36
 * QQ群481606175
 */

public class UserInfo extends BmobUser implements Serializable{
    int id;
    private String realname;
    private String remark;
    private String iconurl;
    private String cardnum;
    private String gongzi;
    private boolean sex;

    CodeModel type;
    String typeName;


    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getGongzi() {
        return gongzi;
    }

    public CodeModel getType() {
        return type;
    }

    public void setType(CodeModel type) {
        this.type = type;
    }

    public void setGongzi(String gongzi) {
        this.gongzi = gongzi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }
}
