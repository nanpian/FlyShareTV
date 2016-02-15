package cn.fxdata.tv.bean.hotschool;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class HotSchool_ {

    @SerializedName("school_id")
    @Expose
    private String schoolId;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * 
     * @return The schoolId
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * 
     * @param schoolId
     *            The school_id
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    /**
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *            The name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(schoolId).append(name).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HotSchool_) == false) {
            return false;
        }
        HotSchool_ rhs = ((HotSchool_) other);
        return new EqualsBuilder().append(schoolId, rhs.schoolId)
                .append(name, rhs.name).isEquals();
    }

}
