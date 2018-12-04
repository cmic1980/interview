package pro.caifu365.interview.io.bio;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipeSample {

    public static void main(String[] args) {

        Sender sender = new Sender();
        Receiver receiver = new Receiver();
        PipedInputStream pi = receiver.getPipedInputStream();
        PipedOutputStream po = sender.getPipedOutputStream();
        try {
            pi.connect(po);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        new Thread(sender).start();
        new Thread(receiver).start();

    }
}

class Sender implements Runnable {
    PipedOutputStream out = null;

    public PipedOutputStream getPipedOutputStream() {
        out = new PipedOutputStream();
        return out;
    }

    @Override
    public void run() {

        try {
            out.write("Hello , Receiver!".getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}

class Receiver implements Runnable {
    PipedInputStream in = null;

    public PipedInputStream getPipedInputStream() {
        in = new PipedInputStream();
        return in;
    }

    @Override
    public void run() {

        byte[] bys = new byte[1024];
        try {
            in.read(bys);
            System.out.println("读取到的信息：" + new String(bys).trim());
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
