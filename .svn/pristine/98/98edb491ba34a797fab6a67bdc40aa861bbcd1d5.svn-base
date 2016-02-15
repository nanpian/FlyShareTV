
package cn.fxdata.tv.bean.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class HotComment {

    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("praise_times")
    @Expose
    private String praiseTimes;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    /**
     * 
     * @return
     *     The commentId
     */
    public String getCommentId() {
        return commentId;
    }

    /**
     * 
     * @param commentId
     *     The comment_id
     */
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The content
     */
    public String getContent() {
        return content;
    }

    /**
     * 
     * @param content
     *     The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 
     * @return
     *     The praiseTimes
     */
    public String getPraiseTimes() {
        return praiseTimes;
    }

    /**
     * 
     * @param praiseTimes
     *     The praise_times
     */
    public void setPraiseTimes(String praiseTimes) {
        this.praiseTimes = praiseTimes;
    }

    /**
     * 
     * @return
     *     The created
     */
    public String getCreated() {
        return created;
    }

    /**
     * 
     * @param created
     *     The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * 
     * @return
     *     The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     *     The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * @return
     *     The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 
     * @param avatar
     *     The avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(commentId).append(userId).append(content).append(praiseTimes).append(created).append(username).append(avatar).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HotComment) == false) {
            return false;
        }
        HotComment rhs = ((HotComment) other);
        return new EqualsBuilder().append(commentId, rhs.commentId).append(userId, rhs.userId).append(content, rhs.content).append(praiseTimes, rhs.praiseTimes).append(created, rhs.created).append(username, rhs.username).append(avatar, rhs.avatar).isEquals();
    }

}
