package com.droid.hkszkiller.http;

import com.droid.hkszkiller.models.BasicResponse;
import com.droid.hkszkiller.models.Certificate;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

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

    @POST("/nationality/getCertificateList")
    Observable<BasicResponse<List<Certificate>>> getCertificateList();

    @GET("/user/getVerify")
    Call<ResponseBody> getVerify(@Query("random") double random);

    @POST("/nationality/getCertificateList")
    Call<BasicResponse<List<Certificate>>> getCertificateList1();

    @GET("/api/playlist/videos/v1")
    Observable<Object> userInfo();

    @POST("/api/linked/check/v1")
    Observable<Object> login(
            @Field("certType") int certType,
            @Field("certNo") String certNo,
            @Field("pwd") String pwd,
            @Field("verifyCode") String verifyCode);
}
