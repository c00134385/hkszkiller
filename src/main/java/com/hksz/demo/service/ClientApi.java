package com.hksz.demo.service;


import com.hksz.demo.models.BasicResponse;
import com.hksz.demo.models.Certificate;
import com.hksz.demo.models.RoomInfo;
import com.hksz.demo.models.UserInfo;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ClientApi {

//    public interface GetRequest_Interface {

//        @GET("openapi.do?keyfrom=abc&key=2032414398&type=data&doctype=json&version=1.1&q=car")
//        Call<Reception> getCall(@Field("name") String name);
//        // @GET注解的作用:采用Get方法发送网络请求
//
//        // getCall() = 接收网络请求数据的方法
//        // 其中返回类型为Call<*>，*是接收数据的类（即上面定义的Translation类）
//        // 如果想直接获得Responsebody中的内容，可以定义网络请求返回值为Call<ResponseBody>
//    }

//    @POST("/nationality/getCertificateList")
//    Observable<BasicResponse<List<Certificate>>> getCertificateList();

    @POST("/nationality/getCertificateList")
    Call<BasicResponse<List<Certificate>>> getCertificateList();

    @GET("/user/getVerify")
    Call<ResponseBody> getVerify(@Query("random") double random);

    @POST("/user/login")
    @Headers({"Content-Type: application/json"})
    Call<ResponseBody> login1(@Body RequestBody body);

    @POST("/user/login")
    @FormUrlEncoded
    Call<BasicResponse> login(
            @Field("certType") int certType,
            @Field("certNo") String certNo,
            @Field("pwd") String pwd,
            @Field("verifyCode") String verifyCode
    );

    @POST("/user/getUserInfo")
    Call<BasicResponse<UserInfo>> getUserInfo();

    @POST("/user/getUserInfo")
    Call<ResponseBody> getUserInfo1();

    @POST("/passInfo/userCenterIsCanReserve")
    Call<BasicResponse> isCanReserve();

    @POST("/orderInfo/getCheckInDate")
    Call<BasicResponse> getCheckInDate();

    @POST("/passInfo/gerReserveOrderInfo")
    Call<BasicResponse> getReserveOrderInfo();

    @POST("/districtHousenumLog/getList")
    @FormUrlEncoded
    Call<BasicResponse<List<RoomInfo>>> getDistrictHouseList(
            @Field("checkinDate") String checkinDate //"yyyy-MM-dd"
    );

    @GET("/passInfo/confirmOrder")
    Call<ResponseBody> confirmOrder(
            @Query("checkinDate") String checkinDate,
            @Query("t") long timespan,
            @Query("s") String sign
    );
}
