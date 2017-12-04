package com.piemicrosystems.hoodcop.object;

/**
 * Created by aangjnr on 15/11/2017.
 */

public class User {

    String id;
    String name;
    String imageUrl;
    String phoneNo;
    String email;
    int levelNo;
    String tagline;


    public User(String id, String name, String imageUrl, String phoneNo, String email, int levelNo, String tagLine) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.phoneNo = phoneNo;
        this.email = email;
        this.levelNo = levelNo;
        this.tagline = tagLine;

    }


    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }


}
