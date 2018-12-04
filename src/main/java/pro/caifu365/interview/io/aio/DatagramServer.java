package pro.caifu365.interview.io.aio;

import pro.caifu365.interview.io.commons.ServerInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;

public class DatagramServer {

    public static void main(String[] args) throws IOException {
        // 获取通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        // 绑定端口
        datagramChannel.bind(new InetSocketAddress(ServerInfo.SERVER_PORT));
        // 分配Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // byte[] bytes;
        while (true) {
            // 清空Buffer
            buffer.clear();
            // 接受客户端发送数据
            SocketAddress socketAddress = datagramChannel.receive(buffer);
            if (socketAddress != null) {
              /*  int position = buffer.position();
                bytes = new byte[position];
                buffer.flip();
                for(int i=0; i<position; ++i) {
                    bytes[i] = buffer.get();
                }*/

                buffer.flip();
                if (buffer.hasRemaining()) {
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    System.out.println("receive client message：" + socketAddress.toString() + ":" + new String(bytes, "UTF-8"));
                }

                //接收到消息后给发送方回应
                sendCallback(socketAddress, datagramChannel);
            }
        }
    }

    public static void sendCallback(SocketAddress socketAddress, DatagramChannel datagramChannel) throws IOException {
        String message = "I has receive your message";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(message.getBytes("UTF-8"));
        buffer.flip();
        datagramChannel.send(buffer, socketAddress);
    }

}
