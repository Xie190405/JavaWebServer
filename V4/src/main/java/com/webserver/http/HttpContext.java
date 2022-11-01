package com.webserver.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class HttpContext {
    private static Map<String,String> mimeMapping = new HashMap<>();
    static {
        initMimeMapping();
    }
    private static void initMimeMapping(){
        Properties properties = new Properties();
        try {
            properties.load(HttpContext.class.getResourceAsStream("web.properties"));
            properties.forEach((k,v)->mimeMapping.put(k.toString(),v.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getMimetype(String ext){
        return mimeMapping.get(ext);
    }

    public static Boolean containsKey(String ext){
        return mimeMapping.containsKey(ext);
    }
}
