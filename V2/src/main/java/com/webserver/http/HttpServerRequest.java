package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*
 * 请求对象:
 *   该类的每一个实例就表示客户端一个请求
 *   每个请求的由三部分组成：
 *   1、请求行
 *   2、消息头
 *   3、请求正文
 * */
public class HttpServerRequest {
    private Socket socket;
    private String method;
    private String url;
    private String protocol;
    private Map<String,String> headerMap = new HashMap<>();

    public HttpServerRequest(Socket socket) throws IOException {
        this.socket = socket;
        // 解析请求首行信息
        parseRequestLine();
        // 解析请求头信息
        parseRequestHeader();
        // 解析请求正为
        parseRequestContent();

    }

    private void parseRequestLine() throws IOException {
        String line = readline();
        System.out.println("请求行:"+line);
        // 定义变量分别存储请求方式、请求路径、协议版本
        String[] d = line.split("\\s");
        method=d[0];
        url = d[1];
        protocol = d[2];
        System.out.println("method:"+method);
        System.out.println("url:"+url);
        System.out.println("protocol:"+protocol);
    }
    private void parseRequestHeader() throws IOException {
        String key;
        String value;
        // 读取请求头
        while (true){
            String line = readline();
            System.out.println("请求头:"+line);
            if (line.isEmpty()){
                break;
            }
            key = line.split(": ")[0];
            value = line.split(": ")[1];
            headerMap.put(key,value);
        }
        System.out.println(headerMap);
    }
    private void parseRequestContent(){}

    private String readline() throws IOException {
        InputStream in = socket.getInputStream();
        StringBuilder builder = new StringBuilder();
        char cur = 'a';
        char pre = 'a';
        int data;
        while ((data = in.read()) != -1) {
            cur = (char) data;
            if (cur == 10 && pre == 13) {
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();  // 返回处理好的一行请求
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeaderMap(String name) {
        return headerMap.get(name);
    }
}
