package com.greener;

public class StoreDetailList {
    private String imageUri;
    private int num;
    private Boolean save;

    public StoreDetailList() {

    }

    public StoreDetailList(String imageUri, int num, Boolean save) {
        this.imageUri = imageUri;
        this.num = num;
        this.save = save;
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

    public Boolean getSave() {
        return save;
    }

    public void setSave(Boolean save) {
        this.save = save;
    }
}
