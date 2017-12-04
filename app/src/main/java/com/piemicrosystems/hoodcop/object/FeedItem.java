package com.piemicrosystems.hoodcop.object;

import java.util.HashMap;

/**
 * Created by aangjnr on 15/11/2017.
 */

public class FeedItem {


    String id;
    String datePosted; //Long value converted to String for easy usability
    String status;
    User postee;
    Boolean withImage;
    String imageUrl;
    //List<PeopleWhoStarred> stars;
    HashMap<String, Comment> comments;

    HashMap<String, PeopleWhoStarred> stars;


    public FeedItem() {
    }

    public FeedItem(String id, String datePosted, String status, User postee, Boolean withImage, String imageUrl, HashMap<String, PeopleWhoStarred> stars, HashMap<String, Comment> comments) {

        this.id = id;
        this.datePosted = datePosted;
        this.status = status;
        this.postee = postee;
        this.withImage = withImage;
        this.imageUrl = imageUrl;
        this.stars = stars;
        this.comments = comments;
    }


    public FeedItem(String id, String datePosted, String status, User postee, Boolean withImage, HashMap<String, PeopleWhoStarred> stars, HashMap<String, Comment> comments) {

        this.id = id;
        this.datePosted = datePosted;
        this.status = status;
        this.postee = postee;
        this.withImage = withImage;
        this.stars = stars;
        this.comments = comments;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getWithImage() {
        return withImage;
    }

    public void setWithImage(Boolean withImage) {
        this.withImage = withImage;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getPostee() {
        return postee;
    }

    public void setPostee(User postee) {
        this.postee = postee;
    }

    public HashMap<String, Comment> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, Comment> comments) {
        this.comments = comments;
    }

    public HashMap<String, PeopleWhoStarred> getStars() {
        return stars;
    }

    public void setStars(HashMap<String, PeopleWhoStarred> stars) {
        this.stars = stars;
    }
}
