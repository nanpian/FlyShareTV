package cn.fxdata.tv.bean.hotplaybroadcast;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Data {

    @SerializedName("advertisement")
    @Expose
    private List<Advertisement> advertisement = new ArrayList<Advertisement>();
    @SerializedName("broadcast")
    @Expose
    private List<Broadcast> broadcast = new ArrayList<Broadcast>();

    /**
     * 
     * @return The advertisement
     */
    public List<Advertisement> getAdvertisement() {
        return advertisement;
    }

    /**
     * 
     * @param advertisement
     *            The advertisement
     */
    public void setAdvertisement(List<Advertisement> advertisement) {
        this.advertisement = advertisement;
    }

    /**
     * 
     * @return The broadcast
     */
    public List<Broadcast> getBroadcast() {
        return broadcast;
    }

    /**
     * 
     * @param broadcast
     *            The broadcast
     */
    public void setBroadcast(List<Broadcast> broadcast) {
        this.broadcast = broadcast;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(advertisement).append(broadcast)
                .toHashCode();
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
        return new EqualsBuilder().append(advertisement, rhs.advertisement)
                .append(broadcast, rhs.broadcast).isEquals();
    }

}
