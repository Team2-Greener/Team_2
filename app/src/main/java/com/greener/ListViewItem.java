package com.greener;

import android.net.Uri;

public class ListViewItem {
    private String imageUri;
    private String titleStr ;
    private String ContentStr ;

    public void setImage(String uri) { imageUri = uri ; }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setContent(String text) {
        ContentStr = text ;
    }

    public String getImage() { return this.imageUri; }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getContent() {
        return this.ContentStr ;
    }

    public ListViewItem(String uri, String titleStr, String ContentStr) {
        this.imageUri = uri;
        this.titleStr = titleStr;
        this.ContentStr = ContentStr;
    }
}
