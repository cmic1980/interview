package pro.caifu365.interview.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {

    public static void main(String[] args) {
        AtomicTest atomicTest = new AtomicTest();
        atomicTest.exc1();
    }

    class Test1 {
        private long count = 0;
        //若要线程安全执行执行count++，需要加锁
        public void increment() {
            count++;
            System.out.println("AtomicInteger is：" + count);
        }

        public long getCount() {
            return count;
        }
    }

    class Test2 {
        private AtomicInteger count = new AtomicInteger();

        public void increment() {
            count.incrementAndGet();
            System.out.println("AtomicInteger is：" + count.get());
        }
        //使用AtomicInteger之后，不需要加锁，也可以实现线程安全。
        public int getCount() {
            return count.get();
        }
    }

    public void exc1()
    {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Test1 test1 = new Test1();
        for (int i =0;i<100;i++)
        {
            Thread t1 = new Thread(()->{
                test1.increment();
            });
            executorService.execute(t1);
        }
        executorService.shutdown();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("abc is：" + test1.getCount());
    }

    public void exc2()
    {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Test2 test2 = new Test2();
        Thread t1 = new Thread(()->{
            test2.increment();
        });
        Thread t2 = new Thread(()->{
            test2.increment();
        });
        Thread t3 = new Thread(()->{
            test2.increment();
        });
        Thread t4 = new Thread(()->{
            test2.increment();
        });
        Thread t5 = new Thread(()->{
            test2.increment();
        });

        executorService.execute(t1);
        executorService.execute(t2);
        executorService.execute(t3);
        executorService.execute(t4);
        executorService.execute(t5);
        executorService.shutdown();
    }
}
