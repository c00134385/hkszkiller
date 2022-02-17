package com.hksz.demo.service;

import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;

import java.io.IOException;

public class LoggerInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Connection", "keep-alive")
                .build();
        printRequest(request);
        Response response = chain.proceed(request);
        printResponse(response);
        return response;
    }

    private void printRequest(Request request) throws IOException {
        log("-----------------------------------------------------------------");
        String requestStartMessage = "--> " + request.method() + ' ' + request.url();
        if(request.method() == "POST") {
            requestStartMessage += " (" + request.body().contentLength() + "-byte body)";
        }
        log(requestStartMessage);

        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
//            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
//                log(name + ": " + headers.value(i));
//            }
            log(name + ": " + headers.value(i));
        }

    }

    private void printResponse(Response response) {
        log("\n");
        log("<-- "  + response.toString());
        Headers headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            log(headers.name(i) + ": " + headers.value(i));
        }

        log(response.toString());

        if (!HttpHeaders.hasBody(response)) {
            log("<-- END HTTP");
        } else {
            log("<-- END HTTP (hasBody=true)");
        }
        log("\n\n");
    }

    private void log(String message) {
        System.out.println(message);
    }
}
