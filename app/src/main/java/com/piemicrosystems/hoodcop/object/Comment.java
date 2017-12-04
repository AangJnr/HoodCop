package com.piemicrosystems.hoodcop.object;

import java.util.HashMap;

/**
 * Created by aangjnr on 15/11/2017.
 */

public class Comment {

    String id;
    String postId;
    User user;
    String comment;
    String datePosted; //Long value converted to String for easy usability

    HashMap<String, PeopleWhoStarred> stars;


    public Comment(String postId, String id, User user, String comment, String datePosted) {

        postId = postId;
        this.id = id;
        this.user = user;
        this.comment = comment;

        this.datePosted = datePosted;

    }


    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HashMap<String, PeopleWhoStarred> getStars() {
        return stars;
    }

    public void setStars(HashMap<String, PeopleWhoStarred> stars) {
        this.stars = stars;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
