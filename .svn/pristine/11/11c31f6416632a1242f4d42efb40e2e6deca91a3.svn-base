package cn.fxdata.tv.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDetail {
    @Expose
    @SerializedName("error_code")
    private Integer error_code;
    @Expose
    @SerializedName("error_msg")
    private String error_msg;
    @Expose
    @SerializedName("data")
    private MovieDetailData data;

    public int getError_code() {
        return error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public MovieDetailData getDetail() {
        return data;
    }

    public static class MovieDetailData {
        @Expose
        @SerializedName("movie_id")
        private String movie_id;
        @Expose
        @SerializedName("movie_name")
        private String movie_name;
        @Expose
        @SerializedName("director")
        private String director;
        @Expose
        @SerializedName("actor")
        private String actor;
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("area")
        private String area;
        @Expose
        @SerializedName("remark")
        private String remark;
        @Expose
        @SerializedName("detail")
        private String detail;
        @Expose
        @SerializedName("release_time")
        private String release_time;
        @Expose
        @SerializedName("film_url")
        private String film_url;

        public String getId() {
            return movie_id;
        }

        public String getName() {
            return movie_name;
        }

        public String getType() {
            return type;
        }

        public String getArea() {
            return area;
        }

        public boolean getRemark() {
            return (remark != null) && (!"null".equals(remark));
        }

        public String getDetail() {
            return detail;
        }

        public String getUrl() {
            return film_url;
        }
    }
}
