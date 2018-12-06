package pro.caifu365.interview.thread;

import java.util.concurrent.*;

public class ExecutorTest {
    public static void main(String[] args) {
        ExecutorTest executorTest = new ExecutorTest();
        // executorTest.testFixedThreadPool();
        // executorTest.testSingleThreadPool();
        // executorTest.testCachedThreadPool();
        // executorTest.testScheduledThreadPool();
        // executorTest.testSingleScheduledThreadPool();
        executorTest.testCustomizedThreadPool();
    }

    class MyThread extends Thread {
        public void run() {
            System.out.println(Thread.currentThread().getName() + "正在执行...");
        }
    }

    public void testFixedThreadPool() {
        //创建一个可重用固定线程数的线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
        Thread t1 = new MyThread();
        Thread t2 = new MyThread();
        Thread t3 = new MyThread();
        Thread t4 = new MyThread();
        Thread t5 = new MyThread();
        //将线程放入线程池中进行执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        //关闭线程池
        pool.shutdown();
    }

    public void testSingleThreadPool() {
        //创建一个可重用固定线程数的线程池
        ExecutorService pool = Executors.newSingleThreadExecutor();
        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
        Thread t1 = new MyThread();
        Thread t2 = new MyThread();
        Thread t3 = new MyThread();
        Thread t4 = new MyThread();
        Thread t5 = new MyThread();
        //将线程放入线程池中进行执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        //关闭线程池
        pool.shutdown();
    }

    public void testCachedThreadPool() {
        // 创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。
        ExecutorService pool = Executors.newCachedThreadPool();
        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
        Thread t1 = new MyThread();
        Thread t2 = new MyThread();
        Thread t3 = new MyThread();
        Thread t4 = new MyThread();
        Thread t5 = new MyThread();
        //将线程放入线程池中进行执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        //关闭线程池
        pool.shutdown();
    }

    public void testScheduledThreadPool() {
        // 创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
        Thread t1 = new MyThread();
        Thread t2 = new MyThread();
        Thread t3 = new MyThread();
        Thread t4 = new MyThread();
        Thread t5 = new MyThread();
        //将线程放入线程池中进行执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        //使用延迟执行风格的方法
        pool.schedule(t4, 2, TimeUnit.SECONDS);
        pool.schedule(t5, 2, TimeUnit.SECONDS);
        //关闭线程池
        pool.shutdown();
    }

    public void testSingleScheduledThreadPool() {
        // 创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。
        ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
        Thread t1 = new MyThread();
        Thread t2 = new MyThread();
        Thread t3 = new MyThread();
        Thread t4 = new MyThread();
        Thread t5 = new MyThread();
        //将线程放入线程池中进行执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        //使用延迟执行风格的方法
        pool.schedule(t4, 2, TimeUnit.SECONDS);
        pool.schedule(t5, 2, TimeUnit.SECONDS);
        //关闭线程池
        pool.shutdown();
    }

    public void testCustomizedThreadPool() {
        //创建等待队列
        BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(20);
        //创建一个单线程执行任务，它可安排在给定延迟后运行命令或者定期地执行。
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 3, 20, TimeUnit.SECONDS, bqueue);
        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
        Thread t1 = new DelayThread();
        Thread t2 = new DelayThread();
        Thread t3 = new DelayThread();
        Thread t4 = new DelayThread();
        Thread t5 = new DelayThread();
        Thread t6 = new DelayThread();
        Thread t7 = new DelayThread();
        //将线程放入线程池中进行执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        pool.execute(t6);
        pool.execute(t7);

        //关闭线程池
        pool.shutdown();
    }

    class DelayThread extends Thread {
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "正在执行...");
        }
    }
}
