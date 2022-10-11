package com.example.rfid.api;


import com.example.rfid.data.CurrentProductContentDTO;
import com.example.rfid.data.ItemOfProducts;
import com.example.rfid.data.JoinAccountDTO;
import com.example.rfid.data.ProductDTO;
import com.example.rfid.model.UserModel;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIService {

    /*로그인*/
    @POST("loginAccount")
    Call<Integer> loginAccount(
            @Query("account_id") String account_id,
            @Query("account_password") String account_password,
            @Query("account_isPro") int account_isPro
    );

    /*회원가입*/
    @POST("signUpAccount")
    Call<Integer> signUpAccount(@Query("account_id") String account_id,
                                @Query("account_password") String account_password,
                                @Query("account_name") String account_name,
                                @Query("account_ispro") int account_ispro,
                                @Query("armyunit_idx") int armyunit_idx,
                                @Query("accoundArmyUnit_permission") int accoundArmyUnit_permission);

    /*아이디찾기*/
    @GET("findID")
    Call<String> findID(@Query("account_name") String account_name,
                        @Query("accoundArmyUnit_armyunit_idx") int accoundArmyUnit_armyunit_idx);


    /*비밀번호찾기*/
    @GET("findPassword")
    Call<String> findPassword(@Query("account_name") String account_name,
                              @Query("accoundArmyUnit_armyunit_idx") int accoundArmyUnit_armyunit_idx,
                              @Query("account_id") String account_id);


    /*부대조회*/
    @GET("getArmyUnitName")
    Call<JoinAccountDTO> getArmyUnitName(@Query("account_idx") int account_idx);

    /*품목조회*/
    @GET("getProductCountOfItems")
    Call<List<ItemOfProducts>> getProductCountOfItems(@Query("account_idx") int account_idx);

    /*자재조회*/
    @GET("getProductOfItemIdx")
    Call<List<CurrentProductContentDTO>> getProductOfItemIdx(@Query("item_idx") int item_idx);

    /*자재수정*/
    @POST("updateProduct")
    Call<Integer> updateProduct(@Query("product_account_idx") int product_account_idx,
                                @Query("product_name") String product_name,
                                @Query("product_description") String product_description,
                                @Query("product_taginfo") String product_taginfo,
                                @Query("product_status") int product_status,
                                @Query("product_idx") int product_idx);

    /*품목추가*/
    @POST("registItem")
    Call<Integer> registItem(@Query("item_name") String item_name,
                             @Query("item_image") String item_image,
                             @Query("account_idx") int account_idx);

    /*자재추가*/
    @POST("registProduct")
    Call<Integer> registProduct(@Query("product_name") String product_name,
                                @Query("product_description") String product_description,
                                @Query("product_item_idx") int product_item_idx,
                                @Query("product_account_idx") int product_account_idx,
                                @Query("product_taginfo") String product_taginfo);

    /*자재불출*/
    @POST("provistionProduct")
    Call<Integer> provistionProduct(@Query("provisionInfo_product_idx") int provisionInfo_product_idx,
                                    @Query("provisionInfo_account_idx") int provisionInfo_account_idx,
                                    @Query("provisionInfo_company") String provisionInfo_company,
                                    @Query("provisionInfo_platoon") String provisionInfo_platoon,
                                    @Query("provisionInfo_user_name") String provisionInfo_user_name,
                                    @Query("provisionInfo_defualt") String provisionInfo_defualt);

    /*태그로 자재조회*/
    @GET("getProductIdxOfTagInfo")
    Call<ProductDTO> getProductIdxOfTagInfo(@Query("product_taginfo") String product_taginfo);

    /*반납하기*/
    @POST("returnProduct")
    Call<Integer> returnProduct(@Query("product_status") int product_status,
                                @Query("product_idx") int product_idx);


    @GET("getProductInfoOfTag")
    Call<HashMap<String, Object>> getProductInfoOfTag(
            @Query("product_taginfo") String product_taginfo
    );


}