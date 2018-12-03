# NIO 使用

参考： http://ifeve.com/java-nio-vs-io/




## 1. 通道和缓冲器
### 1.1 简介
java.nio.*包的引入是为了提高速度，并且旧的IO包已经用nio重新实现过，所以即使你不用nio，也已经收益了

下面的格式可能比较乱，需要配合GetChannel例子来理解

- 如何理解：
    - 你要读数据的地方，可能说的是IO流，可以看做一个煤矿，煤就是数据
    - 通道：Channel，包含煤层的矿藏，就是一个煤矿里有煤的地方
    - 缓冲器：ByteBuffer，可以看做运煤的车，注意这里装车和卸车也是有意义的动作
    - 我们能做的就是派运煤的车去通道，也就是将缓冲器ByteBuffer和Channel连起来，往里送煤（写），往外运煤（读）
    - ——-缓一缓——-
    - ByteBuffer是很底层的类，直接存储未加工的字节 
        - 初始化：
            - 要写数据时，已经有数据了，所以可以得到byte[]
            - ByteBuffer.wrap(byte[]) //相当于wrap(array, 0, array.length);
            - ByteBuffer.wrap(byte[], offset, length) //offset + length不能超出byte数组的长度
            - 要读数据时，最多只能拿到ByteBuffer可能需要大小
            - ByteBuffer buff = ByteBuffer.allocate((int) fc.size());
        - 接口：byte的读写，不支持对象，连String也不支持
        - 将数据放到ByteBuffer里：装车
            - 上面的wrap方法
            - 一系列put方法，只支持基本类型
        - 将数据从ByteBuffer中转出来：卸车 
            - 一系列get方法，只支持基本类型，注意flip
            - String str = new String(buff.array(), “utf-8”)，buff.array()，跟ByteBuffer指针无关
        - ByteBuffer内部指针：
            - ByteBuffer里有个指针 
            - fc.read(buff)会从往ByteBuffer里写（装车），从哪儿写，总有个起始位置，就是ByteBuffer指针的位置
            - 写完，指针直到最后，也就是第一个空白可写区域
            - 读取里面的信息（卸车），就需要回到起始位置 
                - flip一下
            - positon操作可以跳到任意位置
        - FileChannel：FileInputStream, FileOutputStream, RandomAccessFile这三个旧类被修改了，以支持channel 
            - Reader和Writer不直接支持Channel，但Channel里提供了便利方法来支持他们
            - 获得FileChannel： 
                - FileChannel fc = new FileOutputStream(“data.txt”).getChannel(); //写
                - FileChannel fc = new FileInputStream(“cl1024.json”).getChannel(); //读
                - FileChannel fc = new RandomAccessFile(“data.txt”, “rw”).getChannel(); //可读可写
            - 移动文件指针：append写时，断点续传时能用 
                - fc.position(fc.size()); // Move to the end
            - 写，将一个ByteBuffer写到Channel里： 
                - fc.write(ByteBuffer.wrap(“Some text “.getBytes()));
            - 读，将一个channel里的内容，读到ByteBuffer里，读多少，由ByteBuffer的长度决定
                - fc.read(buff);
                - buff.flip(); 读出来的ByteBuffer一般需要再次解析出来，通过getInt,getFloat等操作，读写切换时，需要flip一下
                - flip怎么理解：fc.read(buff)，ByteBuffer里有个指针 
                    - fc.read(buff)会从往ByteBuffer里写，从哪儿写，总有个起始位置，就是ByteBuffer指针的位置
                    - 写完，指针直到最后，也就是第一个空白可写区域
                    - 所以现在就好理解了，读完文件，也就是往ByteBuffer写完，指针指向ByteBuffer最后，你再读取里面的信息，就需要回到起始位置
