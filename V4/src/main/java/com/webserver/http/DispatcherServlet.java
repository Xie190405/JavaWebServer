package com.webserver.http;

import com.webserver.controller.UserController;

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

    private DispatcherServlet(){
    }

    public static DispatcherServlet getInstance(){
        if(obj==null){
            obj = new DispatcherServlet();
        }
        return obj;
    }
    public void server(HttpServerRequest request, HttpServletResponse response){
        String path = request.getRequestURL();  // 表示请求部分
        if("/myweb/reg".equals(path)){
            // 满足就进行注册操作
            System.out.println("!!!!!!!!!!!!!!!!注册操作");
            UserController controller = new UserController();
            controller.reg(request, response);
        }else{
            File file = new File("./webapps"+path);
            if(file.isFile()){
                response.setFile(file);
            }else{
                //不是文件(要么不存在,要么是目录,404情况)
                file = new File("./webapps/root/404.html");
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                response.setFile(file);
            }
        }
        response.addHeader("server","WebServer");
    }
}
