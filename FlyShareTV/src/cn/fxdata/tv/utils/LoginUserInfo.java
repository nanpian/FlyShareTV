package cn.fxdata.tv.utils;

import java.io.Serializable;

import cn.fxdata.tv.bean.LoginInfoReturn.UserInfo;

/**
 * @description：TODO<请描述这个类是干什么的>
 * @author：lyu
 * @date:2015-5-21
 * @version：V1.0
 */
public class LoginUserInfo extends UserInfo implements Serializable {

    private String private_code;
    /** 亲加 密码 */
    private String pwd;
    /** 角色 default,system */
    private String role;
    /** 1(允许),-1(不允许) */
    private int allow_search;

    private int accept_recommend;

    private int contact_visible;

    private int event_visible;

    public String getPrivate_code() {
        return private_code;
    }

    public void setPrivate_code(String private_code) {
        this.private_code = private_code;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getAllow_search() {
        return allow_search;
    }

    public void setAllow_search(int allow_search) {
        this.allow_search = allow_search;
    }

    public int getAccept_recommend() {
        return accept_recommend;
    }

    public void setAccept_recommend(int accept_recommend) {
        this.accept_recommend = accept_recommend;
    }

    public int getContact_visible() {
        return contact_visible;
    }

    public void setContact_visible(int contact_visible) {
        this.contact_visible = contact_visible;
    }

    public int getEvent_visible() {
        return event_visible;
    }

    public void setEvent_visible(int event_visible) {
        this.event_visible = event_visible;
    }
}
