package cn.fxdata.tv.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * Home new movies json return entity.
 * 
 * @author dewei
 * 
 */
public class NewMoviesReturn {

    @Expose
    @SerializedName("error_code")
    private Integer error_code;
    @Expose
    @SerializedName("error_msg")
    private String error_msg;
    @Expose
    @SerializedName("data")
    private TwoMoviesLists data;

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

    public TwoMoviesLists getData() {
        return data;
    }

    public void setData(TwoMoviesLists data) {
        this.data = data;
    }

    public static class TwoMoviesLists {
        @Expose
        @SerializedName("list_now")
        private List<Movies> list_now;
        @Expose
        @SerializedName("list_preview")
        private List<Movies> list_pre;

        @Expose
        @SerializedName("preview_end")
        private String preview_end;

        public String getPreview_end() {
            return preview_end;
        }

        public void setPreview_end(String preview_end) {
            this.preview_end = preview_end;
        }

        public List<Movies> getList_now() {
            return list_now;
        }

        public void setList_now(List<Movies> list_now) {
            this.list_now = list_now;
        }

        public List<Movies> getList_pre() {
            return list_pre;
        }

        public void setList_pre(List<Movies> list_pre) {
            this.list_pre = list_pre;
        }
    }

    public static class Movies implements Serializable {
        @Expose
        private String movie_id;
        @Expose
        private String thumb;
        @Expose
        private String type;
        @Expose
        private String name;
        @Expose
        private String created;

        public String getMovie_id() {
            return movie_id;
        }

        public void setMovie_id(String movie_id) {
            this.movie_id = movie_id;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private int resId;

        public Movies(String id, int resId) {
            this.movie_id = id;
            this.resId = resId;
        }

        private final static String MOVIE_TIME_FLAG = "movie_time_flag";

        public static Movies getMovieTime(int resId) {
            return new Movies(MOVIE_TIME_FLAG, resId);
        }

        public boolean isMovieTime() {
            return this.movie_id.equals(MOVIE_TIME_FLAG);
        }

        public int getMovieTimeResId() {
            return this.resId;
        }
    }

}
