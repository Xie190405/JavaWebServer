package com.webserver.controller;

import com.webserver.http.HttpServerRequest;
import com.webserver.http.HttpServletResponse;
import com.webserver.vo.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/* 处理用户相关的处理*/
public class UserController {
    private static File userDir;
    static {
        userDir = new File("./users");
        if (!userDir.exists()){
            userDir.mkdirs();
        }
    }

    public void reg(HttpServerRequest request, HttpServletResponse response){
        System.out.println("开始处理注册！");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageString = request.getParameter("age");
        System.out.println(username+","+password+","+nickname+","+ageString);
        // 对数据合法性进行验证
        if (username==null||password==null||nickname==null||ageString==null||ageString.matches("[1-9]+")){
            System.out.println("数据不合法");
            File file = new File("./webapps/myweb/reg_info_error.html");
            response.setFile(file);
            return;
        }
        else {
            File file = new File(userDir,username+".obj");
            if(file.exists()){
                response.setFile(new File("./webapps/myweb/reg_has_user.html"));
                return;
            }
        }
        // 对数据是否已经存在判断
        int age = Integer.parseInt(ageString);
        User user = new User(username,password,nickname,age);

        // 持久化数据 对象写入文件
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(new File(userDir,username+".obj"))
        )
        ){
            oos.writeObject(user);
            System.out.println("写入完毕！");
            File file = new File("./webapps/myweb/reg_success.html");
            response.setFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("注册成功");
    }

    public void login(HttpServerRequest request,HttpServletResponse response){

    }
}
