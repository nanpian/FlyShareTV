package cn.fxdata.tv.bean;

import java.io.Serializable;

import com.zgntech.core.entity.BaseReturn;

public class LoginInfoReturn extends BaseReturn {
    private LoginInfo data;

    public LoginInfo getData() {
        return data;
    }

    public void setData(LoginInfo data) {
        this.data = data;
    }

    public static class LoginInfo implements Serializable {
        private UserInfo userInfo;

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }

    }

    public static class UserInfo implements Serializable {

        private String user_id;

        private String mobile;

        private String avatar;

        private String username;

        private String role;

        private String private_code;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getPrivate_code() {
            return private_code;
        }

        public void setPrivate_code(String private_code) {
            this.private_code = private_code;
        }

    }
}
