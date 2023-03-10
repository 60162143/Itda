package com.example.itda.ui.home;

public class mainCategoryData {
    private int categoryId;
    private String categoryNm;
    private String imagePath;
    private int imageId;

    public mainCategoryData(int categoryId, String categoryNm, String imagePath, int imageId) {
        this.categoryId = categoryId;
        this.categoryNm = categoryNm;
        this.imagePath = imagePath;
        this.imageId = imageId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryNm() {
        return categoryNm;
    }

    public void setCategoryNm(String categoryNm) {
        this.categoryNm = categoryNm;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
