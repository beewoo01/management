package com.example.rfid;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;


import com.example.rfid.data.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;


/**
 * ClassName            Utils
 * Created by JSky on   2020-06-30
 *
 * Description
 */

public class Utils {


    public static boolean getGPSState(Context context){

        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            return false;
        } else {
            return true;
        }


    }

    public static boolean getPermissionStatus(Context context){
        boolean check = false;
        if (Constant.ACCESS_FINE_LOCATION_STAT == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 29) {
                if (Constant.ACCESS_BACKGROUND_LOCATION_STAT == PackageManager.PERMISSION_GRANTED) {
                    check = true;
                } else {
                    check = false;
                }
            } else {
                check = true;
            }

        } else {
            check = false;
        }
        return check;
    }

    public static int getNetworkStatus(Context context) {
        final int TYPE_WIFI = 1;
        final int TYPE_MOBILE = 2;
        final int TYPE_NOT_CONNECTED = 3;


        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_MOBILE) {// LTE
                return TYPE_MOBILE;
            } else if (type == ConnectivityManager.TYPE_WIFI) {//wifi
                return TYPE_WIFI;
            }
        }
        return TYPE_NOT_CONNECTED;  //????????? ???????????? ??????

    }


    /**
     * ?????? ?????? ??????????????? ?????? ????????? ??????. * @author roter * http://www.jhb.kr
     */
    public static class SoundSearcher {
        private static final char HANGUL_BEGIN_UNICODE = 44032;// ???
        private static final char HANGUL_LAST_UNICODE = 55203; // ???
        private static final char HANGUL_BASE_UNIT = 588;//????????? ?????? ????????? ?????????
        // ??????
        private static final char[] INITIAL_SOUND = {'???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???'};

        /**
         * ?????? ????????? INITIAL_SOUND?????? ??????. * @param searchar * @return
         */
        private static boolean isInitialSound(char searchar) {
            for (char c : INITIAL_SOUND) {
                if (c == searchar) {
                    return true;
                }
            }
            return false;
        }

        /**
         * ?????? ????????? ????????? ?????????. * * @param c ????????? ?????? * @return
         */
        private static char getInitialSound(char c) {
            int hanBegin = (c - HANGUL_BEGIN_UNICODE);
            int index = hanBegin / HANGUL_BASE_UNIT;
            return INITIAL_SOUND[index];
        }

        /**
         * ?????? ????????? ???????????? ?????? * @param c ?????? ?????? * @return
         */
        private static boolean isHangul(char c) {
            return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
        }

        /**
         * ?????????.
         */
        public SoundSearcher() {
        }

        /**
         * ????????? ??????. ?????? ?????? ?????????.
         * @param value : ?????? ?????? ex> ?????????????????????
         * @param search : ????????? ex> ???????????????
         * @return ?????? ????????? ????????? true ???????????? false.
         */
        public static boolean matchString(String value, String search) {
            int t = 0;
            int seof = value.length() - search.length();
            int slen = search.length();
            if (seof < 0)
                return false; //???????????? ??? ?????? false??? ????????????.
            for (int i = 0; i <= seof; i++) {
                t = 0;
                while (t < slen) {
                    if (isInitialSound(search.charAt(t)) == true && isHangul(value.charAt(i + t))) { //?????? ?????? char??? ???????????? value??? ????????????
                        if (getInitialSound(value.charAt(i + t)) == search.charAt(t)) //????????? ???????????? ????????? ????????????
                            t++;
                        else break;
                    } else { //char??? ????????? ????????????
                        if (value.charAt(i + t) == search.charAt(t)) //?????? ????????? ????????????.
                            t++;
                        else break;
                    }
                }
                if (t == slen) return true; //?????? ????????? ????????? ????????? true??? ????????????.
            }
            return false; //???????????? ?????? ?????? ???????????? false??? ????????????.
        }
    }


    //?????? ????????? ?????? (???????????????) ????????? ?????????
    public static double distance(double P1_latitude, double P1_longitude,
                                  double P2_latitude, double P2_longitude) {
        if ((P1_latitude == P2_latitude) && (P1_longitude == P2_longitude)) {
            return 0;
        }
        double e10 = P1_latitude * Math.PI / 180;
        double e11 = P1_longitude * Math.PI / 180;
        double e12 = P2_latitude * Math.PI / 180;
        double e13 = P2_longitude * Math.PI / 180;
        /* ????????? GRS80 */
        double c16 = 6356752.314140910;
        double c15 = 6378137.000000000;
        double c17 = 0.0033528107;
        double f15 = c17 + c17 * c17;
        double f16 = f15 / 2;
        double f17 = c17 * c17 / 2;
        double f18 = c17 * c17 / 8;
        double f19 = c17 * c17 / 16;
        double c18 = e13 - e11;
        double c20 = (1 - c17) * Math.tan(e10);
        double c21 = Math.atan(c20);
        double c22 = Math.sin(c21);
        double c23 = Math.cos(c21);
        double c24 = (1 - c17) * Math.tan(e12);
        double c25 = Math.atan(c24);
        double c26 = Math.sin(c25);
        double c27 = Math.cos(c25);
        double c29 = c18;
        double c31 = (c27 * Math.sin(c29) * c27 * Math.sin(c29))
                + (c23 * c26 - c22 * c27 * Math.cos(c29))
                * (c23 * c26 - c22 * c27 * Math.cos(c29));
        double c33 = (c22 * c26) + (c23 * c27 * Math.cos(c29));
        double c35 = Math.sqrt(c31) / c33;
        double c36 = Math.atan(c35);
        double c38 = 0;
        if (c31 == 0) {
            c38 = 0;
        } else {
            c38 = c23 * c27 * Math.sin(c29) / Math.sqrt(c31);
        }
        double c40 = 0;
        if ((Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))) == 0) {
            c40 = 0;
        } else {
            c40 = c33 - 2 * c22 * c26
                    / (Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38)));
        }
        double c41 = Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))
                * (c15 * c15 - c16 * c16) / (c16 * c16);
        double c43 = 1 + c41 / 16384
                * (4096 + c41 * (-768 + c41 * (320 - 175 * c41)));
        double c45 = c41 / 1024 * (256 + c41 * (-128 + c41 * (74 - 47 * c41)));
        double c47 = c45
                * Math.sqrt(c31)
                * (c40 + c45
                / 4
                * (c33 * (-1 + 2 * c40 * c40) - c45 / 6 * c40
                * (-3 + 4 * c31) * (-3 + 4 * c40 * c40)));
        double c50 = c17
                / 16
                * Math.cos(Math.asin(c38))
                * Math.cos(Math.asin(c38))
                * (4 + c17
                * (4 - 3 * Math.cos(Math.asin(c38))
                * Math.cos(Math.asin(c38))));
        double c52 = c18
                + (1 - c50)
                * c17
                * c38
                * (Math.acos(c33) + c50 * Math.sin(Math.acos(c33))
                * (c40 + c50 * c33 * (-1 + 2 * c40 * c40)));
        double c54 = c16 * c43 * (Math.atan(c35) - c47);
        // return distance in meter
        return c54;
    }

    //????????? ????????? ??????
    public short bearingP1toP2(double P1_latitude, double P1_longitude,
                               double P2_latitude, double P2_longitude) {
        // ?????? ?????? : ????????? ????????? ?????? ????????? ???????????? ?????? ???????????? ?????????
        //????????? ????????? ????????????.
        double Cur_Lat_radian = P1_latitude * (3.141592 / 180);
        double Cur_Lon_radian = P1_longitude * (3.141592 / 180);
        // ?????? ?????? : ????????? ????????? ?????? ????????? ???????????? ?????? ???????????? ?????????
        // ????????? ????????? ????????????.
        double Dest_Lat_radian = P2_latitude * (3.141592 / 180);
        double Dest_Lon_radian = P2_longitude * (3.141592 / 180);
        // radian distance
        double radian_distance = 0;
        radian_distance = Math.acos(Math.sin(Cur_Lat_radian)
                * Math.sin(Dest_Lat_radian) + Math.cos(Cur_Lat_radian)
                * Math.cos(Dest_Lat_radian)
                * Math.cos(Cur_Lon_radian - Dest_Lon_radian));
        // ????????? ?????? ????????? ?????????.(?????? ???????????? ?????? ????????? ???????????? ????????????
        //????????? ???????????? ??????. ??????????????????.
        double radian_bearing = Math.acos((Math.sin(Dest_Lat_radian) - Math
                .sin(Cur_Lat_radian)
                * Math.cos(radian_distance))
                / (Math.cos(Cur_Lat_radian) * Math.sin(radian_distance)));
        // acos??? ????????? ???????????? x??? 360????????? ????????? ?????? radian(??????)?????????.
        double true_bearing = 0;
        if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0) {
            true_bearing = radian_bearing * (180 / 3.141592);
            true_bearing = 360 - true_bearing;
        } else {
            true_bearing = radian_bearing * (180 / 3.141592);
        }
        return (short) true_bearing;
    }

    public static String getChangeDateFormat(String dateTime) {

        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-M-d", Locale.KOREA);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy???MM???dd???", Locale.KOREA);

        String newDate="";
        try {

            Date oldDate = oldFormat.parse(dateTime);
            newDate = newFormat.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDate;
    }

    public static String getNowDate() {

        long now = System.currentTimeMillis();

        Date date = new Date(now);

        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        String formatDate = sdfNow.format(date);

        return formatDate;
    }

    public static String getNowDateKR() {

        long now = System.currentTimeMillis();

        Date date = new Date(now);

        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy???MM???dd???", Locale.KOREA);

        String formatDate = sdfNow.format(date);

        return formatDate;
    }

    public static String getNowTime() {

        long now = System.currentTimeMillis();

        Date date = new Date(now);

        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);

        String formatDate = sdfNow.format(date);

        return formatDate;
    }

    public static int getNowDatePick(int type) {

        long now = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        int formatDate=0;

       switch (type){

           case 1:
               formatDate = calendar.get(Calendar.YEAR);
               break;

           case 2:
               formatDate = (calendar.get(Calendar.MONTH) + 1);
               break;

           case 3:
               formatDate = calendar.get(Calendar.DAY_OF_MONTH);
               break;
       }


        return formatDate;
    }


    public static String getNowDateDay() {

        long now = System.currentTimeMillis();

        Date date = new Date(now);

        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd (EEE) HH:mm:ss", Locale.KOREA);

        String formatDate = sdfNow.format(date);

        return formatDate;
    }

    public static String getNowDateTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        String formatDate = sdfNow.format(date);

        return formatDate;
    }

    /**
     * ?????? ????????? ????????? ????????? ??????(??? ~ ???)
     * @param date
     * @param dateType
     * @return
     * @throws Exception
     */
    public static String getDateToDay(String date, String dateType) throws Exception {

        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "???";
                break;
            case 2:
                day = "???";
                break;
            case 3:
                day = "???";
                break;
            case 4:
                day = "???";
                break;
            case 5:
                day = "???";
                break;
            case 6:
                day = "???";
                break;
            case 7:
                day = "???";
                break;

        }

        return day;
    }
    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }


    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }


    public static String getCurrentAddress(Context mContext, double latitude, double longitude) {

        //GPS??? ????????? ??????
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //gps, ???????????? ??????
            //  ((MainActivity)mContext).showSnackbar(mContext.getString(R.string.snackbar_msg_gps_err));
            //  Toast.makeText(mContext, "?????? ?????? : GPS ????????? ????????????", Toast.LENGTH_LONG).show();
            return "[err] ?????? ?????? : GPS ????????? ????????????";
        } catch (IllegalArgumentException illegalArgumentException) {
            //  ((MainActivity)mContext).showSnackbar(mContext.getString(R.string.snackbar_msg_gpspoint_err));
            // Toast.makeText(mContext, "????????? GPS ??????", Toast.LENGTH_LONG).show();
            return "[err] ????????? GPS ?????? ????????? ?????????.";

        }



        if (addresses == null || addresses.size() == 0) {
            //  ((MainActivity)mContext).showSnackbar(mContext.getString(R.string.snackbar_msg_findaddr_err));
            // Toast.makeText(mContext, "?????? ?????????", Toast.LENGTH_LONG).show();
            return "[err] ????????? ?????? ????????? ?????????. ";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    public static double getCurrentMapPoint(Context mContext, String locationName, int type) {

        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        List<Address> addresses;

        /*???????????? ??????*/
        try {

            addresses = geocoder.getFromLocationName(
                    locationName,
                    7);
//            for(int i=0; i < addresses.size(); i++){
//                Log.e("fc_debug", addresses.get(i).toString());
//            }

        } catch (IOException ioException) {
            //gps, ???????????? ??????
            //  ((MainActivity)mContext).showSnackbar(mContext.getString(R.string.snackbar_msg_gps_err));
            //  Toast.makeText(mContext, "?????? ?????? : GPS ????????? ????????????", Toast.LENGTH_LONG).show();
            return -1;
        } catch (IllegalArgumentException illegalArgumentException) {
            //  ((MainActivity)mContext).showSnackbar(mContext.getString(R.string.snackbar_msg_gpspoint_err));
            // Toast.makeText(mContext, "????????? GPS ??????", Toast.LENGTH_LONG).show();
            return -1;

        }



        if (addresses == null || addresses.size() == 0) {
            //  ((MainActivity)mContext).showSnackbar(mContext.getString(R.string.snackbar_msg_findaddr_err));
            // Toast.makeText(mContext, "?????? ?????????", Toast.LENGTH_LONG).show();
            return -1;

        }

        Address address = addresses.get(0);
        double mapPoint=0;
        if(type == 0){
            mapPoint = address.getLatitude();
        }else{
            mapPoint = address.getLongitude();
        }
        return mapPoint;

    }

    public static void waitTwoSec(final View v){

        v.setClickable(false);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                v.setClickable(true);


            }
        }, 1500); // 2
    }

    public static String getIpAddress(){
        //Device??? ?????? ?????? ??????????????? ?????? ??????
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();

                //???????????? ????????? IP??? ????????? ?????? ?????? ??? ??? ??? ??????
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {

                    InetAddress inetAddress = enumIpAddr.nextElement();


                    //IPv6 : fe80::64b9::c8dd:7003
                    //IPv4 : 123.234.123.123

                    if(inetAddress.isLoopbackAddress()) {
                        Log.i("IPAddress", intf.getDisplayName() + "(loopback) | " + inetAddress.getHostAddress());
                    }
                    else
                    {
                        Log.i("IPAddress", intf.getDisplayName() + " | " + inetAddress.getHostAddress());
                    }

                    //IPv4 ?????? ??????
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "-";

    }

    public static String textFileReader(Context context, String fileName) {
        String entireFile = "";
        String line;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open("text/" + fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            while((line = br.readLine()) != null) { // <--------- place readLine() inside loop
                entireFile += (line + "\n"); // <---------- add each line to entireFile
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return entireFile;
    }

    public static String birthDayToAge(String birthday) {
        int age;
        String nowDate = getNowDate();

        // foramt yyyy-MM-dd
        int nowYear = Integer.parseInt(nowDate.substring(0, 4));
        int nowMonth = Integer.parseInt(nowDate.substring(5, 7));
        int nowDay = Integer.parseInt(nowDate.substring(8, 10));

        int userYear = Integer.parseInt(birthday.substring(0, 4));
        int userMonth = Integer.parseInt(birthday.substring(5, 7));
        int userDay = Integer.parseInt(birthday.substring(8, 10));

        age = nowYear - userYear;

        if (nowMonth < userMonth) { // ???????????? "???"??? ???????????? ??????
            age--;

        } else if (nowMonth == userMonth) { // ???????????? "???"??? ???????????? ??????

            if (nowDay < userDay) {
                age--; // ?????? ??????????????? (????????? - 1)

            }

        }

        return String.valueOf(age + 1);
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

}
