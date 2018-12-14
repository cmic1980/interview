package pro.caifu365.interview.gc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LockThread {

    public static void createLockThread(final Object lock) {
        Thread thread = new Thread(() -> {
            synchronized (lock) {

                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public static void createBusyThread(){
        Thread thread = new Thread(() -> {
            while (true){
                ;
            }
        });
        thread.start();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        createBusyThread();

        br.readLine();
        Object obj=new Object();
        createLockThread(obj);
    }
}
