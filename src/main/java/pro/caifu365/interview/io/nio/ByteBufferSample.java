package pro.caifu365.interview.io.nio;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

public class ByteBufferSample {
    private final static String PIC_FILE_NAME = "IMG_6370.JPG";

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024);
        var url = ByteBufferSample.class.getClassLoader().getResource(PIC_FILE_NAME);
        String fileName = url.getFile();


        FileChannel ifc = null;
        FileChannel ofc = null;
        try {
            ifc = new FileInputStream(fileName).getChannel();
            String ext = FilenameUtils.getExtension(PIC_FILE_NAME);
            String newFileName = fileName.replace("." + ext, "_New." + ext);

            ofc = new FileOutputStream(newFileName).getChannel();

            int totalSize = 0;
            int bufferSize = 0;

            Date start = new Date();

            while (true) {
                bufferSize = ifc.read(byteBuffer);
                if (bufferSize == -1) {
                    break;
                }
                totalSize = totalSize + bufferSize;

                byteBuffer.flip();
                ofc.write(byteBuffer);
                byteBuffer.clear();
            }

            Date end = new Date();
            System.out.println(end.getTime() - start.getTime());
            System.out.println(totalSize);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ifc != null) {
                    ifc.close();
                }

                if (ofc != null) {
                    ofc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ;
    }
}
