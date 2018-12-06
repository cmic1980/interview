package pro.caifu365.interview.thread;

public class FooRunnable implements Runnable {
    private Foo foo = new Foo();

    public static void main(String[] args) {
        FooRunnable r = new FooRunnable();
        Thread ta = new Thread(r, "Thread-A");
        Thread tb = new Thread(r, "Thread-B");
        ta.start();
        tb.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            this.fix(30);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " :当前foo对象的x值= " + foo.getX());
        }
    }

    public int fix(int y) {
        return foo.fix(y);
    }

    public class Foo {
        private int x = 100;

        public int getX() {
            return x;
        }

        public int fix(int y) {
            x = x - y;
            return x;
        }
    }
}
