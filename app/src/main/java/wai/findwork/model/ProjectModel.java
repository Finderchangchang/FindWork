package wai.findwork.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Finder丶畅畅 on 2017/1/17 21:00
 * QQ群481606175
 */

public class ProjectModel extends BmobObject {
    UserInfo userinfo;
    String ProjectName;
    String GuiMo;
    String City;

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getGuiMo() {
        return GuiMo;
    }

    public void setGuiMo(String guiMo) {
        GuiMo = guiMo;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
