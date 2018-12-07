package pro.caifu365.interview.thread;

import sun.misc.Unsafe;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanTest implements Runnable{
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    public static AtomicBoolean exits = new AtomicBoolean(true);
    public static void main(String[] args) {
        AtomicBooleanTest abd = new AtomicBooleanTest();
        Thread t1 = new Thread(abd);
        Thread t2 = new Thread(abd);
        t1.start();
        t2.start();


    }

    @Override
    public void run() {

        System.out.println("begin run");
        System.out.println("real " + exits.get());
        if(exits.compareAndSet(true,false)){
            System.out.println(Thread.currentThread().getName() + "  " + exits.get() );
            exits.set(true);
        }else{
            run();
        }
    }


}
