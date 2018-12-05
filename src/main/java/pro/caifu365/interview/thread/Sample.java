package pro.caifu365.interview.thread;

import java.util.Map;

public class Sample {
    public static void main(String[] args) {
        Sample sample = new Sample();
        // sample.test1();
        // sample.test2();
        sample.test3();

        /*for (Map.Entry<Thread, StackTraceElement[]> stackTrace : Thread.getAllStackTraces().entrySet()) {
            Thread thread = (Thread) stackTrace.getKey();
            System.out.println("线程：" + thread.getName());
        }*/

/*        System.out.println("主线程名字：" + Thread.currentThread().getName());
        System.out.println("主线程ID：" + Thread.currentThread().getId());

        new Thread(()->{
            System.out.println("子线程名字：" + Thread.currentThread().getName());
            System.out.println("子线程ID：" + Thread.currentThread().getId());
        }).start();*/
    }

    // 1、实现Runnable接口的多线程例子
    public class RunnableImpl implements Runnable {
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            for (int i = 0; i < 5; i++) {
                try {
                    // 静态方法强制当前正在执行的线程休眠
                    // 当线程睡眠时，它入睡在某个地方，在苏醒之前不会返回到可运行状态。当睡眠时间到期，则返回到可运行状态。
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + ":" + i);
            }
        }
    }

    public void test1() {
        RunnableImpl ri1 = new RunnableImpl();
        RunnableImpl ri2 = new RunnableImpl();
        Thread t1 = new Thread(ri1, "蔡颖");
        Thread t2 = new Thread(ri2, "何苗");
        t1.start();
        t2.start();
    }


    //   2、扩展Thread类实现的多线程例子
    class TestThread extends Thread {
        public TestThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.println(this.getName() + ":" + i);
            }
        }
    }

    public void test2() {
        Thread t1 = new TestThread("李白");
        Thread t2 = new TestThread("屈原");
        t1.start();
        t2.start();
    }

    class JoinThread extends Thread {
        private Thread joinThread;
        public JoinThread(String name, Thread joinThread) {
            super(name);

            this.joinThread = joinThread;
        }

        @Override
        public void run() {

            try {
                joinThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 100; i++) {
                System.out.println(this.getName() + ":" + i);
            }
        }
    }

    public void test3() {
        Thread t1 = new TestThread("李白");
        Thread t2 = new JoinThread("屈原",t1);
        t1.start();
        t2.start();
    }
}