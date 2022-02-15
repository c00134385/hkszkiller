package com.hksz.demo.models;

public class LoginResult {
    private int fillStatus;
    private String redirect_uri;

    public int getFillStatus() {
        return fillStatus;
    }

    public void setFillStatus(int fillStatus) {
        this.fillStatus = fillStatus;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }
}
