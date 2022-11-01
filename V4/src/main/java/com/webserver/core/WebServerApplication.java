package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
*   WebServerApplication是一个web容器，实现Tomcat的基础功能
*   web容器主要有两个功能:
*   1、管理部署网络应用
*   2、处理客户端的请求，建立TCP连接和使用HTTP协议进行交互，使得浏览器可以调用
* 部署在网络应用中的功能
*   网络应用:每个网络应用通常都包含网页、素材、逻辑处理代码等，就是我们俗称的一个网站
*
 */
public class WebServerApplication {
    private ServerSocket serverSocket;

    // 构造方法初始化服务端,端口为8088
    public WebServerApplication(){
        try {
            System.out.println("正在启动服务端...");
            serverSocket = new ServerSocket(8088);
            System.out.println("服务端启动成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void start(){
        try {
            while (true) {
                System.out.println("等待客户端连接...");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端连接成功");
                // 启动一个线程处理客户端请求
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        WebServerApplication webServer = new WebServerApplication();
        webServer.start();
    }

}
