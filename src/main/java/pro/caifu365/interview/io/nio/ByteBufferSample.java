package pro.caifu365.interview.io.nio;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

public class ByteBufferSample {

    public static void main(String[] args) {
        ByteBufferSample sample = new ByteBufferSample();
        sample.cloneFile();

        sample.writeWithNewFile();
        sample.writeWithExistingFile();
    }

    public void cloneFile() {
        String fileName = "IMG_6370.JPG";
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
        var url = ByteBufferSample.class.getClassLoader().getResource(fileName);
        String fullFileName = url.getFile();
        String newFileName = this.getNewFileName(fullFileName);

        FileChannel ifc = null;
        FileChannel ofc = null;
        try {
            ifc = new FileInputStream(fullFileName).getChannel();
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
    }

    public void writeWithNewFile() {
        // 写文件
        FileChannel fc = null;
        try {
            String filePath = ByteBufferSample.class.getClassLoader().getResource("ByteBuffer_Sample_Data.txt").getPath();
            String newFileName = this.getNewFileName(filePath);

            FileOutputStream fos = new FileOutputStream(newFileName);
            fc = fos.getChannel();

            fc.write(ByteBuffer.wrap("New File".getBytes()));  //
            fc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeWithExistingFile() {
        // 写文件
        FileChannel fc = null;
        try {
            String filePath = ByteBufferSample.class.getClassLoader().getResource("ByteBuffer_Sample_Data.txt").getPath();
            fc = new RandomAccessFile(filePath, "rw").getChannel();
            fc.position(fc.size()); // Move to the end
            fc.write(ByteBuffer.wrap("Some more".getBytes()));
            fc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNewFileName(String fileName, String newTag) {
        String ext = FilenameUtils.getExtension(fileName);
        String newFileName = fileName.replace("." + ext, newTag + "." + ext);
        return newFileName;
    }

    private String getNewFileName(String fileName) {
        return getNewFileName(fileName, "_New");
    }
}
