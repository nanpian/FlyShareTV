
package cn.fxdata.tv.bean.episode;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Data {

    @SerializedName("list")
    @Expose
    private java.util.List<cn.fxdata.tv.bean.episode.List> list = new ArrayList<cn.fxdata.tv.bean.episode.List>();
    @SerializedName("count_sets")
    @Expose
    private Integer countSets;

    /**
     * 
     * @return
     *     The list
     */
    public java.util.List<cn.fxdata.tv.bean.episode.List> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<cn.fxdata.tv.bean.episode.List> list) {
        this.list = list;
    }

    /**
     * 
     * @return
     *     The countSets
     */
    public Integer getCountSets() {
        return countSets;
    }

    /**
     * 
     * @param countSets
     *     The count_sets
     */
    public void setCountSets(Integer countSets) {
        this.countSets = countSets;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(list).append(countSets).toHashCode();
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
        return new EqualsBuilder().append(list, rhs.list).append(countSets, rhs.countSets).isEquals();
    }

}
