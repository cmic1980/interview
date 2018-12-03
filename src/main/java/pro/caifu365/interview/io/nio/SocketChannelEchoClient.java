package pro.caifu365.interview.io.nio;

import org.apache.commons.lang3.ArrayUtils;
import pro.caifu365.interview.io.commons.ServerInfo;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.sql.Array;
import java.util.*;

public class SocketChannelEchoClient {
    private String lineSeparator = System.getProperty("line.separator", "\n");

    public static void main(String[] args) {
        SocketChannel socketChannel = null;

        SocketChannelEchoClient echoClient = new SocketChannelEchoClient();
        echoClient.init();
    }

    private static Selector selector = null;
    private volatile static boolean stop = false;
    private static SocketChannel channel = null;

    public void init() {
        initSelector();// 初始化selector
        initSocketChannel(); // 初始化serverSocketChannel
        run();
    }

    // 初始化selector
    public void initSelector() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 初始化SocketChannel
    public void initSocketChannel() {
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(ServerInfo.SERVER_HOST, ServerInfo.SERVER_PORT));
            channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (ClosedChannelException e) {
            System.out.println("client: 失去主机连接");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (!stop) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    handle(key);
                    iterator.remove();
                }

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void handle(SelectionKey key) throws IOException {
        try {
            // 连接就绪
            if (key.isConnectable()) {
                handleConnectable(key);
            }
            // 读就绪
            if (key.isReadable()) {
                handelReadable(key);
            }
        } catch (Exception e) {
            key.cancel();
            if (key.channel() != null) {
                try {
                    key.channel().close();
                } catch (IOException e1) {

                }
            }
        }
    }

    private void handelReadable(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();

        StringBuilder stringBuilder = new StringBuilder("来自服务端的：");
        ByteBuffer buffer = ByteBuffer.allocate(30);

        int position = sc.read(buffer); // 从channel读到buffer
        String content = "来自服务端的: ";
        byte[] allBytes = null;
        while (position != 0) {// 代表读完毕了,准备写(即打印出来)
            buffer.flip();
            if (allBytes == null) {
                allBytes = buffer.array().clone();
            } else {
                // =====取出buffer里的数据
                byte[] readBytes = new byte[buffer.remaining()]; // 创建字节数组
                buffer.get(readBytes);
                allBytes = ArrayUtils.addAll(allBytes, readBytes);
            }

            buffer.clear();
            // buffer = ByteBuffer.allocate(10);
            position = sc.read(buffer);
        }

        String s = new String(allBytes);
        System.out.println(s);
        inputHandle(sc);
    }

    private void handleConnectable(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        if (sc.finishConnect()) {
            // 将关注的事件变成read
            sc.register(selector, SelectionKey.OP_READ);
            doWrite(sc, "Hello Server");
        }
    }

    private void doWrite(SocketChannel sc, String data) throws IOException {

        data = data + lineSeparator;

        byte[] req = data.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(req);
        byteBuffer.put(req);
        byteBuffer.flip();
        sc.write(byteBuffer);
    }

    private void inputHandle(SocketChannel sc) {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        System.out.println("请输入要发送的信息：");
        String input = scanner.next();
        try {
            doWrite(sc, input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
