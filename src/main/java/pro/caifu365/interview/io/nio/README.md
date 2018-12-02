# ByteBuffer 使用

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
        
## 1.3  连接通道
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

## 1.4 字符流：CharBuffer和Charset，其实就是byte[]和编码问题
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
    
## 1.5 视图缓冲器：ShortBuffer，IntBuffer, LongBuffer，FloatBuffer，DoubleBuffer，CharBuffer
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


##  1.6 字节序
- 简介： 
    - 高位优先，Big Endian，最重要的字节放地址最低的存储单元，ByteBuffer默认以高位优先，网络传输大部分也以高位优先
    - 低位优先，Little Endian
    - ByteBuffer.order()方法切换字节序 
        - ByteOrderr.BIG_ENDIAN
        - ByteOrderr.LITTLE_ENDIAN
    - 对于00000000 01100001，按short来读，如果是big endian，就是97， 以little endian，就是24832
    
## 1.7 Scatter/Gather
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

## 1.8 内存映射文件：大文件的读写
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




