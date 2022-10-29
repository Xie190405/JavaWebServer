package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/*
* 单例模式:
*   1、提供静态私有的当前类
*   2、私有化构造方法
*   3、提供一个静态公开的方法获取该类的实例，只实例一次
* */
public class DispatcherServlet {

    private static DispatcherServlet obj;
    private Map<String,String> contentType = new HashMap<>();

    private DispatcherServlet(){
        //html          text/html
        //css           text/css
        //js            application/javascript
        //png           image/png
        //gif           image/gif
        //jpg           image/jpeg
        contentType.put("html","text/html");
        contentType.put("css","text/css");
        contentType.put("js","application/javascript");
        contentType.put("png","image/png");
        contentType.put("gif","image/gif");
        contentType.put("jpg","image/image/jpeg");

    }

    public static DispatcherServlet getInstance(){
        if(obj==null){
            obj = new DispatcherServlet();
        }
        return obj;
    }
    public void server(HttpServerRequest request, HttpServletResponse response){
        String path = request.getUrl();
        File file = new File("./webapps"+path);
        if(file.isFile()){
            //如果定位是文件
            response.setFile(file);
            String fileType = file.getName().split("\\.")[1];
            // 设置两个响应头Content-type和Content-length
            if(contentType.containsKey(fileType)){
                response.addHeader("Content-Type",contentType.get(fileType));
            }
            else{
                //默认接受html
                response.addHeader("Content-Type","text/html");
            }
            response.addHeader("Content-Length",String.valueOf(file.length()));
        }else{//不是文件(要么不存在,要么是目录,404情况)
            file = new File("./webapps/root/404.html");
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            response.setFile(file);
            response.addHeader("Content-Type","text/html");
            response.addHeader("Content-Length",String.valueOf(file.length()));
        }
        response.addHeader("server","WebServer");
    }
}
