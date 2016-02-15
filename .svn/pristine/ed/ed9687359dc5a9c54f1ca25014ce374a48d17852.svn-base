
package cn.fxdata.tv.bean.comment;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Data {

    @SerializedName("hot_comment")
    @Expose
    private List<HotComment> hotComment = new ArrayList<HotComment>();
    @SerializedName("total_page")
    @Expose
    private Integer totalPage;
    @SerializedName("update_comment")
    @Expose
    private List<UpdateComment> updateComment = new ArrayList<UpdateComment>();

    /**
     * 
     * @return
     *     The hotComment
     */
    public List<HotComment> getHotComment() {
        return hotComment;
    }

    /**
     * 
     * @param hotComment
     *     The hot_comment
     */
    public void setHotComment(List<HotComment> hotComment) {
        this.hotComment = hotComment;
    }

    /**
     * 
     * @return
     *     The totalPage
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * 
     * @param totalPage
     *     The total_page
     */
    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 
     * @return
     *     The updateComment
     */
    public List<UpdateComment> getUpdateComment() {
        return updateComment;
    }

    /**
     * 
     * @param updateComment
     *     The update_comment
     */
    public void setUpdateComment(List<UpdateComment> updateComment) {
        this.updateComment = updateComment;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(hotComment).append(totalPage).append(updateComment).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Data) == false) {
            return false;
        }
        Data rhs = ((Data) other);
        return new EqualsBuilder().append(hotComment, rhs.hotComment).append(totalPage, rhs.totalPage).append(updateComment, rhs.updateComment).isEquals();
    }

}
