package pro.caifu365.interview.io.bio;

import pro.caifu365.interview.io.commons.ServerInfo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) {
        EchoClient.start();
    }

    public static void start(){
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            Socket socket = new Socket(ServerInfo.SERVER_HOST, ServerInfo.SERVER_PORT);
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("请输入要发送的数据信息：");
                String input = scanner.next();
                pw.println(input);
                pw.flush();

                String result = br.readLine();
                System.out.println(result);
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
}
