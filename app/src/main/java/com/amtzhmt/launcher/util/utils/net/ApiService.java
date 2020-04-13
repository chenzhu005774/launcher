package com.amtzhmt.launcher.util.utils.net;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * des:ApiService
 */
public interface ApiService {


    //获取首页
    @GET("apk/template/getPage")
    Call<ResponseBody> getPage( @Query("dataCode") String dataCode, @Query("orgCode") String orgCode);

    //   获取token
    @GET("apk/auth/getToken")
    Call<ResponseBody> getLoginToken();

    //  绑定机顶盒账号与唯一标识符
    @GET("apk/auth/bindMacToAccount")
    Call<ResponseBody> bindMacToAccount(@Query("mac")String  mac);

    @POST("apk/auth/authentication")
    Call<ResponseBody> login(@Body Map<String,String> params);

    @GET("apk/channel/findPage")
    Call<ResponseBody> getChannel(@Query("dataCode")String  dataCode , @Query("pageNumber")int pageNumber , @Query("pagesize")int pagesize);
    //获取apk 升级信息
    @GET("apk/manage/findPage")
    Call<ResponseBody> getApkInfo( @Query("pageNumber")int pageNumber , @Query("pagesize")int pagesize);
    //获取系统升级信息
    @GET("apk/stbSystem/findPage")
    Call<ResponseBody> getSysInfo( @Query("pageNumber")int pageNumber , @Query("pagesize")int pagesize);
    //获取系统升级信息
    @GET("apk/advert/findPage")
//    Call<ResponseBody> getAdvert( @Query("orgCode")String  orgCode , @Query("pageNumber")int pageNumber , @Query("pagesize")int pagesize);
    Call<ResponseBody> getAdvert(  @Query("pageNumber")int pageNumber , @Query("pagesize")int pagesize);
}
