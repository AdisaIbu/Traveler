package model;

import com.example.traveler.Satisfaction;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class City {

    private String name;
    private String memories;
    private List<String> imageUrls;
    private String userId;
    private String username;

    @ServerTimestamp
    private Date timestamp;
    private Satisfaction satisfaction;
    private String documentId;



    public City(){}

    public City(String name, String memories, List<String> imageUrls, String userId, String username, Date timestamp, Satisfaction satisfaction, String documentId) {
        this.name = name;
        this.memories = memories;
        this.imageUrls = imageUrls;
        this.userId = userId;
        this.username = username;
        this.timestamp = timestamp;
        this.satisfaction = satisfaction;
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemories() {
        return memories;
    }

    public void setMemories(String memories) {
        this.memories = memories;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public Satisfaction getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Satisfaction satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
