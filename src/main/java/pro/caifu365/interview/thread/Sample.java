package pro.caifu365.interview.thread;

import java.util.Map;

public class Sample {
    public static void main(String[] args) {
        Sample sample = new Sample();
        // sample.test1();
        // sample.test2();


        for (Map.Entry<Thread, StackTraceElement[]> stackTrace : Thread.getAllStackTraces().entrySet()) {
            Thread thread = (Thread) stackTrace.getKey();
            System.out.println("线程：" + thread.getName());
        }


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
                System.out.println(name + ":" + i);
            }
        }
    }

    public void test1() {
        RunnableImpl ri1 = new RunnableImpl();
        RunnableImpl ri2 = new RunnableImpl();
        Thread t1 = new Thread(ri1, "李白");
        Thread t2 = new Thread(ri2, "屈原");
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
            for (int i = 0; i < 5; i++) {
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

}