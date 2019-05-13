package udacity.project.com.bakingapp.database;

import java.io.Serializable;

public class Step implements Serializable {

    public Step() {

    }

    private String videoURL;

    private String description;

    private String id;

    private String shortDescription;

    private String thumbnailURL;

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public String toString() {
        return "Step [videoURL = " + videoURL + ", description = " + description + ", id = " + id + ", shortDescription = " + shortDescription + ", thumbnailURL = " + thumbnailURL + "]";
    }
}
