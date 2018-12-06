package pro.caifu365.interview.thread;

import java.util.concurrent.*;

public class CallableTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableTest test = new CallableTest();
        test.exec();
    }

    class MyCallable implements Callable {
        private String oid;

        MyCallable(String oid) {
            this.oid = oid;
        }

        @Override
        public Object call() throws Exception {
            return oid + "任务返回的内容";

        }
    }

    public void exec() throws ExecutionException, InterruptedException {



        //创建一个线程池
        ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        //创建两个有返回值的任务
        Callable c1 = new MyCallable("A");
        Callable c2 = new MyCallable("B");
        //执行任务并获取Future对象
        Future f1 = pool.submit(c1);
        Future f2 = pool.submit(c2);
        //从Future对象上获取任务的返回值，并输出到控制台
        System.out.println(">>>" + f1.get().toString());
        System.out.println(">>>" + f2.get().toString());
        //关闭线程池
        pool.shutdown();
    }
}
