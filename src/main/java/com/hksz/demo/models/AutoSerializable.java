package com.hksz.demo.models;

import com.google.gson.Gson;

import java.io.Serializable;

abstract public class AutoSerializable implements Serializable {

    @Override
    public String toString() {
//        return super.toString();
        return new Gson().toJson(this);
    }
}
