package com.gitee.Jmysy.binlog4j.core.utils;

import com.google.gson.Gson;

public class GsonUtils {

    private static final Gson gson = new Gson();

    public static  <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }


}
