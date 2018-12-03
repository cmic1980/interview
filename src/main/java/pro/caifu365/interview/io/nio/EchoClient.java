package pro.caifu365.interview.io.nio;

import pro.caifu365.interview.io.commons.ServerInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoClient {
    public static void main(String[] args) {
        SocketChannel socketChannel = null;

        EchoClient echoClient = new EchoClient();
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
            channel.register(selector, SelectionKey.OP_CONNECT);
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
        // 连接就绪
        try {
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

    private static void handelReadable(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int temp = sc.read(buffer); // 从channel读到buffer
        String content = "来自服务端的: ";
        if (temp > 0) {// 代表读完毕了,准备写(即打印出来)
            buffer.flip(); // 为write()准备
            // =====取出buffer里的数据
            byte[] bytes = new byte[buffer.remaining()]; // 创建字节数组
            buffer.get(bytes);// 将数据取出放到字节数组里
            content += new String(bytes);
            content += "============";
            System.out.println(content);
        }
    }

    private static void handleConnectable(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        if (sc.finishConnect()) {
            // 将关注的事件变成read
            sc.register(selector, SelectionKey.OP_READ);
            String lineSeparator = System.getProperty("line.separator", "\n");
            doWrite(sc, "Hello world" + lineSeparator);
        }
    }

    private static void doWrite(SocketChannel sc, String data) throws IOException {
        byte[] req = data.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(req);
        byteBuffer.put(req);
        byteBuffer.flip();
        sc.write(byteBuffer);
    }
}
