package com.Ads;

import com.appnext.appnextsdk.API.AppnextAd;

import java.io.Serializable;

/**
 * Created by krishan on 8/6/2014.
 */
public class Myad implements Serializable {

    private java.lang.String adDesc;
    private java.lang.String adTitle;
    private java.lang.String imageURL;
    private java.lang.String appURL;
    private java.lang.String bannerID;
    private java.lang.String campaignID;
    private java.lang.String cb;
    private java.lang.String zoneID;
    private java.lang.String adPackage;
    private java.lang.String epub;
    private java.lang.String bpub;
    private int adID;
    private java.lang.String revenueType;
    private java.lang.String revenueRate;
    private java.lang.String categories;


    public String getAdDesc() {
        return adDesc;
    }

    public void setAdDesc(String adDesc) {
        this.adDesc = adDesc;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAppURL() {
        return appURL;
    }

    public void setAppURL(String appURL) {
        this.appURL = appURL;
    }

    public String getBannerID() {
        return bannerID;
    }

    public void setBannerID(String bannerID) {
        this.bannerID = bannerID;
    }

    public String getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(String campaignID) {
        this.campaignID = campaignID;
    }

    public String getCb() {
        return cb;
    }

    public void setCb(String cb) {
        this.cb = cb;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getAdPackage() {
        return adPackage;
    }

    public void setAdPackage(String adPackage) {
        this.adPackage = adPackage;
    }

    public String getEpub() {
        return epub;
    }

    public void setEpub(String epub) {
        this.epub = epub;
    }

    public String getBpub() {
        return bpub;
    }

    public void setBpub(String bpub) {
        this.bpub = bpub;
    }

    public int getAdID() {
        return adID;
    }

    public void setAdID(int adID) {
        this.adID = adID;
    }

    public String getRevenueType() {
        return revenueType;
    }

    public void setRevenueType(String revenueType) {
        this.revenueType = revenueType;
    }

    public String getRevenueRate() {
        return revenueRate;
    }

    public void setRevenueRate(String revenueRate) {
        this.revenueRate = revenueRate;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
