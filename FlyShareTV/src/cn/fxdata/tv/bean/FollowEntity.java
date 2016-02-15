package cn.fxdata.tv.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jianyong on 15/6/16.
 */
public class FollowEntity {

    @SerializedName("recent_time")
    @Expose
    private String recent_time;

    @SerializedName("recent_sets")
    @Expose
    private String recent_sets;

    @SerializedName("movie_id")
    @Expose
    private String movie_id;

    @SerializedName("movie_name")
    @Expose
    private String movie_name;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("thumb")
    @Expose
    private String thumb;

    @SerializedName("class_id")
    @Expose
    private String class_id;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getRecent_time() {
        return recent_time;
    }

    public void setRecent_time(String recent_time) {
        this.recent_time = recent_time;
    }

    public String getRecent_sets() {
        return recent_sets;
    }

    public void setRecent_sets(String recent_sets) {
        this.recent_sets = recent_sets;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "FollowEntity: recent_time:" + recent_time + ", recent_sets:"
                + recent_sets + ", movie_id:" + movie_id + ", movie_name:"
                + ", type:" + type + ", thumb:" + thumb;
    }
}
