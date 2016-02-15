package cn.fxdata.tv.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class HotCommentReturn {
    /** 返回 码 */
    @Expose
    private Integer error_code;
    /** 错误信息 */
    @Expose
    private String error_msg;
    @Expose
    private TopicCommentsData data;

    /**
     * @return the error_code
     */
    public Integer getError_code() {
        return error_code;
    }

    /**
     * @param error_code
     *            the error_code to set
     */
    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    /**
     * @return the error_msg
     */
    public String getError_msg() {
        return error_msg;
    }

    /**
     * @param error_msg
     *            the error_msg to set
     */
    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    /**
     * @return the data
     */
    public TopicCommentsData getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(TopicCommentsData data) {
        this.data = data;
    }

    public class TopicCommentsData implements Serializable {
        @Expose
        private List<MovieComments> hot_comment;
        @Expose
        private Integer total_page;
        @Expose
        private List<MovieComments> update_comment;

        public List<MovieComments> getHot_comment() {
            return hot_comment;
        }

        public void setHot_comment(List<MovieComments> hot_comment) {
            this.hot_comment = hot_comment;
        }

        public Integer getTotal_page() {
            return total_page;
        }

        public void setTotal_page(Integer total_page) {
            this.total_page = total_page;
        }

        public List<MovieComments> getUpdate_comment() {
            return update_comment;
        }

        public void setUpdate_comment(List<MovieComments> update_comment) {
            this.update_comment = update_comment;
        }
    }

    public class MovieComments {
        @Expose
        public String comment_id;
        @Expose
        public String user_id;
        @Expose
        public String content;
        @Expose
        public String praise_times;
        @Expose
        public String created;
        @Expose
        public String username;
        @Expose
        public String avatar;

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPraise_times() {
            return praise_times;
        }

        public void setPraise_times(String praise_times) {
            this.praise_times = praise_times;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
