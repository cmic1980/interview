package pro.caifu365.interview.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessSample {
    public static void main(String[] args) {
        // runProcessWithBuilder();
        runProcessWithRuntime();
    }

    // 使用ProcessBuilder 运行 exe 并打印结果
    private static void runProcessWithBuilder() {
        try {
            List<String> list = new ArrayList<String>();
            ProcessBuilder pb = null;
            Process p = null;
            String line = null;
            BufferedReader stdout = null;

            //list the files and directorys under C:\
            // 命令行 cmd /c dir
            list.add("CMD");
            list.add("/C");
            list.add("dir");
            pb = new ProcessBuilder(list);
            pb.directory(new File("C:\\"));
            p = pb.start();

            stdout = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
            }
            stdout.close();

            //echo the value of NAME
            pb = new ProcessBuilder();
            pb.command(new String[]{"CMD.exe", "/C", "echo %NAME%"});
            pb.environment().put("NAME", "TEST");
            p = pb.start();

            stdout = new BufferedReader(new InputStreamReader(p
                    .getInputStream(), "GBK"));
            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
            }
            stdout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 使用Runtime  运行 exe 并打印结果
    private static void runProcessWithRuntime() {
        try {
            Process p = null;
            String line = null;
            BufferedReader stdout = null;

            //list the files and directorys under C:\
            p = Runtime.getRuntime().exec("CMD.exe /C dir", null, new File("C:\\"));
            stdout = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
            }
            stdout.close();

            //echo the value of NAME
            p = Runtime.getRuntime().exec("CMD.exe /C echo %NAME%", new String[]{"NAME=TEST"});
            stdout = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
            }
            stdout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
