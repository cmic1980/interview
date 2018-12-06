package pro.caifu365.interview.thread;

public class WaitSample {

    public static void main(String[] args) {
        WaitSample waitSample = new WaitSample();
        waitSample.exec();
    }

    class ThreadB extends Thread {
        int total;

        public void run() {
            synchronized (this) {
                for (int i = 0; i < 101; i++) {
                    total += i;
                }
                //（完成计算了）唤醒在此对象监视器上等待的单个线程，在本例中线程A被唤醒
                notify();
            }
        }

        public int getTotal() {
            return this.total;
        }
    }

    public void exec() {
        ThreadB b = new ThreadB();
        //启动计算线程
        b.start();
        //线程A拥有b对象上的锁。线程为了调用wait()或notify()方法，该线程必须是那个对象锁的拥有者
        synchronized (b) {
            try {
                System.out.println("等待对象b完成计算......");
                b.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("b对象计算的总和是：" + b.getTotal());
        }
    }
}
