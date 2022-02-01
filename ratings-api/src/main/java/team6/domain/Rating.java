package team6.domain;

import java.util.Calendar;

public class Rating {

    private String id;

    private String userId;

    private String productId;
    
    private String locationName;

    private int rating;

    private String userNotes;

    private Calendar timestamp;

    public Rating(String id, String userId, String productId, String locationName, int rating, String userNotes,
            Calendar timestamp) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.locationName = locationName;
        this.rating = rating;
        this.userNotes = userNotes;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserNotes() {
        return userNotes;
    }

    public void setUserNotes(String userNotes) {
        this.userNotes = userNotes;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

}
