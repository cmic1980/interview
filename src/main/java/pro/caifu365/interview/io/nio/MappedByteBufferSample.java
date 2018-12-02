package pro.caifu365.interview.io.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferSample {

    public void copyFile1() {
        long timeStar = System.currentTimeMillis();// 得到当前的时间

        FileChannel fic = null;
        FileChannel foc = null;
        try {
            fic = new FileInputStream("d:/video/2/in mad[atid]/atid239/1229-atid239.avi").getChannel();
            foc = new FileOutputStream("d:/video/2/in mad[atid]/atid239/1229-atid239-1.avi").getChannel();
            MappedByteBuffer src = fic.map(FileChannel.MapMode.READ_ONLY, 0, fic.size());
            foc.write(src);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fic != null) {
                    fic.close();
                }
                if (foc != null) {
                    foc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long timeEnd = System.currentTimeMillis();// 得到当前的时间

        System.out.println("MappedByteBufferSample write time：" + (timeEnd - timeStar) + "ms");
    }

    public void copyFile2() {
        long timeStar = System.currentTimeMillis();// 得到当前的时间

        ByteBuffer byteBuffer = ByteBuffer.allocate(10 * 1024 * 1024);

        FileChannel fic = null;
        FileChannel foc = null;
        try {
            fic = new FileInputStream("d:/video/2/in mad[atid]/atid239/1229-atid239.avi").getChannel();
            foc = new FileOutputStream("d:/video/2/in mad[atid]/atid239/1229-atid239-2.avi").getChannel();

            while (fic.read(byteBuffer) != -1) {
                byteBuffer.flip();
                foc.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fic != null) {
                    fic.close();
                }
                if (foc != null) {
                    foc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long timeEnd = System.currentTimeMillis();// 得到当前的时间

        System.out.println("ByteBuffer write time :" + (timeEnd - timeStar) + "ms");
    }

}
