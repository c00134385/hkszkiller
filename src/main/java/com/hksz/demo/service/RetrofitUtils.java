package com.hksz.demo.service;


import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.hksz.demo.Configure;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private Retrofit retrofit;
    static String cookieHeader = "cookie";
    static String setCookieHeader = "set-cookie";
    private Map<String, Cookie> cookieMap = new HashMap<>();

    public RetrofitUtils() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new HeaderSettingInterceptor())
//                .addNetworkInterceptor(new LoggerInterceptor())
                .readTimeout(Duration.ofSeconds(60))
                .connectTimeout(Duration.ofSeconds(60))
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                        if(url.pathSegments().contains("getVerify")) {
//                            saveCookies(cookies);
//                        }
                        saveCookies(cookies);
//                        System.out.println("cookies url: " + url.toString());
//                        if(null != cookies) {
//                            cookies.forEach(new Consumer<Cookie>() {
//                                @Override
//                                public void accept(Cookie cookie) {
//                                    System.out.println("*cookie: " + cookie);
//                                }
//                            });
//                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = loadCookies();
                        return (null != cookies)?cookies:new ArrayList<Cookie>();
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Configure.host) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public List<Cookie> loadCookies() {
        return new ArrayList<>(cookieMap.values());
    }

    public void saveCookies(List<Cookie> cookies) {
        cookies.forEach(new Consumer<Cookie>() {
            @Override
            public void accept(Cookie cookie) {
                cookieMap.put(cookie.name(), cookie);
            }
        });
    }
}
