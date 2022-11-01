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

    private String requestURL;
    private String queryString;
    private Map<String,String> parameters = new HashMap<>();


    private Map<String,String> headerMap = new HashMap<>();

    public HttpServerRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        // 解析请求首行信息
        parseRequestLine();
        // 解析请求头信息
        parseRequestHeader();
        // 解析请求正为
        parseRequestContent();

    }

    private void parseRequestLine() throws IOException,EmptyRequestException {
        String line = readline();
        System.out.println("请求行:"+line);
        if (line.isEmpty()){
            throw new EmptyRequestException();
        }
        // 定义变量分别存储请求方式、请求路径、协议版本
        String[] d = line.split("\\s");
        method=d[0];
        url = d[1];
        parseUrl(url);
        protocol = d[2];
        System.out.println("method:"+method);
        System.out.println("url:"+url);
        System.out.println("protocol:"+protocol);
    }

    private void parseUrl(String url) {
        // 分两种情况:有参、无参
        String[] data = url.split("\\?");
        requestURL = data[0];
        if (data.length>1) {
            queryString = data[1];
            String[] strings = queryString.split("&");
            for (String s : strings) {
                String[] params = s.split("=");
                if (params.length > 1) {
                    parameters.put(params[0], params[1]);
                } else {
                    parameters.put(params[0], null);
                }
            }
        }
//        if(url.contains("?")){
//            // 有参
//            // https://www.baidu.com/?
//            requestURL = url.substring(0,url.indexOf("?"));
//            queryString = url.substring(url.indexOf("?")+1);
//            String[] strings = queryString.split("&");
//            for (String s: strings){
//                String[] params = s.split("=");
//                if(params.length>1){
//                    parameters.put(params[0],params[1]);
//                }else{
//                    parameters.put(params[0],null);
//                }
//            }
//        }
//        else{
//            requestURL = url;
//        }
        System.out.println("requestURL:"+requestURL);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters"+parameters);
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

    public String getRequestURL() {
        return requestURL;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name){
        return parameters.get(name);
    }
}
