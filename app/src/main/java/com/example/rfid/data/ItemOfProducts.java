package com.example.rfid.data;

import com.google.gson.annotations.SerializedName;

public class ItemOfProducts {

    @SerializedName("item_idx")
    private int item_idx;

    @SerializedName("item_name")
    private String item_name;

    @SerializedName("item_image")
    private String item_image;

    @SerializedName("account_account_idx")
    private int account_account_idx;

    @SerializedName("item_createtime")
    private String item_createtime;

    @SerializedName("item_updatetime")
    private String item_updatetime;

    @SerializedName("allCount")
    private int allCount;

    @SerializedName("remainingProduct")
    private int remainingProduct;

    public int getItem_idx() {
        return item_idx;
    }

    public void setItem_idx(int item_idx) {
        this.item_idx = item_idx;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public int getAccount_account_idx() {
        return account_account_idx;
    }

    public void setAccount_account_idx(int account_account_idx) {
        this.account_account_idx = account_account_idx;
    }

    public String getItem_createtime() {
        return item_createtime;
    }

    public void setItem_createtime(String item_createtime) {
        this.item_createtime = item_createtime;
    }

    public String getItem_updatetime() {
        return item_updatetime;
    }

    public void setItem_updatetime(String item_updatetime) {
        this.item_updatetime = item_updatetime;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getRemainingProduct() {
        return remainingProduct;
    }

    public void setRemainingProduct(int remainingProduct) {
        this.remainingProduct = remainingProduct;
    }

    public ItemOfProducts() {
    }

    public ItemOfProducts(int item_idx, String item_name, String item_image, int account_account_idx, String item_createtime, String item_updatetime, int allCount, int remainingProduct) {
        this.item_idx = item_idx;
        this.item_name = item_name;
        this.item_image = item_image;
        this.account_account_idx = account_account_idx;
        this.item_createtime = item_createtime;
        this.item_updatetime = item_updatetime;
        this.allCount = allCount;
        this.remainingProduct = remainingProduct;
    }
}
