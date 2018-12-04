package pro.caifu365.interview.io.aio;

import pro.caifu365.interview.io.commons.ServerInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutionException;

public class AIOEchoServer {
    private AsynchronousServerSocketChannel serverChannel;

    class AIOEchoServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

        private AsynchronousServerSocketChannel serverChannel;
        private ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 8);

        public AIOEchoServerCompletionHandler(AsynchronousServerSocketChannel serverChannel) {
            this.serverChannel = serverChannel;
        }

        @Override
        public void completed(AsynchronousSocketChannel result, Void attachment) {
            //立即接收下一个请求,不停顿
            this.serverChannel.accept(null, this);


            try {
                while (result.read(buffer).get() != -1) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    buffer.flip();
                    if (buffer.hasRemaining()) {
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);// 将数据取出放到字节数组里
                        bos.write(bytes);
                    }
                    buffer.clear();

                    String content = bos.toString();
                    System.out.println("Client Request Message：" + content);
                    bos.close();

                    // 回写客户端接收消息
                    ByteBuffer outBuffer = ByteBuffer.wrap("Request Received!".getBytes());
                    result.write(outBuffer).get();
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {

                    result.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            //立即接收下一个请求,不停顿
            serverChannel.accept(null, this);
            throw new RuntimeException("connection failed!");
        }
    }


    public void init() throws IOException {
        this.serverChannel = AsynchronousServerSocketChannel.open();
        if (serverChannel.isOpen()) {
            serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 8 * 1024);
            serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverChannel.bind(new InetSocketAddress(ServerInfo.SERVER_PORT));
        } else {
            throw new RuntimeException("Channel not opened!");
        }
    }

    public void start() throws InterruptedException {
        System.out.println("Wait for Client ...");
        this.serverChannel.accept(null, new AIOEchoServerCompletionHandler(serverChannel));
        while (true) {
            Thread.sleep(5000);
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        AIOEchoServer server = new AIOEchoServer();

        server.init();
        server.start();

    }

}
