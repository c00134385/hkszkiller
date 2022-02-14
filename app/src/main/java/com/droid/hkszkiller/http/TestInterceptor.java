package com.droid.hkszkiller.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class TestInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
