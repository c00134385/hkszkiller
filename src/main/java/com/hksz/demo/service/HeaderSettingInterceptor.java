package com.hksz.demo.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderSettingInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Connection", "keep-alive")
                .build();
        Response response = chain.proceed(request);
        return  response;
    }
}
