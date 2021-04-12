package ca.bcit.asahiryoko;

public class PostData {

    private String description;
    private String id;
    private String imageUrl;
    private String placeName;

    public PostData() {
    }

    public PostData(String description, String id, String imageUrl, String placeName) {
        this.description = description;
        this.id = id;
        this.imageUrl = imageUrl;
        this.placeName = placeName;

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

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlaceName() {
        return placeName;
    }
    public void setPlaceName(String placeName) {this.placeName = placeName;}

}
