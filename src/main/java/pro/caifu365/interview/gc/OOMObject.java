package pro.caifu365.interview.gc;

import java.util.ArrayList;
import java.util.List;


/**
 * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -Xlog:gc* -XX:+UseParallelGC
 * -Xlog:gc*
 * <p>
 * -verbose:gc -Xms100M -Xmx100M -Xlog:gc*
 */

public class OOMObject {
    public byte[] placeholder = new byte[64 * 1024];

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<OOMObject>();
        for (int i = 0; i < num; i++) {
            //稍作延时，令监视曲线的变化更加明显
            Thread.sleep(200);
            list.add(new OOMObject());
        }
        System.gc();
    }

    public static void main(String[] args) throws InterruptedException {
        fillHeap(1000);
    }
}
