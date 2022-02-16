package com.hksz.demo.models;

import java.util.Arrays;
import java.util.List;

public class UserAccount {
    private int certType;
    private String certNo;
    private String pwd;

    public static UserAccount parseFromString(String text) {
        try {
            List<String> array = Arrays.asList(text.split(","));
            int certType = Integer.parseInt(array.get(0));
            String certNo = array.get(1);
            String pwd = array.get(2);
            UserAccount userAccount = new UserAccount(certType,certNo,pwd);
            return userAccount;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserAccount(int certType, String certNo, String pwd) {
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
