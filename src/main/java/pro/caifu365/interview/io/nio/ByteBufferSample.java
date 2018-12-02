package pro.caifu365.interview.io.nio;

import org.apache.commons.io.FilenameUtils;

import java.io.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Date;

public class ByteBufferSample {

    public static void main(String[] args) {
        ByteBufferSample sample = new ByteBufferSample();
        sample.cloneFile();
        sample.writeWithNewFile();
        sample.writeWithExistingFile();
        sample.readFile1();
        sample.readFile2();
        sample.readFile3();

        // 大文件 copy
        MappedByteBufferSample mappedBuffer = new MappedByteBufferSample();

        mappedBuffer.copyFile1();
        mappedBuffer.copyFile2();
    }


    private String getNewFileName(String fileName, String newTag) {
        String ext = FilenameUtils.getExtension(fileName);
        String newFileName = fileName.replace("." + ext, newTag + "." + ext);
        return newFileName;
    }

    private String getNewFileName(String fileName) {
        return getNewFileName(fileName, "_New");
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

    public void readFile1() {
        // 写文件
        FileChannel fc = null;
        try {
            String filePath = ByteBufferSample.class.getClassLoader().getResource("ByteBuffer_Sample_Data.txt").getPath();
            fc = new RandomAccessFile(filePath, "rw").getChannel();

            ByteBuffer dst = ByteBuffer.allocate((int)fc.size());
            fc.read(dst);
            fc.close();

            System.out.println("读取1：");
            System.out.println("位置：" + dst.position());
            String txt = new String(dst.array(),"UTF-8");
            System.out.println("位置：" + dst.position());
            System.out.println(txt);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile2() {
        // 写文件
        FileChannel fc = null;
        try {
            String filePath = ByteBufferSample.class.getClassLoader().getResource("ByteBuffer_Sample_Data.txt").getPath();
            fc = new RandomAccessFile(filePath, "rw").getChannel();

            ByteBuffer dst = ByteBuffer.allocate(1024*1024);
            fc.read(dst);
            fc.close();



            System.out.println("读取后位置：" + dst.position());


            dst.flip();
            System.out.println("读取2：");
            System.out.println("flip之后位置：" + dst.position());

            while (dst.hasRemaining()){
                System.out.write(dst.get());
            }
            System.out.println("");
            System.out.println("get之后位置：" + dst.position());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile3() {
        MappedByteBuffer s;


        FileChannel fc = null;
        try {
            String filePath = ByteBufferSample.class.getClassLoader().getResource("ByteBuffer_Sample_Data.txt").getPath();
            fc = new RandomAccessFile(filePath, "rw").getChannel();
            ByteBuffer dst = ByteBuffer.allocate(1024 * 1024);
            fc.read(dst);
            fc.close();

            dst.flip();
            CharBuffer charBuffer = Charset.forName("utf-8").decode(dst);
            System.out.println("读取3：");
            while(charBuffer.hasRemaining())
            {
                System.out.print(charBuffer.get());
            }
            System.out.println();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}
