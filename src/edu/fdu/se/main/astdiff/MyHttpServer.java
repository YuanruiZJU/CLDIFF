package edu.fdu.se.main.astdiff;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.util.TextUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyHttpServer {
    static final String DIVIDER = "--xxx---------------xxx";

    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(12007), 0);
        server.createContext("/DiffMiner/main", new TestHandler());
        server.start();
    }

    static class TestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("123");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            InputStream is = exchange.getRequestBody();
            byte[] cache = new byte[1000*1024];
            int res;
            StringBuilder postString = new StringBuilder();
            while ((res = is.read(cache)) != -1) {
                postString.append(new String(cache).substring(0, res));
            }
            System.out.println(postString.toString());

            //保存为文件
            String[] data = postString.toString().split(DIVIDER);
            if (data.length <= 1) {
                return;
            }
            int size = data.length;
            //找到meta信息
            Meta meta = FileUtil.filterMeta(data[size - 2]);
            //建立一个文件夹
            //文件夹命名为commit_hash
            //文件名以name字段的hash值
            File folder = FileUtil.createFolder("data/" + meta.getCommit_hash());
            //meta 文件
            FileUtil.createFile("meta", new Gson().toJson(meta), folder);
            //代码文件
            FileUtil.convertCodeToFile(data, folder);
            String response = "success";
            os.write(response.getBytes());
            os.close();
        }

    }
}