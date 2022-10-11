package com.example.rfid.data;

import com.google.gson.annotations.SerializedName;

public class ProductDTO {

    @SerializedName("product_idx")
    public int product_idx;

    @SerializedName("product_name")
    public String product_name;

    @SerializedName("product_description")
    public String product_description;

    @SerializedName("product_item_idx")
    public int product_item_idx;

    @SerializedName("product_account_idx")
    public int product_account_idx;

    @SerializedName("product_taginfo")
    public String product_taginfo;

    @SerializedName("product_status")
    public int product_status;

    @SerializedName("product_createtime")
    public String product_createtime;

    @SerializedName("product_updatetime")
    public String product_updatetime;

    @SerializedName("item_image")
    public String item_image;


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

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public int getProduct_item_idx() {
        return product_item_idx;
    }

    public void setProduct_item_idx(int product_item_idx) {
        this.product_item_idx = product_item_idx;
    }

    public int getProduct_account_idx() {
        return product_account_idx;
    }

    public void setProduct_account_idx(int product_account_idx) {
        this.product_account_idx = product_account_idx;
    }

    public String getProduct_taginfo() {
        return product_taginfo;
    }

    public void setProduct_taginfo(String product_taginfo) {
        this.product_taginfo = product_taginfo;
    }

    public int getProduct_status() {
        return product_status;
    }

    public void setProduct_status(int product_status) {
        this.product_status = product_status;
    }

    public String getProduct_createtime() {
        return product_createtime;
    }

    public void setProduct_createtime(String product_createtime) {
        this.product_createtime = product_createtime;
    }

    public String getProduct_updatetime() {
        return product_updatetime;
    }

    public void setProduct_updatetime(String product_updatetime) {
        this.product_updatetime = product_updatetime;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public ProductDTO() {
    }

    public ProductDTO(int product_idx, String product_name, String product_description, int product_item_idx, int product_account_idx, String product_taginfo, int product_status, String product_createtime, String product_updatetime, String item_image) {
        this.product_idx = product_idx;
        this.product_name = product_name;
        this.product_description = product_description;
        this.product_item_idx = product_item_idx;
        this.product_account_idx = product_account_idx;
        this.product_taginfo = product_taginfo;
        this.product_status = product_status;
        this.product_createtime = product_createtime;
        this.product_updatetime = product_updatetime;
        this.item_image = item_image;
    }
}
