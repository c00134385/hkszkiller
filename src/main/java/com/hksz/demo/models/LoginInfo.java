package com.hksz.demo.models;

import java.util.Arrays;
import java.util.List;

public class LoginInfo {
    private int certType;
    private String certNo;
    private String pwd;

    public static LoginInfo parseFromString(String text) {
        try {
            List<String> array = Arrays.asList(text.split(","));
            int certType = Integer.parseInt(array.get(0));
            String certNo = array.get(1);
            String pwd = array.get(2);
            LoginInfo loginInfo = new LoginInfo(certType,certNo,pwd);
            return loginInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LoginInfo(int certType, String certNo, String pwd) {
        this.certType = certType;
        this.certNo = certNo;
        this.pwd = pwd;
    }

    public int getCertType() {
        return certType;
    }

    public void setCertType(int certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
