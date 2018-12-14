package pro.caifu365.interview.gc;

public class M3 {
    private static final int _1MB = 1024 * 1024;


    public static void main(String[] args) {
        testAllocation();
        // testPretenureSizeThreshold();
        // testTenuringThreshold();
    }


    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -Xlog:gc* -XX:+UseParallelGC
     * -Xlog:gc*
     * <p>
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -Xlog:gc*
     */
    public static void testAllocation() {
        byte[] allocation1, allocation2, allocation3, allocation4, allocation5, allocation6;

        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        // allocation4 = new byte[2 * _1MB];
        // allocation5 = new byte[2 * _1MB];
        // allocation6 = new byte[2 * _1MB];

        //出现一次Minor GC

        // System.out.println("Minor GC 1");
    }

    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -Xlog:gc* -XX:SurvivorRatio=8 -XX:+UseParallelGC
     * -Xlog:gc*
     * <p>
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -Xlog:gc* -XX:SurvivorRatio=8
     */
    public static void testPretenureSizeThreshold() {
        var l1 = new byte[4 * _1MB]; //直接分配在老年代中

    }

    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -Xlog:gc* -XX:SurvivorRatio=8 -XX:+UseParallelGC
     * -Xlog:gc*
     * <p>
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -Xlog:gc* -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
     */
    public static void testTenuringThreshold() {
        byte[] allocation1, allocation2, allocation3;
        allocation1 = new byte[_1MB / 4];
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }
}
