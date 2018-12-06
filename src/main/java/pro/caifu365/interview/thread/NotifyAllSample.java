package pro.caifu365.interview.thread;

public class NotifyAllSample {
    class Calculator extends Thread {
        int total;
        @Override
        public void run() {
            synchronized (this) {
                for(int i=0;i<101;i++){
                    total+=i;
                }
            }
            //通知所有在此对象上等待的线程
            notifyAll();
        }
    }

    class ReaderResult extends Thread {
        Calculator c;
        public ReaderResult(Calculator c) {
            this.c = c;
        }
        public void run(){
            synchronized (c) {
                try {
                    System.out.println(Thread.currentThread() + "等待计算结果......");
                    c.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread()+ "计算结果为：" + c.total);
            }
        }
    }

    private void exec()
    {
        Calculator calculator=new Calculator();
        //启动三个线程，分别获取计算结果
        new ReaderResult(calculator).start();
        new ReaderResult(calculator).start();
        new ReaderResult(calculator).start();
        //启动计算线程
        calculator.start();
    }

    public static void main(String[] args) {
        NotifyAllSample notifyAllSample = new NotifyAllSample();
        notifyAllSample.exec();
    }

}
