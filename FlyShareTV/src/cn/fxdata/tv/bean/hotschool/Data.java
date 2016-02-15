package cn.fxdata.tv.bean.hotschool;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Data {

    @SerializedName("hot_school")
    @Expose
    private List<HotSchool_> hotSchool = new ArrayList<HotSchool_>();

    /**
     * 
     * @return The hotSchool
     */
    public List<HotSchool_> getHotSchool() {
        return hotSchool;
    }

    /**
     * 
     * @param hotSchool
     *            The hot_school
     */
    public void setHotSchool(List<HotSchool_> hotSchool) {
        this.hotSchool = hotSchool;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(hotSchool).toHashCode();
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
        return new EqualsBuilder().append(hotSchool, rhs.hotSchool).isEquals();
    }

}
