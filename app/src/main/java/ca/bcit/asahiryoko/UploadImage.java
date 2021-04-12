package ca.bcit.asahiryoko;
/***
 * This is a class for getting the data from firebase.
 *
 * Author: Brennen Chiu
 * Date: April 10, 2021
 * Version: 1.0
 */
public class UploadImage {
    private String id;
    private String placeName;
    private String imageUrl;
    private String description;

    public UploadImage() {
        // empty constructor needed
    }

    public UploadImage(String uid, String name, String imageURL, String uDescription) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        if (uDescription.trim().equals("")){
            uDescription = "";
        }
        id = uid;
        placeName = name;
        imageUrl = imageURL;
        description = uDescription;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }
    public void setPlaceName(String name) {
        placeName = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
