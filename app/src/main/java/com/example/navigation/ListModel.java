package com.example.navigation;

public class ListModel {
    private int type;

    private String mMetaName;
    private String mMetaQuantity;

    private String mRecipeName;

    public static final int METAR_TYPE = 1;
    public static final int RECIPE_TYPE = 2;

    public String getmRecipeName() {
        return mRecipeName;
    }

    public void setmRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    public String getmMetaName() {
        return mMetaName;
    }

    public void setmMetaName(String mMetaName) {
        this.mMetaName = mMetaName;
    }

    public String getmMetaQuantity() {
        return mMetaQuantity;
    }

    public void setmMetaQuantity(String mMetaQuantity) {
        this.mMetaQuantity = mMetaQuantity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ListModel(int type, String mMetaName, String mMetaQuantity, boolean t) {
        //this.title = title;
        this.type = type;
        this.mMetaName = mMetaName;
        this.mMetaQuantity = mMetaQuantity;
    }

    public ListModel(int type, String mRecipeName){
        this.type = type;
        this.mRecipeName = mRecipeName;
    }

}
