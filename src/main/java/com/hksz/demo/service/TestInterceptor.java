package com.hksz.demo.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TestInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
//        System.out.println("headers: " + chain.request().headers().toString());
        Request request = chain.request().newBuilder()
                .addHeader("Connection", "keep-alive")
                .build();
        Response response = chain.proceed(request);
//        System.out.println("response headers: " + response.request().headers().toMultimap().toString());
        return  response;
    }
}
