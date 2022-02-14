package com.droid.hkszkiller.http;

import java.util.ArrayList;
import java.util.List;

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
    private List<Cookie> cookies = new ArrayList<>();

    RetrofitUtils() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new TestInterceptor())
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        setCookies(cookies);
                        System.out.println("cookies url: " + url.toString());
//                        for (Cookie cookie : cookies)
//                        {
//                            System.out.println("cookies: " + cookie.toString());
//                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
//                        ArrayList<Cookie> cookies = new ArrayList<>();
//                        Cookie cookie = new Cookie.Builder()
//                                .hostOnlyDomain(url.host())
//                                .name("SESSION").value("zyao89")
//                                .build();
//                        cookies.add(cookie);
//                        cookies.addAll(getCookies());
                        return getCookies();
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

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }
}
