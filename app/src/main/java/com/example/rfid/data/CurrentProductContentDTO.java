package com.example.rfid.data;

import com.google.gson.annotations.SerializedName;

public class CurrentProductContentDTO {

    @SerializedName("product_idx")
    private int product_idx;

    @SerializedName("product_name")
    private String product_name;

    @SerializedName("product_status")
    private int product_status;

    @SerializedName("provisionInfo_platoon")
    private String provisionInfo_platoon;

    @SerializedName("provisionInfo_user_name")
    private String provisionInfo_user_name;

    @SerializedName("special")
    private String special;

    @SerializedName("provisionInfo_createtime")
    private String provisionInfo_createtime;

    @SerializedName("product_taginfo")
    private String product_taginfo;

    public int getProduct_idx() {
        return product_idx;
    }

    public void setProduct_idx(int product_idx) {
        this.product_idx = product_idx;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_status() {
        return product_status;
    }

    public void setProduct_status(int product_status) {
        this.product_status = product_status;
    }

    public String getProvisionInfo_platoon() {
        return provisionInfo_platoon;
    }

    public void setProvisionInfo_platoon(String provisionInfo_platoon) {
        this.provisionInfo_platoon = provisionInfo_platoon;
    }

    public String getProvisionInfo_user_name() {
        return provisionInfo_user_name;
    }

    public void setProvisionInfo_user_name(String provisionInfo_user_name) {
        this.provisionInfo_user_name = provisionInfo_user_name;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getProvisionInfo_createtime() {
        return provisionInfo_createtime;
    }

    public void setProvisionInfo_createtime(String provisionInfo_createtime) {
        this.provisionInfo_createtime = provisionInfo_createtime;
    }

    public String getProduct_taginfo() {
        return product_taginfo;
    }

    public void setProduct_taginfo(String product_taginfo) {
        this.product_taginfo = product_taginfo;
    }

    public CurrentProductContentDTO() {
    }

    public CurrentProductContentDTO(int product_idx, String product_name, int product_status, String provisionInfo_platoon, String provisionInfo_user_name, String special, String provisionInfo_createtime, String product_taginfo) {
        this.product_idx = product_idx;
        this.product_name = product_name;
        this.product_status = product_status;
        this.provisionInfo_platoon = provisionInfo_platoon;
        this.provisionInfo_user_name = provisionInfo_user_name;
        this.special = special;
        this.provisionInfo_createtime = provisionInfo_createtime;
        this.product_taginfo = product_taginfo;
    }
}
