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
    String OId;
    Integer sorts;//排序用的
    private boolean seleted;

    public Integer getSorts() {
        return sorts;
    }

    public void setSorts(Integer sorts) {
        this.sorts = sorts;
    }

    public boolean isSeleted() {
        return seleted;
    }

    public String getOId() {
        return OId;
    }

    public void setOId(String OId) {
        this.OId = OId;
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
