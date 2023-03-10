package com.example.itda.ui.map;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.itda.ui.home.mainStoreData;

public class MapStoreData/* implements Parcelable*/ {
    private Integer mapStoreId;
    private String mapStoreName;
    private String mapStoreIamagePath;
    private Float mapStoreScore;
    private Double mapStoreLatitude;
    private Double mapStoreLongitude;
    private Float mapStoreDistance;
    private String mapStoreInfo;
    private String mapStoreHashTag;

    public MapStoreData(Integer mapStoreId, String mapStoreName, String mapStoreIamagePath, Float mapStoreScore, Double mapStoreLatitude, Double mapStoreLongitude,Float mapStoreDistance , String mapStoreInfo, String mapStoreHashTag) {
        this.mapStoreId = mapStoreId;
        this.mapStoreName = mapStoreName;
        this.mapStoreIamagePath = mapStoreIamagePath;
        this.mapStoreScore = mapStoreScore;
        this.mapStoreLatitude = mapStoreLatitude;
        this.mapStoreLongitude = mapStoreLongitude;
        this.mapStoreDistance = mapStoreDistance;
        this.mapStoreInfo = mapStoreInfo;
        this.mapStoreHashTag = mapStoreHashTag;
    }

    public Integer getMapStoreId() {
        return mapStoreId;
    }

    public void setMapStoreId(Integer mapStoreId) {
        this.mapStoreId = mapStoreId;
    }

    public String getMapStoreName() {
        return mapStoreName;
    }

    public void setMapStoreName(String mapStoreName) {
        this.mapStoreName = mapStoreName;
    }

    public String getMapStoreIamagePath() {
        return mapStoreIamagePath;
    }

    public void setMapStoreIamagePath(String mapStoreIamagePath) {
        this.mapStoreIamagePath = mapStoreIamagePath;
    }

    public Float getMapStoreScore() {
        return mapStoreScore;
    }

    public void setMapStoreScore(Float mapStoreScore) {
        this.mapStoreScore = mapStoreScore;
    }

    public Double getMapStoreLatitude() {
        return mapStoreLatitude;
    }

    public void setMapStoreLatitude(Double mapStoreLatitude) {
        this.mapStoreLatitude = mapStoreLatitude;
    }

    public Double getMapStoreLongitude() {
        return mapStoreLongitude;
    }

    public void setMapStoreLongitude(Double mapStoreLongitude) {
        this.mapStoreLongitude = mapStoreLongitude;
    }

    public Float getMapStoreDistance() {
        return mapStoreDistance;
    }

    public void setMapStoreDistance(Float mapStoreDistance) {
        this.mapStoreDistance = mapStoreDistance;
    }

    public String getMapStoreInfo() {
        return mapStoreInfo;
    }

    public void setMapStoreInfo(String mapStoreInfo) {
        this.mapStoreInfo = mapStoreInfo;
    }

    public String getMapStoreHashTag() {
        return mapStoreHashTag;
    }

    public void setMapStoreHashTag(String mapStoreHashTag) {
        this.mapStoreHashTag = mapStoreHashTag;
    }
}
