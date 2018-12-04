package pro.caifu365.interview.io.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Selector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PipeSample {

    class Sender implements Runnable {
        private Pipe.SinkChannel sinkChannel;

        Sender(Pipe.SinkChannel sinkChannel) {
            this.sinkChannel = sinkChannel;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.wrap("1234567890".getBytes());
            // 2.3 将数据写入到管道
            try {
                this.sinkChannel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("发送数据完成 ...");
            System.out.println("1");
        }
    }

    class Receiver implements Runnable {
        private Pipe.SourceChannel sourceChannel;

        Receiver(Pipe.SourceChannel sourceChannel) {
            this.sourceChannel = sourceChannel;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);
            try {
                this.sourceChannel.read(buffer);
                buffer.flip();
                byte[] bytes = buffer.array();

                String content = new String(bytes);
                System.out.println("接收到数据：" + content);
                System.out.println("2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        Pipe pipe = null;
        try {
            pipe = Pipe.open();

            Executor executor = Executors.newCachedThreadPool();
            executor.execute(new Sender(pipe.sink()));
            executor.execute(new Receiver(pipe.source()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PipeSample pipeSample = new PipeSample();
        pipeSample.start();
    }

}