- 总结：
    - FileInputStream，FileOutputStream，这相当于煤矿
        - 以前你直接操作stream的read，write，参数是byte[]
        - read，write直接操作煤矿
        - 直接通过byte[]读写，相当于用铁锨铲煤
    - 在new io里，你不能直接操作煤矿了，而是获取一个通道：FileChannel 
        - 通过channel的read，write来操作数据，position，seek等，就是移动指针（文件指针）
        - read，write的参数是ByteBuffer
        - 通过ByteBuffer来包装数据，相当于用车拉煤
    - 由于把byte[]用ByteBuffer包装起来，又面临一个装车和卸车的问题
        - 装车：写文件（wrap, put等方法），读文件（channel.read(buff)）
        - 卸车：读文件（get各种基本类型），写文件（channel.write(buff)）
        - 全车操作：array （与指针无关，read 全部内容）
        - 注意flip的问题，读写切换时，需要flip一下，而且这还不确定就是指针操作 （指针移动到头）
        - 注意rewind的问题，读着读着，想回头从头再读，就得rewind，这个肯定是指针操作
        - buff.hasRemaining()，指针是否到头了
    - 可以看出，Channel和ByteBuffer提供的接口都比较低级，直接和操作系统契合，说是这就是快的原因
    - 关于Channel：
        - FileChannel
        - DatagramChannel：通过UDP读写网络，无连接的
        - SocketChannel：通过TCP读写网络
        - ServerSocketChannel：监听新来的TCP连接，每个新进来的连接都会创建一个SocketChannel
        
例子，代码比较短，直接贴过来
```java
package com.cowthan.nio;

import java.nio.*;
import java.nio.channels.*;
import java.io.*;

public class GetChannel {
    private static final int BSIZE = 1024;
    public static void main(String[] args) throws Exception {
         // 写文件
         FileChannel fc = new FileOutputStream("data.txt").getChannel();
         fc.write(ByteBuffer.wrap("Some text ".getBytes()));  //
         fc.close();
         
         // 写文件：append
         fc = new RandomAccessFile("data.txt", "rw").getChannel();
         fc.position(fc.size()); // Move to the end
         fc.write(ByteBuffer.wrap("Some more".getBytes()));
         fc.close();
         
         // 读文件
         fc = new FileInputStream("data.txt").getChannel();
         ByteBuffer buff = ByteBuffer.allocate((int) fc.size());
         fc.read(buff);
         buff.flip();
         
         System.out.println("读取：");
         String str = new String(buff.array(), "utf-8");
         System.out.println(str);
            
         System.out.println("读取2：");
         while (buff.hasRemaining()){
             System.out.print((char) buff.get());
         }

    }
}
```

### 1.2 更多：flip, clear，compact和mark，reset操作
- flip，clear，compact和mark，reset
    - 这里说的读写都是相对于ByteBuffer 
    - 由写转读：flip
    - 由写转读：clear清空缓冲区，compact清空缓冲区的已读数据（结果就是再装车，就是从未读数据后面开始）
    - 随机读写：mark和reset，如果要一会写一会读，mark会记录当前position，position就是读写的起点，reset会回滚
    - ByteBuffer.allocate(len)的大小问题，大块的移动数据是快的关键，所以长度很重要， 长度越大读写速度越快，但占用内存更多
    - ByteBuffer.wrap(byte[])，不会再复制数组，而是直接以参数为底层数组，快
    - 复制文件时，一个ByteBuffer对象会不断从src的channel来read，并写入dest的channel，注意： 
        - src.read(buff); buff.flip(); dest.write(buff); buff.clear()
        - ByteBuffer必须clear了，才能重新从Channel读
    - ByteBuffer.flip(), clear()比较拙劣，但这正是为了最大速度付出的代价
    

```java
///复制文件的部分代码（更优化的复制文件是用transfer接口，直接通道相连）
ByteBuffer buff = ByteBuffer.allocate(1024); //1K
while(src.read(buff) != -1){
    buff.flip(); //准备卸车
    dest.write(buff); //卸车了
    buff.clear(); //其实这才是真正的卸车，并送回通道那头（可以再次read(buff)了）
}
```

<p style="color;red"> 缓冲器细节：四大索引 </p>

看图： 

