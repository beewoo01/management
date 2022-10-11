package com.example.rfid.data;


import android.util.Log;


public class Constant {

    /*FirstCare Server URL*/
//    public static final String SERVER_URL = "http://raon-soft.com/palette/";

    public static final String SERVER_URL = "http://raon-soft.com/material_management/";

    /*퍼미션*/
    public static int ACCESS_FINE_LOCATION_STAT = 1;
    public static int ACCESS_BACKGROUND_LOCATION_STAT = 1;

    public static final int PROGRESS_COUNT = 3;

    /*위치 정보 받아오기 실패 했을 때, 기본 위경도 값 (사용안함)*/
//    public static final double DefaultLatitude = 35.1750569;
//    public static final double DefaultLongitude = 129.1250885;

    /*이용약관 파일*/
    public static final String FILE_TERM_OF_USE = "fc_termofuse.txt";
    public static final String FILE_PRIVACY = "fc_privacy.txt";


    /*로그 찍기, DEBUG_MODE = true 일때만 로그 찍힘*/
    public static void LOG(String tag, String text) {
        if (UserInfo.getInstance().DEBUG_MODE) {
            Log.e(tag, text);
        }
    }


}