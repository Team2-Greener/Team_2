package com.greener;

public class StoreDetailList {
    private String imageUri;
    private int num;

    public StoreDetailList() {

    }

    public StoreDetailList(String imageUri, int num) {
        this.imageUri = imageUri;
        this.num = num;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
