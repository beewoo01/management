package com.example.rfid.data;

import com.google.gson.annotations.SerializedName;

public class JoinAccountDTO {
    @SerializedName("account_idx")
    private int account_idx;

    @SerializedName("account_id")
    private String account_id;

    @SerializedName("account_password")
    private String account_password;

    @SerializedName("account_name")
    private String account_name;

    @SerializedName("account_ispro")
    private int account_ispro = 0;

    @SerializedName("armyunit_idx")
    private int armyunit_idx;

    @SerializedName("armyunit_name")
    private String armyunit_name;

    @SerializedName("armyunit_division")
    private String armyunit_division;

    @SerializedName("armyunit_regiment")
    private String armyunit_regiment;

    @SerializedName("armyunit_battalion")
    private String armyunit_battalion;

    @SerializedName("accoundArmyUnit_permission")
    private int accoundArmyUnit_permission = 0;

    public int getAccount_idx() {
        return account_idx;
    }

    public void setAccount_idx(int account_idx) {
        this.account_idx = account_idx;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_password() {
        return account_password;
    }

    public void setAccount_password(String account_password) {
        this.account_password = account_password;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public int getAccount_ispro() {
        return account_ispro;
    }

    public void setAccount_ispro(int account_ispro) {
        this.account_ispro = account_ispro;
    }

    public int getArmyunit_idx() {
        return armyunit_idx;
    }

    public void setArmyunit_idx(int armyunit_idx) {
        this.armyunit_idx = armyunit_idx;
    }

    public String getArmyunit_name() {
        return armyunit_name;
    }

    public void setArmyunit_name(String armyunit_name) {
        this.armyunit_name = armyunit_name;
    }

    public String getArmyunit_division() {
        return armyunit_division;
    }

    public void setArmyunit_division(String armyunit_division) {
        this.armyunit_division = armyunit_division;
    }

    public String getArmyunit_regiment() {
        return armyunit_regiment;
    }

    public void setArmyunit_regiment(String armyunit_regiment) {
        this.armyunit_regiment = armyunit_regiment;
    }

    public String getArmyunit_battalion() {
        return armyunit_battalion;
    }

    public void setArmyunit_battalion(String armyunit_battalion) {
        this.armyunit_battalion = armyunit_battalion;
    }

    public int getAccoundArmyUnit_permission() {
        return accoundArmyUnit_permission;
    }

    public void setAccoundArmyUnit_permission(int accoundArmyUnit_permission) {
        this.accoundArmyUnit_permission = accoundArmyUnit_permission;
    }

    public JoinAccountDTO() {
    }

    public JoinAccountDTO(int account_idx, String account_id, String account_password, String account_name, int account_ispro, int armyunit_idx, String armyunit_name, String armyunit_division, String armyunit_regiment, String armyunit_battalion, int accoundArmyUnit_permission) {
        this.account_idx = account_idx;
        this.account_id = account_id;
        this.account_password = account_password;
        this.account_name = account_name;
        this.account_ispro = account_ispro;
        this.armyunit_idx = armyunit_idx;
        this.armyunit_name = armyunit_name;
        this.armyunit_division = armyunit_division;
        this.armyunit_regiment = armyunit_regiment;
        this.armyunit_battalion = armyunit_battalion;
        this.accoundArmyUnit_permission = accoundArmyUnit_permission;
    }
}
