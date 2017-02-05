package wai.findwork.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Finder丶畅畅 on 2017/1/17 20:38
 * QQ群481606175
 */

public class CodeModel extends BmobObject {
    int id;
    String Type;
    String Name;
    String objectid;
    private boolean seleted;

    public boolean isSeleted() {
        return seleted;
    }

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public void setSeleted(boolean seleted) {
        this.seleted = seleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
