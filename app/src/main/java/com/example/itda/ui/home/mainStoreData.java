package com.example.itda.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class mainStoreData implements Parcelable {
    private int StoreId;
    private String StoreName;
    private String StoreAddress;
    private String StoreParking;
    private double StoreLatitude;
    private double StoreLongitude;
    private String StoreNumber;
    private String StoreInfo;
    private int StoreCategoryId;
    private String StoreThumbnailPath;
    private double StoreScore;
    private String StoreWorkingTime;

    public mainStoreData(int storeId, String storeName, String storeAddress, String storeParking, double storeLatitude, double storeLongitude, String storeNumber, String storeInfo, int storeCategoryId, String storeThumbnailPath, double storeScore, String storeWorkingTime) {
        StoreId = storeId;
        StoreName = storeName;
        StoreAddress = storeAddress;
        StoreParking = storeParking;
        StoreLatitude = storeLatitude;
        StoreLongitude = storeLongitude;
        StoreNumber = storeNumber;
        StoreInfo = storeInfo;
        StoreCategoryId = storeCategoryId;
        StoreThumbnailPath = storeThumbnailPath;
        StoreScore = storeScore;
        StoreWorkingTime = storeWorkingTime;
    }

    protected mainStoreData(Parcel in) {
        StoreId = in.readInt();
        StoreName = in.readString();
        StoreAddress = in.readString();
        StoreParking = in.readString();
        StoreLatitude = in.readDouble();
        StoreLongitude = in.readDouble();
        StoreNumber = in.readString();
        StoreInfo = in.readString();
        StoreCategoryId = in.readInt();
        StoreThumbnailPath = in.readString();
        StoreScore = in.readDouble();
        StoreWorkingTime = in.readString();
    }

    // ===========Activity간 데이터를 한꺼번에 전달받기 위해 사용한 Interface==============
    public static final Creator<mainStoreData> CREATOR = new Creator<mainStoreData>() {
        @Override
        public mainStoreData createFromParcel(Parcel in) {
            return new mainStoreData(in);
        }

        @Override
        public mainStoreData[] newArray(int size) {
            return new mainStoreData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(StoreId);
        dest.writeString(StoreName);
        dest.writeString(StoreAddress);
        dest.writeString(StoreParking);
        dest.writeDouble(StoreLatitude);
        dest.writeDouble(StoreLongitude);
        dest.writeString(StoreNumber);
        dest.writeString(StoreInfo);
        dest.writeInt(StoreCategoryId);
        dest.writeString(StoreThumbnailPath);
        dest.writeDouble(StoreScore);
        dest.writeString(StoreWorkingTime);

    }
    //=======================================================================

    public int getStoreId() {
        return StoreId;
    }

    public void setStoreId(int storeId) {
        StoreId = storeId;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getStoreAddress() {
        return StoreAddress;
    }

    public void setStoreAddress(String storeAddress) {
        StoreAddress = storeAddress;
    }

    public String getStoreParking() {
        return StoreParking;
    }

    public void setStoreParking(String storeParking) {
        StoreParking = storeParking;
    }

    public double getStoreLatitude() {
        return StoreLatitude;
    }

    public void setStoreLatitude(Double storeLatitude) {
        StoreLatitude = storeLatitude;
    }

    public double getStoreLongitude() {
        return StoreLongitude;
    }

    public void setStoreLongitude(Double storeLongitude) {
        StoreLongitude = storeLongitude;
    }

    public String getStoreNumber() {
        return StoreNumber;
    }

    public void setStoreNumber(String storeNumber) {
        StoreNumber = storeNumber;
    }

    public String getStoreInfo() {
        return StoreInfo;
    }

    public void setStoreInfo(String storeInfo) {
        StoreInfo = storeInfo;
    }

    public int getStoreCategoryId() {
        return StoreCategoryId;
    }

    public void setStoreCategoryId(int storeCategoryId) {
        StoreCategoryId = storeCategoryId;
    }

    public String getStoreThumbnailPath() {
        return StoreThumbnailPath;
    }

    public void setStoreThumbnailPath(String storeThumbnailPath) {
        StoreThumbnailPath = storeThumbnailPath;
    }

    public double getStoreScore() {
        return StoreScore;
    }

    public void setStoreScore(Double storeScore) {
        StoreScore = storeScore;
    }

    public String getStoreWorkingTime() {
        return StoreWorkingTime;
    }

    public void setStoreWorkingTime(String storeWorkingTime) {
        StoreWorkingTime = storeWorkingTime;
    }
}
