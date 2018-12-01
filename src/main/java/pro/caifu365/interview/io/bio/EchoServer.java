package pro.caifu365.interview.io.bio;


import org.apache.commons.lang3.StringUtils;
import pro.caifu365.interview.io.commons.ServerInfo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class EchoServer implements AutoCloseable {
    private static volatile ServerSocket serverSocket = null;

    private EchoServer(int port) {
        try {
            synchronized (this)
            {
                if (serverSocket == null) {
                    serverSocket = new ServerSocket(port);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runServer() {
        System.out.println("ECHO服务器端已经启动了，该服务在" + serverSocket.getLocalPort() + "端口上监听....");
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            var socket = serverSocket.accept();//阻塞方法
            System.out.println("连接成功" + socket.getRemoteSocketAddress());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));


            while (true) {
                String inputData = br.readLine();
                if (StringUtils.isNotEmpty(inputData)) {
                    if (inputData.equalsIgnoreCase("exit")) {
                        pw.println("【ECHO】Bye Bye ... kiss");
                        pw.flush();
                        break;
                    } else {
                        pw.println("【ECHO】 " + inputData);
                        pw.flush();
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (br != null) {
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void start(int port) {
        EchoServer echoServer = new EchoServer(port);
        Thread thread = new Thread(() -> {
            echoServer.runServer();
        });
        thread.start();
    }

    @Override
    public void close() throws Exception {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    public static void main(String[] args) {
        EchoServer.start(ServerInfo.SERVER_PORT);
    }
}
