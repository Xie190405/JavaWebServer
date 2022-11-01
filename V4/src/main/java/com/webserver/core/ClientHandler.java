package com.webserver.core;

import com.webserver.http.DispatcherServlet;
import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpServerRequest;
import com.webserver.http.HttpServletResponse;

import java.io.IOException;
import java.net.Socket;

/*
* 线程任务:处理客户端请求
* */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            // 1、解析请求
            HttpServerRequest request = new HttpServerRequest(socket);
            HttpServletResponse response = new HttpServletResponse(socket);
            // 2、处理请求
            DispatcherServlet servlet = DispatcherServlet.getInstance();
            servlet.server(request,response);
            // 3、发送响应
            response.response();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EmptyRequestException e) {
            e.printStackTrace();
        } finally {
            try {
                // 响应后断开连接
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
