package pro.caifu365.interview.process;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Consumer extends Thread {
    private String fileName;
    private FileChannel switchFileChannel;
    private MappedByteBuffer mappedByteBuffer;

    public Consumer(String fileName) {
        try {
            fileName = fileName;

            // 获得一个可读写的随机存取文件对象
            RandomAccessFile switchFile = new RandomAccessFile(fileName, "r");

            // 获得相应的文件通道
            switchFileChannel = switchFile.getChannel();

            // 取得文件的实际大小，以便映像到共享内存
            int size = (int) switchFileChannel.size();

            // 获得共享内存缓冲区，该共享内存可读
            mappedByteBuffer = switchFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(300);
                FileLock lock = null;
                lock = switchFileChannel.tryLock(0, 10, true);
                if (lock == null) {
                    System.err.println("Consumer: lock failed");
                    continue;
                }
                Thread.sleep(200);



                System.out.println("Consumer: " + mappedByteBuffer.getChar(0) + ":" + mappedByteBuffer.getChar(4) + ":" + mappedByteBuffer.getChar(8));
                lock.release();
            } catch (IOException ex) {
                System.out.print(ex);
            } catch (InterruptedException ex) {
                System.out.print(ex);
            }
        }
    }

    public static void main(String args[]) {
        Consumer consumer = new Consumer("sharedMemory.bin");
        consumer.start();
    }
}
