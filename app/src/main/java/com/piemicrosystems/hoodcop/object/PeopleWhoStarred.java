package com.piemicrosystems.hoodcop.object;

/**
 * Created by aangjnr on 15/11/2017.
 */

public class PeopleWhoStarred {

    User user;


    public PeopleWhoStarred(User user) {

        this.user = user;

    }


    public PeopleWhoStarred() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
