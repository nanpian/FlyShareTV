package cn.fxdata.tv.bean;

import java.io.Serializable;
import java.util.List;

import cn.fxdata.tv.utils.DateTimeUtil;
import cn.fxdata.tv.utils.StringUtils;

import com.google.gson.annotations.Expose;

public class MovieCommentsDataReturn {

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
        private Integer topic_id;
        @Expose
        private String name;
        @Expose
        private String type;
        @Expose
        private String intro;
        @Expose
        private Integer is_follow;
        @Expose
        private List<MovieComments> list;
        @Expose
        private Integer total;

        /**
         * @return the total
         */
        public Integer getTotal() {
            return total;
        }

        /**
         * @param total
         *            the total to set
         */
        public void setTotal(Integer total) {
            this.total = total;
        }

        /**
         * @return the topic_id
         */
        public Integer getTopic_id() {
            return topic_id;
        }

        /**
         * @param topic_id
         *            the topic_id to set
         */
        public void setTopic_id(Integer topic_id) {
            this.topic_id = topic_id;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type
         *            the type to set
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return the intro
         */
        public String getIntro() {
            return intro;
        }

        /**
         * @param intro
         *            the intro to set
         */
        public void setIntro(String intro) {
            this.intro = intro;
        }

        /**
         * @return the list
         */
        public List<MovieComments> getList() {
            return list;
        }

        /**
         * @param list
         *            the list to set
         */
        public void setList(List<MovieComments> list) {
            this.list = list;
        }

        /**
         * @return the is_follow
         */
        public Integer getIs_follow() {
            return is_follow;
        }

        /**
         * @param is_follow
         *            the is_follow to set
         */
        public void setIs_follow(Integer is_follow) {
            this.is_follow = is_follow;
        }

    }

    public class MovieComments implements Serializable {
        @Expose
        private String id;
        @Expose
        private String topic_id;
        @Expose
        private String topic_name;
        @Expose
        private String topic_thumb;
        @Expose
        private Integer is_followed;
        @Expose
        private String author_id;
        @Expose
        private String author;
        @Expose
        private Integer user_follow;
        @Expose
        private Integer user_block;
        @Expose
        private String avatar;
        @Expose
        private String created;
        @Expose
        private String thumb;
        @Expose
        private String width;
        @Expose
        private String height;
        @Expose
        private String content;
        /** 是否显示更多文字 */
        private boolean contentShowMore = false;
        @Expose
        private String heart;
        @Expose
        private String forward;
        @Expose
        private String comment;
        @Expose
        private Integer is_zan;
        @Expose
        private Integer is_fav;

        private String created_interval;
        /**
         * 是否内容的行数大于最大显示行数
         */
        private String textLineTag = "";

        /**
         * @return the textLineTag
         */
        public String getTextLineTag() {
            return textLineTag;
        }

        /**
         * @param textLineTag
         *            the textLineTag to set
         */
        public void setTextLineTag(String textLineTag) {
            this.textLineTag = textLineTag;
        }

        /**
         * @return the content_fold
         */
        public boolean isContentShowMore() {
            return contentShowMore;
        }

        /**
         * @param content_fold
         *            the content_fold to set
         */
        public void setContentShowMore(boolean content_fold) {
            this.contentShowMore = content_fold;
        }

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id
         *            the id to set
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return the topic_id
         */
        public String getTopic_id() {
            return topic_id;
        }

        /**
         * @param topic_id
         *            the topic_id to set
         */
        public void setTopic_id(String topic_id) {
            this.topic_id = topic_id;
        }

        /**
         * @return the topic_name
         */
        public String getTopic_name() {
            return topic_name;
        }

        /**
         * @param topic_name
         *            the topic_name to set
         */
        public void setTopic_name(String topic_name) {
            this.topic_name = topic_name;
        }

        /**
         * @return the topic_thumb
         */
        public String getTopic_thumb() {
            return topic_thumb;
        }

