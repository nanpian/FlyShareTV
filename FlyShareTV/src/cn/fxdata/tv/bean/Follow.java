package cn.fxdata.tv.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.fxdata.tv.bean.school.Datum;

/**
 * Created by Jianyong on 15/6/29.
 */
public class Follow {

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("data")
    @Expose
    private Data data;

    public class Data {
        @SerializedName("list")
        @Expose
        public List<FollowEntity> list;

        public List<FollowEntity> getList() {
            return list;
        }

        public void setList(List<FollowEntity> list) {
            this.list = list;
        }
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
