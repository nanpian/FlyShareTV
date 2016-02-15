package cn.fxdata.tv.bean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * hotplay 返回数据列表
 * 
 * @author Administrator
 * 
 */
public class HotPlayMoviesReturn {
    @Expose
    @SerializedName("error_code")
    private Integer error_code;
    @Expose
    @SerializedName("error_msg")
    private String error_msg;
    @Expose
    private List<List<MoviesItem>> data = new ArrayList<List<MoviesItem>>();

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public List<List<MoviesItem>> getData() {
        return data;
    }

    public void setData(List<List<MoviesItem>> data) {
        this.data = data;
    }

    public static class MoviesItem {
        @Expose
        @SerializedName("movie_id")
        private String movie_id;
        @Expose
        @SerializedName("movie_name")
        private String movie_name;
        @Expose
        @SerializedName("thumb")
        private String thumb;
        @Expose
        @SerializedName("class_id")
        private String class_id;
        @Expose
        @SerializedName("class")
        private String classes;

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

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public String getClasses() {
            return classes;
        }

        public void setClasses(String classes) {
            this.classes = classes;
        }
    }
}