        /**
         * @param topic_thumb
         *            the topic_thumb to set
         */
        public void setTopic_thumb(String topic_thumb) {
            this.topic_thumb = topic_thumb;
        }

        /**
         * @return the is_followed
         */
        public Integer getIs_followed() {
            return is_followed;
        }

        /**
         * @param is_followed
         *            the is_followed to set
         */
        public void setIs_followed(Integer is_followed) {
            this.is_followed = is_followed;
        }

        /**
         * @return the author_id
         */
        public String getAuthor_id() {
            return author_id;
        }

        /**
         * @param author_id
         *            the author_id to set
         */
        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        /**
         * @return the author
         */
        public String getAuthor() {
            return author;
        }

        /**
         * @param author
         *            the author to set
         */
        public void setAuthor(String author) {
            this.author = author;
        }

        /**
         * @return the avatar
         */
        public String getAvatar() {
            return avatar;
        }

        /**
         * @param avatar
         *            the avatar to set
         */
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        /**
         * @return the created
         */
        public String getCreated() {
            return created;
        }

        /**
         * @param created
         *            the created to set
         */
        public void setCreated(String created) {
            this.created = created;
        }

        /**
         * @return the thumb
         */
        public String getThumb() {
            return thumb;
        }

        /**
         * @param thumb
         *            the thumb to set
         */
        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        /**
         * @return the width
         */
        public String getWidth() {
            return width;
        }

        /**
         * @param width
         *            the width to set
         */
        public void setWidth(String width) {
            this.width = width;
        }

        /**
         * @return the height
         */
        public String getHeight() {
            return height;
        }

        /**
         * @param height
         *            the height to set
         */
        public void setHeight(String height) {
            this.height = height;
        }

        /**
         * @return the content
         */
        public String getContent() {
            return content;
        }

        /**
         * @param content
         *            the content to set
         */
        public void setContent(String content) {
            this.content = content;
        }

        /**
         * @return the heart
         */
        public String getHeart() {
            return heart;
        }

        /**
         * @param heart
         *            the heart to set
         */
        public void setHeart(String heart) {
            this.heart = heart;
        }

        /**
         * @return the forward
         */
        public String getForward() {
            return forward;
        }

        /**
         * @param forward
         *            the forward to set
         */
        public void setForward(String forward) {
            this.forward = forward;
        }

        /**
         * @return the comment
         */
        public String getComment() {
            return comment;
        }

        /**
         * @param comment
         *            the comment to set
         */
        public void setComment(String comment) {
            this.comment = comment;
        }

        /**
         * @return the is_zan
         */
        public Integer getIs_zan() {
            return is_zan;
        }

        /**
         * @param is_zan
         *            the is_zan to set
         */
        public void setIs_zan(Integer is_zan) {
            this.is_zan = is_zan;
        }

        /**
         * @return the is_fav
         */
        public Integer getIs_fav() {
            return is_fav;
        }

        /**
         * @param is_fav
         *            the is_fav to set
         */
        public void setIs_fav(Integer is_fav) {
            this.is_fav = is_fav;
        }

        /**
         * @return the user_follow
         */
        public Integer getUser_follow() {
            return user_follow;
        }

        /**
         * @param user_follow
         *            the user_follow to set
         */
        public void setUser_follow(Integer user_follow) {
            this.user_follow = user_follow;
        }

        /**
         * @return the user_block
         */
        public Integer getUser_block() {
            return user_block;
        }

        /**
         * @param user_block
         *            the user_block to set
         */
        public void setUser_block(Integer user_block) {
            this.user_block = user_block;
        }

        /**
         * @return the created_interval
         */
        public String getCreated_interval() {
            if (StringUtils.isNullOrEmpty(created_interval)) {
                created_interval = DateTimeUtil.getIntervalHours(created);
            }
            return created_interval;
        }

        /**
         * @param created_interval
         *            the created_interval to set
         */
        public void setCreated_interval(String created_interval) {
            this.created_interval = created_interval;
        }

    }
}
