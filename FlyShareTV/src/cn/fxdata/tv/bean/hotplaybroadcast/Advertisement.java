package cn.fxdata.tv.bean.hotplaybroadcast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Advertisement {

    @SerializedName("advert_id")
    @Expose
    private String advertId;
    @SerializedName("movie_id")
    @Expose
    private String movieId;
    @SerializedName("thumb")
    @Expose
    private String thumb;

    /**
     * 
     * @return The advertId
     */
    public String getAdvertId() {
        return advertId;
    }

    /**
     * 
     * @param advertId
     *            The advert_id
     */
    public void setAdvertId(String advertId) {
        this.advertId = advertId;
    }

    /**
     * 
     * @return The movieId
     */
    public String getMovieId() {
        return movieId;
    }

    /**
     * 
     * @param movieId
     *            The movie_id
     */
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    /**
     * 
     * @return The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * 
     * @param thumb
     *            The thumb
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(advertId).append(movieId)
                .append(thumb).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Advertisement) == false) {
            return false;
        }
        Advertisement rhs = ((Advertisement) other);
        return new EqualsBuilder().append(advertId, rhs.advertId)
                .append(movieId, rhs.movieId).append(thumb, rhs.thumb)
                .isEquals();
    }

}
