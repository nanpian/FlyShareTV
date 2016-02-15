package cn.fxdata.tv.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Administrator on 2015/7/24 0024.
 */
public class HotSearchResult {

    /**
     * data :
     * {"hot_movie":[{"movie_name":"跑男","movie_id":"5"},{"movie_name":"火影"
     * ,"movie_id"
     * :"4"},{"movie_name":"夜魔侠","movie_id":"15"},{"movie_name":"钢铁侠",
     * "movie_id":
     * "14"},{"movie_name":"神奇四侠","movie_id":"13"},{"movie_name":"战狼",
     * "movie_id":
     * "12"},{"movie_name":"杀破狼","movie_id":"11"},{"movie_name":"浪漫满屋"
     * ,"movie_id":"10"},{"movie_name":"变形金刚","movie_id":"9"}]} error_code : 0
     * error_msg :
     */
    @Expose
    private DataEntity data;
    @Expose
    private int error_code;
    @Expose
    private String error_msg;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public DataEntity getData() {
        return data;
    }

    public int getError_code() {
        return error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public static class DataEntity {
        /**
         * hot_movie :
         * [{"movie_name":"跑男","movie_id":"5"},{"movie_name":"火影","movie_id"
         * :"4"}
         * ,{"movie_name":"夜魔侠","movie_id":"15"},{"movie_name":"钢铁侠","movie_id"
         * :"14"},{"movie_name":"神奇四侠","movie_id":"13"},{"movie_name":"战狼",
         * "movie_id"
         * :"12"},{"movie_name":"杀破狼","movie_id":"11"},{"movie_name":"浪漫满屋"
         * ,"movie_id":"10"},{"movie_name":"变形金刚","movie_id":"9"}]
         */
        @Expose
        private List<HotMovieEntity> hot_movie;

        public void setHot_movie(List<HotMovieEntity> hot_movie) {
            this.hot_movie = hot_movie;
        }

        public List<HotMovieEntity> getHot_movie() {
            return hot_movie;
        }

        public static class HotMovieEntity {
            /**
             * movie_name : 跑男 movie_id : 5
             */
            @Expose
            private String movie_name;
            @Expose
            private String movie_id;

            public void setMovie_name(String movie_name) {
                this.movie_name = movie_name;
            }

            public void setMovie_id(String movie_id) {
                this.movie_id = movie_id;
            }

            public String getMovie_name() {
                return movie_name;
            }

            public String getMovie_id() {
                return movie_id;
            }
        }
    }
}
