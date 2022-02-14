package com.droid.hkszkiller.http;

import androidx.annotation.NonNull;

public class Request {
    private static String CLCR = "\r\n";

    private Method method;
    private String path;
    private String version = "HTTP/1.1";
//    private String host = "hk.sz.gov.cn";
//    private int port = 8118;
    private String host = Configure.host;
    private int port = Configure.port;
    private Connection connection = Connection.keep_alive;

    public Request(Method method, String path) {
        this.method = method;
        this.path = path;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(method.name() + " " + path + " " + version + CLCR);
        sb.append("Host: " +  host + ":" + port + CLCR);
        sb.append("Connection: " + connection.getName() + CLCR);
        sb.append(CLCR);
        return sb.toString();
    }
}
