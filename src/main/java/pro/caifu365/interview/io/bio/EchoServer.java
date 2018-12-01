package pro.caifu365.interview.io.bio;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class EchoServer implements AutoCloseable {
    private final static int SERVER_PORT = 9001;
    private final static String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private ServerSocket serverSocket = null;

    private EchoServer(int port) {
        try {
            this.serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EchoServer build() {
        EchoServer echoServer = new EchoServer(SERVER_PORT);
        return echoServer;
    }

    public void start() {
        System.out.println("ECHO服务器端已经启动了，该服务在" + this.serverSocket.getLocalPort() + "端口上监听....");
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            var socket = this.serverSocket.accept();//阻塞方法
            System.out.println("连接成功" + socket.getRemoteSocketAddress());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "GBK"));

            while (true) {
                String inputData = br.readLine();
                pw.println("【ECHO】 " + inputData);
                pw.flush();
            }
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

    @Override
    public void close() throws Exception {
        this.serverSocket.close();
    }

    public static void main(String[] args) {
        EchoServer.build().start();
    }
}
