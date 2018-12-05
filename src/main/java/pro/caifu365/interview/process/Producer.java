package pro.caifu365.interview.process;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Producer extends Thread {
    private FileChannel switchFileChannel;
    private MappedByteBuffer mappedByteBuffer;

    public Producer(String fileName) {
        try {
            // 获得一个可读写的随机存取文件对象
            RandomAccessFile switchFile = new RandomAccessFile(fileName , "rw");

            // 获得相应的文件通道
            switchFileChannel = switchFile.getChannel();

            // 取得文件的实际大小，以便映像到共享内存
            int size = (int) switchFileChannel.size();

            // 获得共享内存缓冲区，该共享内存可读
            mappedByteBuffer = switchFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, size).load();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        int i = 0;
        while (true) {
            try {
                FileLock lock = null;
                lock = switchFileChannel.tryLock();
                if (lock == null) {
                    System.err.println("Producer: lock failed");
                    continue;
                }


                mappedByteBuffer.putChar(0,'A');
                mappedByteBuffer.putChar(4,'B');
                mappedByteBuffer.putChar(8,'C');
                // System.out.println("Producer: " + (i - 3) + ":" + (i - 2) + ":" + (i - 1));
                Thread.sleep(200);
                lock.release();
                Thread.sleep(500);
            } catch (IOException ex) {
                System.out.print(ex);
            } catch (InterruptedException ex) {
                System.out.print(ex);
            }
        }

    }

    public static void main(String args[]) {

        System.out.println("user.home : "+System.getProperty("user.home"));
        Producer producer = new Producer("sharedMemory.bin");
        producer.start();
    }

}