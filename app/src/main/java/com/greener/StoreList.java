package com.greener;

public class StoreList {
    private String imageUri;
    private String nameStr ;
    private String addressStr ;
    private String callStr;
    private String x;
    private String y;

    public StoreList() {

    }

    public StoreList(String addressStr, String callStr, String imageUri, String nameStr, String x, String y) {
        this.addressStr = addressStr;
        this.callStr = callStr;
        this.imageUri = imageUri;
        this.nameStr = nameStr;
        this.x = x;
        this.y = y;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getNameStr() {
        return nameStr;
    }

    public void setNameStr(String nameStr) {
        this.nameStr = nameStr;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public String getCallStr() {
        return callStr;
    }

    public void setCallStr(String callStr) {
        this.callStr = callStr;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getX() { return x; }

    public void setY(String y) {
        this.y = y;
    }

    public String getY() { return y; }

}
