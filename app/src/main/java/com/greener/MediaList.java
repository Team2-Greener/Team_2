package com.greener;

public class MediaList {
    private String imageTitle;
    private String imageUri;
    private String titleStr ;

    public MediaList() {

    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitleStr() { return titleStr; }

    public void setTitleStr(String titleStr) { this.titleStr = titleStr; }
}
