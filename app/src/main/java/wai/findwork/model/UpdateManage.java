package wai.findwork.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/2/20.
 */

public class UpdateManage extends BmobObject {
    private String project;
    private String version;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
