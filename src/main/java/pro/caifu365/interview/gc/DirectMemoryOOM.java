package pro.caifu365.interview.gc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * VM Args:-Xmx20M -XX:MaxDirectMemorySize=10M
 *
 * @author zzm
 */
public class DirectMemoryOOM {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        // Unsafe unsafe = Unsafe.getUnsafe();
        for (int i = 0; i < 100000; i++) {
            unsafe.allocateMemory(_1MB);
            System.out.println(i);
        }
    }
}
