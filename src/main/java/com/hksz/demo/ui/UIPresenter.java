package com.hksz.demo.ui;

import com.hksz.demo.models.Certificate;
import com.hksz.demo.service.Client;
import com.hksz.demo.service.ClientApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class UIPresenter {

//    private final static String KEY = UIPresenter.class.getSimpleName();
//    private static UIPresenter instance;
//
//
//    public static UIPresenter getInstance() {
//        if (instance == null) {
//            synchronized (KEY) {
//                if (instance == null) {
//                    instance = new UIPresenter();
//                }
//            }
//        }
//        return instance;
//    }

    private ClientApi api;
    EventListener listener;

    public UIPresenter(EventListener listener) {
        this.listener = listener;
        api = Client.getInstance().getApi();
        initIndex();
    }

    private void initIndex() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Call<ResponseBody> call = api.index();
                        Response<ResponseBody> response = call.execute();
                        if(response.isSuccessful()) {
                            listener.onIndexDone();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public List<Certificate> getCertificateList() {
        return certificateList;
    }

    private List<Certificate> certificateList;


    public interface EventListener {
        void onIndexDone();
    }
}
