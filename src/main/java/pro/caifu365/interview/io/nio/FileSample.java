package pro.caifu365.interview.io.nio;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;

import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class FileSample {
    public static void main(String[] args) {
        // 连接文件路径
        Path target = Paths.get("d:/video/2/misc","QC-006.XviD.avi");
        File file = target.toFile();

        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-rw-rw-");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);

        try {
            FileStore fileStore = Files.getFileStore(target);
            long l = fileStore.getTotalSpace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteBuffer buffer1 = ByteBuffer.allocate(1024);
        ByteBuffer buffer2 = ByteBuffer.allocateDirect(1024);
        buffer1 = null;



    }
}
