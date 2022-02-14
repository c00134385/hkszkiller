package com.droid.hkszkiller.http;

public enum Connection {
    close("close"),
    keep_alive("keep-alive");

    Connection(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}


