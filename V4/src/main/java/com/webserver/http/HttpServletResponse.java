package com.webserver.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * 响应格式
 *   HTTP/1.1 200 OK(CRLF)
     Content-Type: text/html(CRLF)
     Content-Length: 2546(CRLF)(CRLF)
     1011101010101010101......
 *   1、响应首行:HTTP/1.1 200 OK(CRLF)
 *   2、响应类型:Content-Type
 *   3、响应正文长度:Content-Length
 * */
public class HttpServletResponse {
    //状态行相关信息
    private int statusCode = 200;
    private String statusReason = "OK";

    private Map<String, String> headers = new HashMap<>();

    private File file;
    private final OutputStream out;

    public HttpServletResponse(Socket socket) throws IOException {
        this.out = socket.getOutputStream();
    }

    public void response() throws IOException {
        //3.1发送状态行
        sendResponseLine();
        //3.2发送响应头
        sendResponseHeaders();
        //3.3发送响应正文
        sendResponseContents();
        System.out.println("响应发送完毕");
    }

    private void sendResponseLine() throws IOException {
        // 实现将./webapps/myweb/classTable.html文件响应给客户端
        String line = "HTTP/1.1 " + statusCode + " " + statusReason;
        sendData(line);
    }

    private void sendResponseHeaders() throws IOException {
        Set<Map.Entry<String, String>> entrySet = headers.entrySet();
        for (Map.Entry<String, String> e : entrySet) {
            String key = e.getKey();
            String value = e.getValue();
            String line = key + ": " + value;
            sendData(line);
        }
        // 单独发送一个回车换行符，标识结束响应
        sendCRLF();
    }

    private void sendResponseContents() throws IOException {
        if(file!=null){
            // 以下代码 自动关闭流
            try(FileInputStream fis = new FileInputStream(file)){
                int len;
                byte[] data = new byte[1024 * 10];
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);
                }
            }
        }
    }

    private void sendData(String line) throws IOException {
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        sendCRLF();
    }

    private void sendCRLF() throws IOException {
        out.write(13);
        out.write(10);
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setFile(File file) {
        this.file = file;

        String[] fileTypes = file.getName().split("\\.");
        String fileType = fileTypes[fileTypes.length-1];
        // 设置两个响应头Content-type和Content-length
        if(HttpContext.containsKey(fileType)){
           addHeader("Content-Type",HttpContext.getMimetype(fileType));
        }
        else{
            //默认接受html
            addHeader("Content-Type","text/html");
        }
        addHeader("Content-Length",String.valueOf(file.length()));
    }

    /**
     * @param name 请求头名称
     * @param value 请求头值
     * */
    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }
}