![avatar](https://raw.githubusercontent.com/cowthan/JavaAyo/master/doc/img/buffers-modes.png)

- 四大索引：
    - mark：标记，mark方法记录当前位置，reset方法回滚到上次mark的位置
    - position：位置，当前位置，读和写都是在这个位置操作，并且会影响这个位置，position方法可以seek
    - limit：界限 （ByteBufferr 最后有值的位置）
        - 作为读的界限时：指到buffer当前被填入了多少数据，get方法以此为界限
            - flip一下，limit才有值，指向postion，才能有个读的界限
        - 作为写的界限时
            - allocate或者clear时，直接可写，limit指向capacity，表示最多写到这
            - wrap时，直接可读，所以position是0，limit是指到之后，capacity也是指到最后，直接进入可读状态
    - capacity：容量 （ByteBufferr 大小）
            - allocate方法的参数就是capacity    
            - 所以，可以推断一下，ByteBuffer.capacity = 5时，如果转成IntBuffer，capacity是1，不会指向最后，而是留出了最后一个字节，被忽略了，没法通过Int读写 
     
     对应的方法： 
![avatar](https://raw.githubusercontent.com/cowthan/JavaAyo/master/doc/img/nio2.png)

```java
public final Buffer flip() {
    limit = position;
    position = 0;
    mark = UNSET_MARK;
    return this;
}

public final Buffer rewind() {
    position = 0;
    mark = UNSET_MARK;
    return this;
}

public final boolean hasRemaining() {
    return position < limit;
}

public final Buffer clear() {
    position = 0;
    mark = UNSET_MARK;
    limit = capacity;
    return this;
}


public final Buffer mark() {
    mark = position;
    return this;
}

public final Buffer reset() {
    if (mark == UNSET_MARK) {
        throw new InvalidMarkException("Mark not set");
    }
    position = mark;
    return this;
}

```


- 总结：
    - flip：一般用于由写转读，flip之后可以：  
        - 读：是从头读，能读到刚才写的长度
        - 写：是从头写，会覆盖刚才写入的内容
    - clear：一般用于读转写，clear之后可以： 
        - 读：但是读不到什么了
        - 写：是从头写
    - mark和reset：一般用于读写交替
        - mark：相当于int postion = buffer.postion()，记下当前位置
        - reset：相当于buffer.postion(position)，回到刚才记录的位置
        
### 1.3  连接通道
上面说过，nio通过大块数据的移动来加快读写速度，前面这个大小都由ByteBuffer来控制， 
其实还有方法可以直接将读写两个Channel相连

这也是实现文件复制的更好的方法

```java
public class TransferTo {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("arguments: sourcefile destfile");
            System.exit(1);
        }
        FileChannel in = new FileInputStream(args[0]).getChannel(), out = new FileOutputStream(
                args[1]).getChannel();
        in.transferTo(0, in.size(), out);
        // 或者：
        // out.transferFrom(in, 0, in.size());
    }
} // /:~
```

### 1.4 字符流：CharBuffer和Charset，其实就是byte[]和编码问题
ByteBuffer是最原始的，其实就是字节流，适用于二进制数据的读写，图片文件等

但我们更常用的，其实是字符串

- 字符串涉及到的类：
    - CharBuffer：注意，Channel是直接和ByteBuffer交流，所以CharBuffer只能算是上层封装
    - Charset：编码相关，字节流到字符串，肯定会有编码相关的问题
    - CharBuffer.toString()：得到字符串
- 怎么得到CharBuffer
    - 方法1：ByteBuffer.asCharBuffer()，局限在于使用系统默认编码
    - 方法2：Charset.forName(“utf-8”).decode(buff)，相当于new String(buff.array(), “utf-8”)的高级版 
        - 相对的，Charset.forName(“utf-8”).encode(cbuff)，返回个ByteBuffer，就相当于String.getBytes(“utf-8)
- CharBuffer读写
    - put(String)：写
    - toString()：读，就拿到了字符串
  
=====ByteBuffer.asCharBuffer()的局限：没指定编码，容易乱码=====
- 这个一般情况下不能用，为何： 
    - asCharBuffer()会把ByteBuffer转为CharBuffer，但用的是系统默认编码
    
### 1.5 视图缓冲器：ShortBuffer，IntBuffer, LongBuffer，FloatBuffer，DoubleBuffer，CharBuffer
- Buffer类型：
    - ByteBuffer
    - DoubleBuffer
    - FloatBuffer
    - IntBuffer
    - LongBuffer
    - ShortBuffer
    - CharBuffer 字符串的缓冲区
    - MappedByteBuffer 大文件的缓冲区

ByteBuffer系列的类继承关系挺有意思，可以研究研究

ByteArrayBuffer是其最通用子类，一般操作的都是ByteArrayBuffer

ByteBuffer.asLongBuffer(), asIntBuffer(), asDoubleBuffer()等一系列

- 不多说：
    - ByteBuffer底层是一个byte[]，get()方法返回一个byte，1字节，8bit，10字节可以get几次？10次
    - ByteBuffer.asIntBuffer()得到IntBuffer，底层是一个int[]，get()方法返回一个int，还是10字节，可以get几次？
    - 同理，还有ShortBuffer, LongBuffer, FloatBuffer, DoubleBuffer，这些就是ByteBuffer的一个视图，所以叫视图缓冲器
    - asIntBuffer时，如果ByteBuffer本身有5个byte，则其中前4个会变成IntBuffer的第0个元素，第5个被忽略了，但并未被丢弃
    - 往新的IntBuffer放数据（put(int)），默认时会从头开始写，写入的数据会反映到原来的ByteBuffer上
- 总结：
    - 具体也说不明白了，其实就是你有什么类型的数据，就用什么类型的Buffer
    - 但直接往通道读写的，肯定是ByteBuffer，所以首先得有个ByteBuffer，其他视图Buffer，就得从ByteBuffer来
    - 怎么从ByteBuffer来呢，ByteBuffer.asIntBuffer()等方法

例子：ViewBuffers.java
![avatar](https://raw.githubusercontent.com/cowthan/JavaAyo/master/doc/img/nio1.png)


###  1.6 字节序
- 简介： 
    - 高位优先，Big Endian，最重要的字节放地址最低的存储单元，ByteBuffer默认以高位优先，网络传输大部分也以高位优先
    - 低位优先，Little Endian
    - ByteBuffer.order()方法切换字节序 
        - ByteOrderr.BIG_ENDIAN
        - ByteOrderr.LITTLE_ENDIAN
    - 对于00000000 01100001，按short来读，如果是big endian，就是97， 以little endian，就是24832
    
### 1.7 Scatter/Gather
一个Channel，多个Buffer，相当于多个运煤车在一个通道工作

读到多个Buffer里：
```java
ByteBuffer header = ByteBuffer.allocate(128);
ByteBuffer body   = ByteBuffer.allocate(1024);
ByteBuffer[] bufferArray = { header, body };
channel.read(bufferArray)
```

多个Buffer往channel写：

```java
//注意，Buffer的长度是100，但只有50个数据，就只会写入50，换句话说，只有position和limit之间的内容会被写入（put完先flip一下，才能往channel写？？？）
ByteBuffer header = ByteBuffer.allocate(128);
ByteBuffer body   = ByteBuffer.allocate(1024);
ByteBuffer[] bufferArray = { header, body };
channel.write(bufferArray);
```

### 1.8 内存映射文件：大文件的读写
大文件，如2G的文件，没法一下加载到内存中读写

MappedByteBuffer提供了一个映射功能，可以将文件部分载入到内存中，但你使用时， 
感觉文件都在内存中了

MappedByteBuffer继承了ByteBuffer，所以可以像上面那样使用

它的读取和来回读取要比普通ByteBuffer快的多，但是写入还不如普通ByteBuffer的一般速度。此结论来自以下测试代码

```java
    public void copyFile1() {
        long timeStar = System.currentTimeMillis();// 得到当前的时间

        FileChannel fic = null;
        FileChannel foc = null;
        try {
            fic = new FileInputStream("d:/video/2/in mad[atid]/atid239/1229-atid239.avi").getChannel();
            foc = new FileOutputStream("d:/video/2/in mad[atid]/atid239/1229-atid239-1.avi").getChannel();
            MappedByteBuffer src = fic.map(FileChannel.MapMode.READ_ONLY, 0, fic.size());
            foc.write(src);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fic != null) {
                    fic.close();
                }
                if (foc != null) {
                    foc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long timeEnd = System.currentTimeMillis();// 得到当前的时间

        System.out.println("MappedByteBufferSample write time：" + (timeEnd - timeStar) + "ms");
    }
    
    public void copyFile2() {
            long timeStar = System.currentTimeMillis();// 得到当前的时间
    
            ByteBuffer byteBuffer = ByteBuffer.allocate(10 * 1024 * 1024);
    
            FileChannel fic = null;
            FileChannel foc = null;
            try {
                fic = new FileInputStream("d:/video/2/in mad[atid]/atid239/1229-atid239.avi").getChannel();
                foc = new FileOutputStream("d:/video/2/in mad[atid]/atid239/1229-atid239-2.avi").getChannel();
    
                while (fic.read(byteBuffer) != -1) {
                    byteBuffer.flip();
                    foc.write(byteBuffer);
                    byteBuffer.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fic != null) {
                        fic.close();
                    }
                    if (foc != null) {
                        foc.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    
            long timeEnd = System.currentTimeMillis();// 得到当前的时间
    
            System.out.println("ByteBuffer write time :" + (timeEnd - timeStar) + "ms");
        }
    
```
ByteBuffer write time :1114ms
MappedByteBufferSample write time：14071ms

MappedByteBufferSample 不能超过2 g， 否则会出现 Size exceeds Integer.MAX_VALUE 异常， 有说法是32系统不能超过1.5 g，这与系统内存有关



### 1.9 文件加锁

- 简介
    - 有时我们需要对文件加锁，以同步访问某个文件
    - FileLock是使用了操作系统提供的文件加锁功能，所以可以影响到其他系统进程，其他普通进程，即使不是java写的
    - FileLock.lock()会阻塞，tryLock不会阻塞
    - lock系列方法可以带参数： 
        - 加锁文件的某一部分，多个进程可以分别加锁文件的一部分，数据库就是这样
        - 参数3可以决定是否共享锁，这里又出现个共享锁和独占锁，共享锁需要操作系统支持

用法：

```java
public static void main(String[] args) throws Exception {
    FileOutputStream fos = new FileOutputStream("file.txt");
    FileLock fl = fos.getChannel().tryLock();//---------
    if (fl != null) {
        System.out.println("Locked File");
        TimeUnit.MILLISECONDS.sleep(100);
        fl.release();//---------------------------------
        System.out.println("Released Lock");
    }
    fos.close();
}
```
更多例子

```java
package com.cowthan.nio;

//: io/LockingMappedFiles.java
// Locking portions of a mapped file.
// {RunByHand}
import java.nio.*;
import java.nio.channels.*;
import java.io.*;

public class LockingMappedFiles {
    static final int LENGTH = 0x8FFFFFF; // 128 MB
    static FileChannel fc;

    public static void main(String[] args) throws Exception {
        fc = new RandomAccessFile("test.dat", "rw").getChannel();
        MappedByteBuffer out = fc
                .map(FileChannel.MapMode.READ_WRITE, 0, LENGTH);
        for (int i = 0; i < LENGTH; i++)
            out.put((byte) 'x');
        new LockAndModify(out, 0, 0 + LENGTH / 3);
        new LockAndModify(out, LENGTH / 2, LENGTH / 2 + LENGTH / 4);
    }

    private static class LockAndModify extends Thread {
        private ByteBuffer buff;
        private int start, end;

        LockAndModify(ByteBuffer mbb, int start, int end) {
            this.start = start;
            this.end = end;
            mbb.limit(end);
            mbb.position(start);
            buff = mbb.slice();
            start();
        }

        public void run() {
            try {
                // Exclusive lock with no overlap:
                FileLock fl = fc.lock(start, end, false);
                System.out.println("Locked: " + start + " to " + end);
                // Perform modification:
                while (buff.position() < buff.limit() - 1)
                    buff.put((byte) (buff.get() + 1));
                fl.release();
                System.out.println("Released: " + start + " to " + end);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
} // /:

```


## 2 异步IO

- 2.1 关于Channel： 
    - FileChannel：永远都是阻塞模式，当然读本地文件也不会阻塞多久，没法和Selector配合
    - DatagramChannel：通过UDP读写网络，无连接的
    - SocketChannel：通过TCP读写网络
    - ServerSocketChannel：监听新来的TCP连接，每个新进来的连接都会创建一个SocketChannel
- 简介：
    - Selector提供了一个线程管理多个Channel的功能，与之相比，旧的Socket处理方式是每个Socket连接都在一个线程上阻塞
    - Channel和Selector配合时，必须channel.configureBlocking(false)切换到非阻塞模式
    - 而FileChannel没有非阻塞模式，只有Socket相关的Channel才有
- 概括：
    - SocketServerChannel和SocketChannel的基本用法，参考socket.nio.NioXXServer和Client
    - 可能会阻塞，可以通过channel.configureBlocking(false)设置非阻塞的地方： 
        - SocketChannel.connect(new InetSocketAddress(hostname, port))， 配合sc.finishConnect()判断是否连接成功
        - SocketChannel sc = ssc.accept()，在非阻塞模式下，无新连接进来时返回值会是null
        
## 2.1 旧IO处理Socket的方式
参考：http://ifeve.com/socket-channel/

要读取Socket上的Stream，就得在read时阻塞，所以每一个Socket都得一个线程管理，对于服务器来说，能开的线程数是有限的
    
## 2.2 不使用Selector，自己想法管理SocketChannel
```java
@Override
public void run() {
    while(!isClosed && !Thread.interrupted()){
        for(String key: map.keySet()){
            SocketChannel sc = map.get(key);

            ByteBuffer buf = ByteBuffer.allocate(1024);
            try {
                int bytesRead = sc.read(buf);
                buf.flip();
                if(bytesRead <= 0){

                }else{
                    System.out.println("收到消息（来自" + key + "）：" + Charset.forName("utf-8").decode(buf));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```
- 弊端分析：
    - 不断循环读取所有Channel，有数据则读出来
    - 问题1：在while里，你sleep还是不sleep，sleep就损失太多实时性，不sleep就导致CPU大量空转
    - 问题2：对于ServerSocketChannel，如果accept非阻塞，则需要while(true)不断判断是否有新连接，也浪费CPU
    - 问题3：对于ServerSocket.connect()，如果非阻塞，则需要while(true)不断判断是否连接服务器成功，也浪费CPU
- 所以现在我们知道需要什么了
    - 需要SocketChannel的read方法不阻塞
    - 或者需要一个东西，可以在所有SocketChannel上等待，任何一个有了消息，就可以唤醒，这里面就有个监听的概念
    - 并且可读，可写，accept(), connect()都应该对应不同的事件
    - 这就引出了Selector，Selector就是java从语言层面和系统层面对这个问题的解决方案
    
## 2.3 Selector
使用Selector的完整示例：
```java
Selector selector = Selector.open();
channel.configureBlocking(false);
SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
while(true) {
  int readyChannels = selector.select();   //就在这阻塞，但已经实现了一个线程管理多个Channel（SocketChannel-读写，connect事件，DatagramChannel-读写事件，SocketServerChannel-accept事件）
  if(readyChannels == 0) continue;
  Set selectedKeys = selector.selectedKeys();
  Iterator keyIterator = selectedKeys.iterator();
  while(keyIterator.hasNext()) {
    SelectionKey key = keyIterator.next();
    if(key.isAcceptable()) {
        // a connection was accepted by a ServerSocketChannel.
    } else if (key.isConnectable()) {
        // a connection was established with a remote server.
    } else if (key.isReadable()) {
        // a channel is ready for reading
    } else if (key.isWritable()) {
        // a channel is ready for writing
    }
    keyIterator.remove();
  }
}
```

```java
Selector selector = Selector.open();
SelectionKey selectionKey = sc.register(selector, SelectionKey.OP_READ);

// 看Selector对哪些事件感兴趣
int interestSet = selectionKey.interestOps();
boolean isInterestedInAccept  = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT;
boolean isInterestedInConnect = interestSet & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT;
boolean isInterestedInRead    = interestSet & SelectionKey.OP_READ) == SelectionKey.OP_READ;
boolean isInterestedInWrite   = interestSet & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE;

// 通道中已经就绪的集合，每一次selection都得先访问这个，知道是因为哪些事件被唤醒的
int readySet = selectionKey.readyOps();
// 或者：
selectionKey.isAcceptable();
selectionKey.isConnectable();
selectionKey.isReadable();
selectionKey.isWritable();

// 拿到Channel和Selector
Channel  channel  = selectionKey.channel();
Selector selector = selectionKey.selector();

// 对应关系是：1个Selector，多个Channel，多个SelectionKey，一个Channel对应一个SelectionKey，而且一个SelectionKey可以添加一个extra数据，以满足特定需求

// select方法：这才是会阻塞的地方，注意，在这里阻塞，是性能最佳的表现
int readyCount = selector.select()  //select()阻塞到至少有一个通道在你注册的事件上就绪了
int readyCount = selector.select(long timeout) //最长会阻塞timeout毫秒(参数)
int readyCount = selector.selectNow() //不会阻塞，无则0
// 返回值：有几个通道就绪
/*
select()方法返回的int值表示有多少通道已经就绪。亦即，自上次调用select()方法后有多少通
道变成就绪状态。如果调用select()方法，因为有一个通道变成就绪状态，返回了1，若再次调用select()方法，
如果另一个通道就绪了，它会再次返回1。如果对第一个就绪的channel没有做任何操作，现在就有两个就绪的通
道，但在每次select()方法调用之间，只有一个通道就绪了
*/

// 有通道就绪了，就得得到这个Channel，通道存在SelectionKey里，而selector可以获得一个SelectionKey集合
Set selectedKeys = selector.selectedKeys();
Iterator keyIterator = selectedKeys.iterator();
while(keyIterator.hasNext()) {
    SelectionKey key = keyIterator.next();
    if(key.isAcceptable()) {
        // a connection was accepted by a ServerSocketChannel.
    } else if (key.isConnectable()) {
        // a connection was established with a remote server.
    } else if (key.isReadable()) {
        // a channel is ready for reading
        Channel channel = key.channel();
    } else if (key.isWritable()) {
        // a channel is ready for writing
    }
    keyIterator.remove();
}
```

- register方法参数：Channel事件
    - 参数表示Selector对Channel的什么事件感兴趣
    - Connect：SelectionKey.OP_CONNECT
    - Accept：SelectionKey.OP_ACCEPT
    - Read：SelectionKey.OP_READ
    - Write：SelectionKey.OP_WRITE
    - 可以组合：int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
- SelectionKey都有啥信息：
    - interest集合：对哪些事件感兴趣
    - ready集合：感兴趣的事件集中，哪些事件准备就绪了
    - Channel：监听的哪个Channel
    - Selector：谁在监听
    - 可选的extra
- 唤醒阻塞的Selector：在select方法的阻塞
    - 情况1：有感兴趣的事件来了
    - 情况2：手动调用Selector.wakeup()，只要让其它线程在第一个线程调用select()方法的那个对象上调用Selector.wakeup()方法即可 
        - 如果有其它线程调用了wakeup()方法，但当前没有线程阻塞在select()方法上，下个调用select()方法的线程会立即“醒来（wake up）”。
    - 关闭Selector
        - close()方法，关闭该Selector，且使注册到该Selector上的所有SelectionKey实例无效
        - 通道本身并不会关闭


## 3 DatagramChannel：UDP通信
参考：http://ifeve.com/datagram-channel/