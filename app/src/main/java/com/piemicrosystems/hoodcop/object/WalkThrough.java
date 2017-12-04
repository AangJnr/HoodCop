package com.piemicrosystems.hoodcop.object;

/**
 * Created by aangjnr on 04/11/2017.
 */

public class WalkThrough {

    String headerText;
    String subText;
    int image;


    public WalkThrough(String headerText, String subText, int image) {

        this.headerText = headerText;
        this.subText = subText;
        this.image = image;
    }


    public int getImage() {
        return image;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getSubText() {
        return subText;
    }
}

