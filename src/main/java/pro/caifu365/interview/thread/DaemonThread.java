package pro.caifu365.interview.thread;

public class DaemonThread {
    public static void main(String[] args) {
        DaemonThread daemonThread = new DaemonThread();
        daemonThread.exec();
    }

    public void exec(){
        Thread t1=new MyCommon();
        Thread t2=new Thread(new MyDaemon());
        t2.setDaemon(true);//设置为守护线程
        t2.checkAccess();
        t2.start();
        t1.start();
    }

    class MyCommon extends Thread{
        @Override
        public void run() {
            for(int i=0;i<30;i++){
                System.out.println("线程1第"+i+"次执行！");
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyDaemon implements Runnable{
        @Override
        public void run() {
            for (long i = 0; i < 9999999L; i++) {
                System.out.println("后台线程第" + i +"次执行！");
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
