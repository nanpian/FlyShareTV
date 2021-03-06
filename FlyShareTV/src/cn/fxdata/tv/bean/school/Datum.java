package cn.fxdata.tv.bean.school;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("search_times")
    @Expose
    private String searchTimes;

    /**
     * 
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *            The id
     */
    public void setId(String id) {
        this.id = id;
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

    /**
     * 
     * @return The searchTimes
     */
    public String getSearchTimes() {
        return searchTimes;
    }

    /**
     * 
     * @param searchTimes
     *            The search_times
     */
    public void setSearchTimes(String searchTimes) {
        this.searchTimes = searchTimes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name)
                .append(searchTimes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Datum) == false) {
            return false;
        }
        Datum rhs = ((Datum) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name)
                .append(searchTimes, rhs.searchTimes).isEquals();
    }

}
