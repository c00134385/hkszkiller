package com.hksz.demo.service;

public class Client {

    private final static String KEY = Client.class.getSimpleName();
    private static Client instance;

    private ClientApi api;

    public static Client getInstance() {
        if (instance == null) {
            synchronized (KEY) {
                if (instance == null) {
                    instance = new Client();
                }
            }
        }
        return instance;
    }

    private Client() {
        api = new RetrofitUtils().getRetrofit().create(ClientApi.class);
    }

    public ClientApi getApi() {
        return api;
    }
}
