
package cn.fxdata.tv.bean.episode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class List {

    @SerializedName("film_id")
    @Expose
    private String filmId;
    @SerializedName("film_name")
    @Expose
    private String filmName;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("sets")
    @Expose
    private String sets;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("release_time")
    @Expose
    private String releaseTime;

    /**
     * 
     * @return
     *     The filmId
     */
    public String getFilmId() {
        return filmId;
    }

    /**
     * 
     * @param filmId
     *     The film_id
     */
    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    /**
     * 
     * @return
     *     The filmName
     */
    public String getFilmName() {
        return filmName;
    }

    /**
     * 
     * @param filmName
     *     The film_name
     */
    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    /**
     * 
     * @return
     *     The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * 
     * @param thumb
     *     The thumb
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    /**
     * 
     * @return
     *     The sets
     */
    public String getSets() {
        return sets;
    }

    /**
     * 
     * @param sets
     *     The sets
     */
    public void setSets(String sets) {
        this.sets = sets;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The link
     */
    public String getLink() {
        return link;
    }

    /**
     * 
     * @param link
     *     The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * 
     * @return
     *     The duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The releaseTime
     */
    public String getReleaseTime() {
        return releaseTime;
    }

    /**
     * 
     * @param releaseTime
     *     The release_time
     */
    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(filmId).append(filmName).append(thumb).append(sets).append(type).append(link).append(duration).append(releaseTime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof List) == false) {
            return false;
        }
        List rhs = ((List) other);
        return new EqualsBuilder().append(filmId, rhs.filmId).append(filmName, rhs.filmName).append(thumb, rhs.thumb).append(sets, rhs.sets).append(type, rhs.type).append(link, rhs.link).append(duration, rhs.duration).append(releaseTime, rhs.releaseTime).isEquals();
    }

}
