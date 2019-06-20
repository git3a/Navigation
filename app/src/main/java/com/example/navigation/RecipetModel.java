package com.example.navigation;

public class RecipetModel {
    //private String title;
    private int type;

    private String mStepIndex;
    private String mStepInner;

    private String mMetaName;
    private String mMetaQuantity;

    private String mRecipeImgurl;
    private String mRecipeName;
    private String mMetaTitle;

    public String getmRecipeImgurl() {
        return mRecipeImgurl;
    }

    public void setmRecipeImgurl(String mRecipeImgurl) {
        this.mRecipeImgurl = mRecipeImgurl;
    }

    public String getmRecipeName() {
        return mRecipeName;
    }

    public void setmRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    public String getmMetaTitle() {
        return mMetaTitle;
    }

    public void setmMetaTitle(String mMetaTitle) {
        this.mMetaTitle = mMetaTitle;
    }

    public static final int METAR_TYPE = 1;
    public static final int STEP_TYPE = 2;
    public static final int IMGNAME_TYPE = 3;

    public RecipetModel(int type, String mStepIndex, String mStepInner) {
        //this.title = title;
        this.type = type;
        this.mStepIndex = mStepIndex;
        this.mStepInner = mStepInner;
    }

    public RecipetModel(int type, String mMetaName, String mMetaQuantity,boolean t) {
        //this.title = title;
        this.type = type;
        this.mMetaName = mMetaName;
        this.mMetaQuantity = mMetaQuantity;
    }

    public RecipetModel(int type,String mRecipeImgurl,String mRecipeName,String mMetaTitle){

        this.type = type;
        this.mRecipeImgurl = mRecipeImgurl;
        this.mRecipeName = mRecipeName;
        this.mMetaTitle = mMetaTitle;
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

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getmStepIndex() {
        return mStepIndex;
    }

    public void setmStepIndex(String mStepIndex) {
        this.mStepIndex = mStepIndex;
    }

    public String getmStepInner() {
        return mStepInner;
    }

    public void setmStepInner(String mStepInner) {
        this.mStepInner = mStepInner;
    }
}
