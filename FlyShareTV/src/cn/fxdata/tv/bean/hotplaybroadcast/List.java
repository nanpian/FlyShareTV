package cn.fxdata.tv.bean.hotplaybroadcast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class List {

    @SerializedName("movie_id")
    @Expose
    private String movieId;
    @SerializedName("movie_name")
    @Expose
    private String movieName;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("class_id")
    @Expose
    private String classId;
    @SerializedName("class")
    @Expose
    private String _class;

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
     * @return The movieName
     */
    public String getMovieName() {
        return movieName;
    }

    /**
     * 
     * @param movieName
     *            The movie_name
     */
    public void setMovieName(String movieName) {
        this.movieName = movieName;
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

    /**
     * 
     * @return The classId
     */
    public String getClassId() {
        return classId;
    }

    /**
     * 
     * @param classId
     *            The class_id
     */
    public void setClassId(String classId) {
        this.classId = classId;
    }

    /**
     * 
     * @return The _class
     */
    public String getClass_() {
        return _class;
    }

    /**
     * 
     * @param _class
     *            The class
     */
    public void setClass_(String _class) {
        this._class = _class;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(movieId).append(movieName)
                .append(thumb).append(classId).append(_class).toHashCode();
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
        return new EqualsBuilder().append(movieId, rhs.movieId)
                .append(movieName, rhs.movieName).append(thumb, rhs.thumb)
                .append(classId, rhs.classId).append(_class, rhs._class)
                .isEquals();
    }

}