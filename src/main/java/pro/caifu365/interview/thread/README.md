# Java线程详解（深度好文）
https://blog.csdn.net/kwame211/article/details/78963044

## Java线程：概念与原理
### 一、进程与线程
进程是指一个内存中运行的应用程序，每个进程都有自己独立的一块内存空间，即进程空间或（虚空间）。进程不依赖于线程而独立存在，一个进程中可以启动多个线程。比如在Windows系统中，一个运行的exe就是一个进程。

线程是指进程中的一个执行流程，一个进程中可以运行多个线程。比如java.exe进程中可以运行很多线程。线程总是属于某个进程，线程没有自己的虚拟地址空间，与进程内的其他线程一起共享分配给该进程的所有资源。

“同时”执行是人的感觉，在线程之间实际上轮换执行。

进程在执行过程中拥有独立的内存单元，进程有独立的地址空间，而多个线程共享内存，从而极大地提高了程序的运行效率。

线程在执行过程中与进程还是有区别的。每个独立的线程有一个程序运行的入口、顺序执行序列和程序的出口。但是线程不能够独立执行，必须依存在应用程序中，由应用程序提供多个线程执行控制。
 
进程是具有一定独立功能的程序关于某个数据集合上的一次运行活动，进程是系统进行资源分配和调度的一个独立单位。
 
线程是进程的一个实体，是CPU调度和分派的基本单位，它是比进程更小的能独立运行的基本单位。线程自己基本上不拥有系统资源，只拥有一点在运行中必不可少的资源（如程序计数器,一组寄存器和栈），但是它可与同属一个进程的其他的线程共享进程所拥有的全部资源。
 
线程有自己的堆栈和局部变量，但线程之间没有单独的地址空间，一个线程包含以下内容：

- 一个指向当前被执行指令的指令指针；
- 一个栈；
- 一个寄存器值的集合，定义了一部分描述正在执行线程的处理器状态的值
- 一个私有的数据区。

我们使用Join()方法挂起当前线程，直到调用Join()方法的线程执行完毕。该方法还存在包含参数的重载版本，其中的参数用于指定等待线程结束的最长时间（即超时）所花费的毫秒数。如果线程中的工作在规定的超时时段内结束，该版本的Join()方法将返回一个布尔量True。

简而言之：

- 一个程序至少有一个进程，一个进程至少有一个线程。
- 线程的划分尺度小于进程，使得多进程程序的并发性高。
- 另外，进程在执行过程中拥有独立的内存单元，而多个线程共享内存，从而极大地提高了程序的运行效率。
- 线程在执行过程中与进程还是有区别的。每个独立的线程有一个程序运行的入口、顺序执行序列和程序的出口。但是线程不能够独立执行，必须依存在应用程序中，由应用程序提供多个线程执行控制。
- 从逻辑角度来看，多线程的意义在于一个应用程序中，有多个执行部分可以同时执行。但操作系统并没有将多个线程看做多个独立的应用，来实现进程的调度和管理以及资源分配。这就是进程和线程的重要区别。

在Java中，每次程序运行至少启动2个线程：一个是main线程，一个是垃圾收集线程。
每当使用java命令执行一个类的时候，实际上都会启动一个JVM，每一个JVM实际上就是在操作系统中启动了一个进程。

### 二、Java中的线程
在Java中，“线程”指两件不同的事情：

#### 1.java.lang.Thread类的一个实例；
#### 2. 线程的执行。

在 Java程序中，有两种方法创建线程：
 
一是对 Thread 类进行派生并覆盖 run方法；
 
二是通过实现Runnable接口创建。

使用java.lang.Thread类或者java.lang.Runnable接口编写代码来定义、实例化和启动新线程。

一个Thread类实例只是一个对象，像Java中的任何其他对象一样，具有变量和方法，生死于堆上。

Java中，每个线程都有一个调用栈，即使不在程序中创建任何新的线程，线程也在后台运行着。

一个Java应用总是从main()方法开始运行，main()方法运行在一个线程内，他被称为主线程。

 一旦创建一个新的线程，就产生一个新的调用栈。
 
 线程总体分两类：用户线程和守候线程。
 
 当所有用户线程执行完毕的时候，JVM自动关闭。但是守候线程却不独立于JVM，守候线程一般是由操作系统或者用户自己创建的。

## Java线程：创建与启动

### 一、定义线程
#### 1. 扩展java.lang.Thread类。

此类中有个run()方法，应该注意其用法：public void run()

如果该线程是使用独立的Runnable运行对象构造的，则调用该Runnable对象的run方法；否则，该方法不执行任何操作并返回。

Thread的子类应该重写该方法。

#### 2. 实现java.lang.Runnable接口。
void run()

使用实现接口Runnable的对象创建一个线程时，启动该线程将导致在独立执行的线程中调用对象的run方法。

方法run的常规协定是，它可能执行任何所需的操作。

### 二、实例化线程
#### 1. 如果是扩展java.lang.Thread类的线程，则直接new即可。
#### 2. 如果是实现了java.lang.Runnable接口的类，则用Thread的构造方法：
```java
Thread(Runnabletarget)  
Thread(Runnabletarget, String name)  
Thread(ThreadGroupgroup, Runnable target)  
Thread(ThreadGroupgroup, Runnable target, String name)  
Thread(ThreadGroupgroup, Runnable target, String name, long stackSize)  
```

 其中：
 
 Runnable target：实现了Runnable接口的类的实例。 
 
#### 1. Thread类也实现了Runnable接口，因此，从Thread类继承的类的实例也可以作为target传入这个构造方法。
#### 2. 直接实现Runnable接口类的实例。
#### 3. 线程池建立多线程。

String name：线程的名子。这个名子可以在建立Thread实例后通过Thread类的setName方法设置。默认线程名：Thread-N，N是线程建立的顺序，是一个不重复的正整数。

ThreadGroup group：当前建立的线程所属的线程组。如果不指定线程组，所有的线程都被加到一个默认的线程组中。

long stackSize：线程栈的大小，这个值一般是CPU页面的整数倍。如x86的页面大小是4KB.在x86平台下，默认的线程栈大小是12KB。

### 三、启动线程
在线程的Thread对象上调用start()方法，而不是run()或者别的方法。

在调用start()方法之前：线程处于新状态中，新状态指有一个Thread对象，但还没有一个真正的线程。

在调用start()方法之后：发生了一系列复杂的事情——

启动新的执行线程（具有新的调用栈）；

该线程从新状态转移到可运行状态；

当该线程获得机会执行时，其目标run()方法将运行。

注意：对Java来说，run()方法没有任何特别之处。像main()方法一样，它只是新线程知道调用的方法名称（和签名）。因此，在Runnable上或者Thread上调用run方法是合法的。但并不启动新的线程。

 ### 四、例子
 #### 1. 实现Runnable接口的多线程例子
 
 ```java
/** 
 * 实现Runnable接口的类 
 */
public class RunnableImpl implements Runnable{  
    private Stringname;  
    public RunnableImpl(String name) {  
       this.name = name;  
    }  
    @Override  
    public void run() {  
       for (int i = 0; i < 5; i++) {  
           for(long k=0;k<100000000;k++);  
           System.out.println(name+":"+i);  
       }       
    }  
}  
   
/** 
 * 测试Runnable类实现的多线程程序 
 */  
public class TestRunnable {  
   
    public static void main(String[] args) {  
       RunnableImpl ri1=new RunnableImpl("李白");  
       RunnableImpl ri2=new RunnableImpl("屈原");  
       Thread t1=new Thread(ri1);  
       Thread t2=new Thread(ri2);  
       t1.start();  
       t2.start();  
    }  
} 
```

 #### 2. 扩展Thread类实现的多线程例子
 ```java
/** 
 * 测试扩展Thread类实现的多线程程序 
 */  
public class TestThread extends Thread {  
    public TestThread(String name){  
       super(name);  
    }  
    @Override  
    public void run() {  
       for(int i=0;i<5;i++){  
           for(long k=0;k<100000000;k++);  
           System.out.println(this.getName()+":"+i);  
       }  
    }  
    public static void main(String[] args){  
       Thread t1=new TestThread("李白");  
       Thread t2=new TestThread("屈原");  
       t1.start();  
       t2.start();        
    }  
} 
```

对于上面的多线程程序代码来说，输出的结果是不确定的。其中的一条语句for(long k=0;k<100000000;k++);是用来模拟一个非常耗时的操作的。


### 五、一些常见问题    
#### 1、线程的名字，一个运行中的线程总是有名字的，名字有两个来源，一个是虚拟机自己给的名字，一个是你自己的定的名字。在没有指定线程名字的情况下，虚拟机总会为线程指定名字，并且主线程的名字总是mian，非主线程的名字不确定。
#### 2、线程都可以设置名字，也可以获取线程的名字，连主线程也不例外。
#### 3、获取当前线程的对象的方法是：Thread.currentThread()；
#### 4、在上面的代码中，只能保证：每个线程都将启动，每个线程都将运行直到完成。一系列线程以某种顺序启动并不意味着将按该顺序执行。对于任何一组启动的线程来说，调度程序不能保证其执行次序，持续时间也无法保证。
#### 5、当线程目标run()方法结束时该线程完成。
#### 6、一旦线程启动，它就永远不能再重新启动。只有一个新的线程可以被启动，并且只能一次。一个可运行的线程或死线程可以被重新启动。
#### 7、线程的调度是JVM的一部分，在一个CPU的机器上上，实际上一次只能运行一个线程。一次只有一个线程栈执行。JVM线程调度程序决定实际运行哪个处于可运行状态的线程。众多可运行线程中的某一个会被选中做为当前线程。可运行线程被选择运行的顺序是没有保障的。
#### 8、尽管通常采用队列形式，但这是没有保障的。队列形式是指当一个线程完成“一轮”时，它移到可运行队列的尾部等待，直到它最终排队到该队列的前端为止，它才能被再次选中。事实上，我们把它称为可运行池而不是一个可运行队列，目的是帮助认识线程并不都是以某种有保障的顺序排列而成一个一个队列的事实。
#### 9、尽管我们没有无法控制线程调度程序，但可以通过别的方式来影响线程调度的方式。

## Java线程：线程栈模型与线程的变量
要理解线程调度的原理，以及线程执行过程，必须理解线程栈模型。

线程栈是指某时刻时内存中线程调度的栈信息，当前调用的方法总是位于栈顶。线程栈的内容是随着程序的运行动态变化的，因此研究线程栈必须选择一个运行的时刻（实际上指代码运行到什么地方)。

下面通过一个示例性的代码说明线程（调用）栈的变化过程。

![avatar](https://img-blog.csdn.net/20150905033648798)

这幅图描述在代码执行到两个不同时刻1、2时候，虚拟机线程调用栈示意图。

当程序执行到t.start();时候，程序多出一个分支（增加了一个调用栈B），这样，栈A、栈B并行执行。
 
从这里就可以看出方法调用和线程启动的区别了。

## Java线程：线程状态的转换
### 一、线程状态
线程的状态转换是线程控制的基础。线程状态总的可以分为五大状态。用一个图来描述如下：

![avatar](https://img-blog.csdn.net/20150905033659802)


#### 1、新状态：线程对象已经创建，还没有在其上调用start()方法。
#### 2、可运行状态：当线程有资格运行，但调度程序还没有把它选定为运行线程时线程所处的状态。当start()方法调用时，线程首先进入可运行状态。在线程运行之后或者从阻塞、等待或睡眠状态回来后，也返回到可运行状态。
#### 3、运行状态：线程调度程序从可运行池中选择一个线程作为当前线程时线程所处的状态。这也是线程进入运行状态的唯一一种方式。
#### 4、等待/阻塞/睡眠状态：这是线程有资格运行时它所处的状态。实际上这个三状态组合为一种，其共同点是：线程仍旧是活的，但是当前没有条件运行。换句话说，它是可运行的，但是如果某件事件出现，他可能返回到可运行状态。
#### 5、死亡态：当线程的run()方法完成时就认为它死去。这个线程对象也许是活的，但是，它已经不是一个单独执行的线程。线程一旦死亡，就不能复生。如果在一个死去的线程上调用start()方法，会抛出java.lang.IllegalThreadStateException异常。

### 二、阻止线程执行
对于线程的阻止，考虑一下三个方面，不考虑IO阻塞的情况：
-   睡眠；
-   等待；

因为需要一个对象的锁定而被阻塞。

#### 1、睡眠
Thread.sleep(longmillis)和Thread.sleep(long millis, int nanos)静态方法强制当前正在执行的线程休眠（暂停执行），以“减慢线程”。当线程睡眠时，它入睡在某个地方，在苏醒之前不会返回到可运行状态。当睡眠时间到期，则返回到可运行状态。

线程睡眠的原因：线程执行太快，或者需要强制进入下一轮，因为Java规范不保证合理的轮换。

睡眠的实现：调用静态方法。

```java
try {
    Thread.sleep(123);
} 
catch (InterruptedException e) {
    e.printStackTrace();
}
```

睡眠的位置：为了让其他线程有机会执行，可以将Thread.sleep()的调用放线程run()之内。这样才能保证该线程执行过程中会睡眠。

例如，在前面的例子中，将一个耗时的操作改为睡眠，以减慢线程的执行。可以这么写：

```java
for(int i=0;i<5;i++){  
    // 很耗时的操作，用来减慢线程的执行  
    try {  
        Thread.sleep(3);  
    } catch (InterruptedException e) {  
        e.printStackTrace();  
    }  
    System.out.println(this.getName()+":"+i);  
}
```

 这样，线程在每次执行过程中，总会睡眠3毫秒，睡眠了，其他的线程就有机会执行了。
 
 注意：
 
##### 1、线程睡眠是帮助所有线程获得运行机会的最好方法。
##### 2、线程睡眠到期自动苏醒，并返回到可运行状态，不是运行状态。sleep()中指定的时间是线程不会运行的最短时间。因此，sleep()方法不能保证该线程睡眠到期后就开始执行。
##### 3、sleep()是静态方法，只能控制当前正在运行的线程。

下面给个例子：

```java
/** 
 * 一个计数器，计数到100，在每个数字之间暂停1秒，每隔10个数字输出一个字符串 
 */  
public class CalcThread extends Thread {  
    public void run(){  
       for(int i=0;i<100;i++){  
           if ((i)%10==0) {  
              System.out.println("--------"+i);  
           }  
           System.out.print(i);  
           try {  
              Thread.sleep(1);  
              System.out.print("    线程睡眠1毫秒！\n");  
           } catch (InterruptedException e) {  
              e.printStackTrace();  
           }  
       }  
    }  
   
    public static void main(String[] args) {  
       new CalcThread().start();  
    }  
}
```

#### 2、线程的优先级和线程让步yield()
线程的让步是通过Thread.yield()来实现的。yield()方法的作用是：暂停当前正在执行的线程对象，并执行其他线程。

要理解yield()，必须了解线程的优先级的概念。线程总是存在优先级，优先级范围在1~10之间。JVM线程调度程序是基于优先级的抢先调度机制。在大多数情况下，当前运行的线程优先级将大于或等于线程池中任何线程的优先级。但这仅仅是大多数情况。

注意：当设计多线程应用程序的时候，一定不要依赖于线程的优先级。因为线程调度优先级操作是没有保障的，只能把线程优先级作用作为一种提高程序效率的方法，但是要保证程序不依赖这种操作。

当线程池中线程都具有相同的优先级，调度程序的JVM实现自由选择它喜欢的线程。这时候调度程序的操作有两种可能：一是选择一个线程运行，直到它阻塞或者运行完成为止。二是时间分片，为池内的每个线程提供均等的运行机会。

设置线程的优先级：线程默认的优先级是创建它的执行线程的优先级。可以通过setPriority(int newPriority)更改线程的优先级。

例如：
```java
Thread t = new MyThread();  
t.setPriority(8);  
t.start(); 
```

线程优先级为1~10之间的正整数，JVM从不会改变一个线程的优先级。然而，1~10之间的值是没有保证的。一些JVM可能不能识别10个不同的值，而将这些优先级进行每两个或多个合并，变成少于10个的优先级，则两个或多个优先级的线程可能被映射为一个优先级。

线程默认优先级是5，Thread类中有三个常量，定义线程优先级范围：

```java
static intMAX_PRIORITY：线程可以具有的最高优先级。  
static intMIN_PRIORITY：线程可以具有的最低优先级。  
static intNORM_PRIORITY：分配给线程的默认优先级。  
```

#### 3、Thread.yield()方法

Thread.yield()方法作用是：暂停当前正在执行的线程对象，并执行其他线程。

 yield()应该做的是让当前运行线程回到可运行状态，以允许具有相同优先级的其他线程获得运行机会。因此，使用yield()的目的是让相同优先级的线程之间能适当的轮转执行。但是，实际中无法保证yield()达到让步目的，因为让步的线程还有可能被线程调度程序再次选中。
 
 结论：yield()从未导致线程转到等待/睡眠/阻塞状态。在大多数情况下，yield()将导致线程从运行状态转到可运行状态，但有可能没有效果。
 
#### 4、join()方法

Thread的非静态方法join()让一个线程B“加入”到另外一个线程A的尾部。在A执行完毕之前，B不能工作。例如：

```java
Thread t = new MyThread();  
t.start();  
t.join(); 
```

另外，join()方法还有带超时限制的重载版本。例如t.join(5000);则让线程等待5000毫秒，如果超过这个时间，则停止等待，变为可运行状态。

线程的加入join()对线程栈导致的结果是线程栈发生了变化，当然这些变化都是瞬时的。下面给示意图：

![avatar](https://img-blog.csdn.net/20150905033810371)


小结

到目前位置，介绍了线程离开运行状态的3种方法：
##### 1、调用Thread.sleep()：使当前线程睡眠至少多少毫秒（尽管它可能在指定的时间之前被中断）。
##### 2、调用Thread.yield()：不能保障太多事情，尽管通常它会让当前运行线程回到可运行性状态，使得有相同优先级的线程有机会执行。
##### 3、调用join()方法：保证当前线程停止执行，直到该线程所加入的线程完成为止。然而，如果它加入的线程没有存活，则当前线程不需要停止。

除了以上三种方式外，还有下面几种特殊情况可能使线程离开运行状态：
 
##### 1、线程的run()方法完成。
##### 2、在对象上调用wait()方法（不是在线程上调用）。 
##### 3、线程不能在对象上获得锁定，它正试图运行该对象的方法代码。
##### 4、线程调度程序可以决定将当前运行状态移动到可运行状态，以便让另一个线程获得运行机会，而不需要任何理由。

## Java线程：线程的同步与锁
### 一、同步问题提出
线程的同步是为了防止多个线程访问一个数据对象时，对数据造成的破坏。

例如：两个线程ThreadA、ThreadB都操作同一个对象Foo对象，并修改Foo对象上的数据。
```java
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
   
public class FooRunnable implements Runnable {  
    private Foo foo =new Foo();  
   
    public static void main(String[] args) {  
       FooRunnable r = new FooRunnable();  
        Thread ta = new Thread(r,"Thread-A");  
        Thread tb = new Thread(r,"Thread-B");  
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
            System.out.println(Thread.currentThread().getName()+ " :当前foo对象的x值= " + foo.getX());  
        }  
    }  
   
    public int fix(int y) {  
       return foo.fix(y);  
    }  
}
```
从结果发现，这样的输出值明显是不合理的，原因是两个线程不加控制的访问Foo对象并修改其数据所致。

如果要保持结果的合理性，只需要达到一个目的，就是将对Foo的访问加以限制，每次只能有一个线程在访问。这样就能保证Foo对象中数据的合理性了。

在具体的Java代码中需要完成以下两个操作：

把竞争访问的资源类Foo变量x标识为private；

同步修改变量的代码，使用synchronized关键字同步方法或代码。

### 二、同步和锁定
#### 1、锁的原理
Java中每个对象都有一个内置锁。

当程序运行到非静态的synchronized同步方法上时，自动获得与正在执行代码类的当前实例（this实例）有关的锁。获得一个对象的锁也称为获取锁、锁定对象、在对象上锁定或在对象上同步。

当程序运行到synchronized同步方法或代码块时才该对象锁才起作用。

一个对象只有一个锁。所以，如果一个线程获得该锁，就没有其他线程可以获得锁，直到第一个线程释放（或返回）锁。这也意味着任何其他线程都不能进入该对象上的synchronized方法或代码块，直到该锁被释放。

释放锁是指持锁线程退出了synchronized同步方法或代码块。

关于锁和同步，有一下几个要点：
##### 1）只能同步方法，而不能同步变量和类；
##### 2）每个对象只有一个锁；当提到同步时，应该清楚在什么上同步？也就是说，在哪个对象上同步？
##### 3）不必同步类中所有的方法，类可以同时拥有同步和非同步方法。
##### 4）如果两个线程要执行一个类中的synchronized方法，并且两个线程使用相同的实例来调用方法，那么一次只能有一个线程能够执行方法，另一个需要等待，直到锁被释放。也就是说：如果一个线程在对象上获得一个锁，就没有任何其他线程可以进入（该对象的）类中的任何一个同步方法。
##### 5）如果线程拥有同步和非同步方法，则非同步方法可以被多个线程自由访问而不受锁的限制。
##### 6）线程睡眠时，它所持的任何锁都不会释放。
##### 7）线程可以获得多个锁。比如，在一个对象的同步方法里面调用另外一个对象的同步方法，则获取了两个对象的同步锁。
##### 8）同步损害并发性，应该尽可能缩小同步范围。同步不但可以同步整个方法，还可以同步方法中一部分代码块。


```java
public int fix(int y) {
    synchronized (this)
    {
        x = x - y;
        return x;
    }
}
```

当然，同步方法也可以改写为非同步方法，但功能完全一样的，例如：

```java
public synchronized int getX() {  
    return x++;  
} 
```
与

```java
public int getX() {  
  synchronized (this){
    return x;
  }
}
```
效果是完全一样的。

### 三、静态方法同步

要同步静态方法，需要一个用于整个类对象的锁，这个对象是就是这个类（XXX.class)。

例如：

```java
public static intsetName(String name){  
  synchronized(Xxx.class){  
    Xxx.name = name;  
  }  
} 
```

### 四、如果线程不能获得锁会怎么样

如果线程试图进入同步方法，而其锁已经被占用，则线程在该对象上被阻塞。实质上，线程进入该对象的一种池中，必须在那里等待，直到其锁被释放，该线程再次变为可运行或运行为止。

当考虑阻塞时，一定要注意哪个对象正被用于锁定：

#### 1、调用同一个对象中非静态同步方法的线程将彼此阻塞。如果是不同对象，则每个线程有自己的对象的锁，线程间彼此互不干预。
#### 2、调用同一个类中的静态同步方法的线程将彼此阻塞，它们都是锁定在相同的Class对象上。
#### 3、静态同步方法和非静态同步方法将永远不会彼此阻塞，因为静态方法锁定在Class对象上，非静态方法锁定在该类的对象上。
#### 4、对于同步代码块，要看清楚什么对象已经用于锁定（synchronized后面括号的内容）。在同一个对象上进行同步的线程将彼此阻塞，在不同对象上锁定的线程将永远不会彼此阻塞。


### 五、何时需要同步
在多个线程同时访问互斥（可交换）数据时，应该同步以保护数据，确保两个线程不会同时修改更改它。

对于非静态字段中可更改的数据，通常使用非静态方法访问。

对于静态字段中可更改的数据，通常使用静态方法访问。

如果需要在非静态方法中使用静态字段，或者在静态字段中调用非静态方法，问题将变得非常复杂。

### 六、线程安全类

当一个类已经很好的同步以保护它的数据时，这个类就称为“线程安全的”。

即使是线程安全类，也应该特别小心，因为操作的线程之间仍然不一定安全。

举个形象的例子，比如一个集合是线程安全的，有两个线程在操作同一个集合对象，当第一个线程查询集合非空后，删除集合中所有元素的时候。第二个线程也来执行与第一个线程相同的操作，也许在第一个线程查询后，第二个线程也查询出集合非空，但是当第一个执行清除后，第二个再执行删除显然是不对的，因为此时集合已经为空了。

举个例子：

```java
public class NameList {  
    private List nameList = Collections.synchronizedList(newLinkedList());  
   
    public void add(String name) {  
        nameList.add(name);  
    }  
   
    public String removeFirst() {  
       if (nameList.size()>0) {  
       return (String) nameList.remove(0);  
       } else {  
           return null;  
       }  
    }    
}  
   
public class TestNameList {  
    public static void main(String[] args) {  
        final NameList nl =new NameList();  
         nl.add("苏东坡");  
         class NameDropper extends Thread{  
           @Override  
           public void run() {  
              String name = nl.removeFirst();  
                System.out.println(name);  
           }          
         }  
         Thread t1= new NameDropper();  
         Thread t2= new NameDropper();  
         t1.start();  
         t2.start();  
    }  
}
```

执行结果：
```java
苏东坡
null
```

 虽然集合对象
 
 ```java
private List nameList = Collections.synchronizedList(new LinkedList());
```

是同步的，但是程序还不是线程安全的。

出现这种事件的原因是，上例中一个线程操作列表过程中无法阻止另外一个线程对列表的其他操作。

解决上面问题的办法是，在操作集合对象的NameList上面做一个同步。改写后的代码如下：

```java
public class NameList {  
    private List nameList = Collections.synchronizedList(newLinkedList());  
   
    public synchronized void add(String name) {  
        nameList.add(name);  
    }  
   
    public synchronized StringremoveFirst() {  
       if (nameList.size()>0) {  
        return (String) nameList.remove(0);  
       } else {  
           return null;  
       }  
    }    
}
```
这样，当一个线程访问其中一个同步方法时，其他线程只有等待。

### 七、线程死锁   
死锁对Java程序来说，是很复杂的，也很难发现问题。当两个线程被阻塞，每个线程在等待另一个线程时就发生死锁。

还是看一个比较直观的死锁例子：

```java
public class Deadlock {  
    private static class Resource{  
       public int value;  
    }  
    private Resource resourceA = new Resource();  
    private Resource resourceB = new Resource();  
    public int read(){  
       synchronized (resourceA) {  
           synchronized (resourceB) {  
              return resourceB.value+resourceA.value;  
           }  
       }  
    }  
    public void write(int a,int b){  
       synchronized(resourceB){  
           synchronized (resourceA) {  
              resourceA.value=a;  
              resourceB.value=b;  
           }  
       }  
    }  
}
```
假设read()方法由一个线程启动，write()方法由另外一个线程启动。读线程将拥有resourceA锁，写线程将拥有resourceB锁，两者都坚持等待的话就出现死锁。

实际上，上面这个例子发生死锁的概率很小。因为在代码内的某个点，CPU必须从读线程切换到写线程，所以，死锁基本上不能发生。

但是，无论代码中发生死锁的概率有多小，一旦发生死锁，程序就死掉。有一些设计方法能帮助避免死锁，包括始终按照预定义的顺序获取锁这一策略。已经超出SCJP的考试范围。

### 八、线程同步小结          
#### 1、线程同步的目的是为了保护多个线程访问一个资源时对资源的破坏。
#### 2、线程同步方法是通过锁来实现，每个对象都有切仅有一个锁，这个锁与一个特定的对象关联，线程一旦获取了对象锁，其他访问该对象的线程就无法再访问该对象的其他同步方法。
#### 3、对于静态同步方法，锁是针对这个类的，锁对象是该类的Class对象。静态和非静态方法的锁互不干预。一个线程获得锁，当在一个同步方法中访问另外对象上的同步方法时，会获取这两个对象锁。
#### 4、对于同步，要时刻清醒在哪个对象上同步，这是关键。
#### 5、编写线程安全的类，需要时刻注意对多个线程竞争访问资源的逻辑和安全做出正确的判断，对“原子”操作做出分析，并保证原子操作期间别的线程无法访问竞争资源。
#### 6、当多个线程等待一个对象锁时，没有获取到锁的线程将发生阻塞。
#### 7、死锁是线程间相互等待锁锁造成的，在实际中发生的概率非常的小。真让你写个死锁程序，不一定好使，呵呵。但是，一旦程序发生死锁，程序将死掉。

## Java线程：线程的交互

 线程交互是比较复杂的问题，SCJP要求不很基础：给定一个场景，编写代码来恰当使用等待、通知和通知所有线程。
 
### 一、线程交互的基础知识

SCJP所要求的线程交互知识点需要从java.lang.Object的类的三个方法来学习：

```java
void notify() -> 唤醒在此对象监视器上等待的单个线程。  
void notifyAll() -> 唤醒在此对象监视器上等待的所有线程。  
void wait() -> 导致当前的线程等待，直到其他线程调用此对象的 notify()方法或 notifyAll()方法。
```

当然，wait()还有另外两个重载方法：

```java
void wait(longtimeout) -> 导致当前的线程等待，直到其他线程调用此对象的 notify()方法或 notifyAll()方法，或者超过指定的时间量。  
void wait(longtimeout, int nanos) -> 导致当前的线程等待，直到其他线程调用此对象的 notify()方法或 notifyAll()方法，或者其他某个线程中断当前线程，或者已超过某个实际时间量。  
```

以上这些方法是帮助线程传递线程关心的时间状态。

关于等待/通知，要记住的关键点是：

必须从同步环境内调用wait()、notify()、notifyAll()方法。线程不能调用对象上等待或通知的方法，除非它拥有那个对象的锁。

wait()、notify()、notifyAll()都是Object的实例方法。与每个对象具有锁一样，每个对象可以有一个线程列表，他们等待来自该信号（通知）。线程通过执行对象上的wait()方法获得这个等待列表。从那时候起，它不再执行任何其他指令，直到调用对象的notify()方法为止。如果多个线程在同一个对象上等待，则将只选择一个线程（不保证以何种顺序）继续执行。如果没有线程等待，则不采取任何特殊操作。

下面看个例子就明白了：

```java
/** 
 * 计算输出其他线程锁计算的数据 
 */  
public class ThreadA {  
    public static void main(String[] args) {  
       ThreadB b=new ThreadB();  
       //启动计算线程  
       b.start();  
       //线程A拥有b对象上的锁。线程为了调用wait()或notify()方法，该线程必须是那个对象锁的拥有者  
       synchronized (b) {  
           try {  
              System.out.println("等待对象b完成计算......");  
              b.wait();  
           } catch (InterruptedException e) {  
              e.printStackTrace();  
           }  
           System.out.println("b对象计算的总和是：" + b.total);  
       }  
    }  
}  
   
/** 
 * 计算1+2+3+...+100的和 
 */  
public class ThreadB extends Thread {  
    int total;  
    public void run(){  
       synchronized (this) {  
           for (int i=0;i<101;i++){  
              total+=i;  
           }  
           //（完成计算了）唤醒在此对象监视器上等待的单个线程，在本例中线程A被唤醒  
           notify();  
       }  
    }  
}
```     

执行结果：

```java
等待对象b完成计算......  
b对象计算的总和是：5050
```

千万注意：

当在对象上调用wait()方法时，执行该代码的线程立即放弃它在对象上的锁。然而调用notify()时，并不意味着这时线程会放弃其锁。如果线程荣然在完成同步代码，则线程在移出之前不会放弃锁。因此，只要调用notify()并不意味着这时该锁变得可用。

### 二、多个线程在等待一个对象锁时候使用notifyAll()

 在多数情况下，最好通知等待某个对象的所有线程。如果这样做，可以在对象上使用notifyAll()让所有在此对象上等待的线程冲出等待区，返回到可运行状态。
 
 举个例子：
 
```java
/** 
 * 计算线程 
 */  
public class Calculator extends Thread {  
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
   
/** 
 * 获取计算结果并输出 
 */  
public class ReaderResult extends Thread {  
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
    public static void main(String[] args) {  
       Calculator calculator=new Calculator();  
       //启动三个线程，分别获取计算结果  
       new ReaderResult(calculator).start();  
       new ReaderResult(calculator).start();  
       new ReaderResult(calculator).start();  
       //启动计算线程  
       calculator.start();  
    }  
}
```
执行结果：

```java
Thread[Thread-1,5,main]等待计算结果......  
Thread[Thread-2,5,main]等待计算结果......  
Thread[Thread-3,5,main]等待计算结果......  
Exception in thread"Thread-0" java.lang.IllegalMonitorStateException  
    atjava.lang.Object.notifyAll(Native Method)  
    attest.Calculator.run(Calculator.java:15)  
Thread[Thread-3,5,main]计算结果为：5050  
Thread[Thread-2,5,main]计算结果为：5050  
Thread[Thread-1,5,main]计算结果为：5050  
```

运行结果表明，程序中有异常，并且多次运行结果可能有多种输出结果。这就是说明，这个多线程的交互程序还存在问题。究竟是出了什么问题，需要深入的分析和思考，下面将做具体分析。

实际上，上面这个代码中，我们期望的是读取结果的线程在计算线程调用notifyAll()之前等待即可。但是，如果计算线程先执行，并在读取结果线程等待之前调用了notify()方法，那么又会发生什么呢？这种情况是可能发生的。因为无法保证线程的不同部分将按照什么顺序来执行。幸运的是当读取线程运行时，它只能马上进入等待状态----它没有做任何事情来检查等待的事件是否已经发生。 ----因此，如果计算线程已经调用了notifyAll()方法，那么它就不会再次调用notifyAll()，----并且等待的读取线程将永远保持等待。这当然是开发者所不愿意看到的问题。

因此，当等待的事件发生时，需要能够检查notifyAll()通知事件是否已经发生。

通常，解决上面问题的最佳方式是利用某种循环，该循环检查某个条件表达式，只有当正在等待的事情还没有发生的情况下，它才继续等待。

## Java线程：线程的调度-休眠
Java线程调度是Java多线程的核心，只有良好的调度，才能充分发挥系统的性能，提高程序的执行效率。

这里要明确的一点，不管程序员怎么编写调度，只能最大限度的影响线程执行的次序，而不能做到精准控制。

线程休眠的目的是使线程让出CPU的最简单的做法之一，线程休眠时候，会将CPU资源交给其他线程，以便能轮换执行，当休眠一定时间后，线程会苏醒，进入准备状态等待执行。
  
线程休眠的方法是Thread.sleep(long millis)和Thread.sleep(long millis, int nanos)，均为静态方法，那调用sleep休眠的哪个线程呢？简单说，哪个线程调用sleep，就休眠哪个线程。


```java
/** 
 * Java线程：线程的调度-休眠 
 */  
public class TestSleep {  
    public static void main(String[] args) {  
       Thread t1=new MyThread1();  
       Thread t2=new Thread(new MyRunnable());  
       t1.start();  
       t2.start();  
    }  
}  
class MyThread1 extends Thread{  
    @Override  
    public void run() {  
       for(int i=0;i<3;i++){  
           System.out.println("线程1第"+i+"次执行！");  
           try {  
              Thread.sleep(50);  
           } catch (InterruptedException e) {  
              e.printStackTrace();  
           }  
       }  
    }    
}  
class MyRunnable implements Runnable{  
    @Override  
    public void run() {       
       for(int i=0;i<3;i++){  
           System.out.println("线程2第"+i+"次执行！");  
           try {  
              Thread.sleep(50);  
           } catch (InterruptedException e) {  
              e.printStackTrace();  
           }  
       }  
    }    
}  
```
执行结果：

```java
线程1第0次执行！  
线程2第0次执行！  
线程2第1次执行！  
线程1第1次执行！  
线程2第2次执行！  
线程1第2次执行！ 
```
从上面的结果输出可以看出，无法精准保证线程执行次序。

## Java线程：线程的调度-优先级

与线程休眠类似，线程的优先级仍然无法保障线程的执行次序。只不过，优先级高的线程获取CPU资源的概率较大，优先级低的并非没机会执行。

线程的优先级用1-10之间的整数表示，数值越大优先级越高，默认的优先级为5。

在一个线程中开启另外一个新线程，则新开线程称为该线程的子线程，子线程初始优先级与父线程相同。

```java
/** 
 * Java线程：线程的调度-优先级 
 */  
public class TestPriority {  
    public static void main(String[] args) {  
       Thread t1=new MyThread1();  
       Thread t2=new Thread(new MyRunnable());  
       t1.setPriority(10);  
       t2.setPriority(1);  
       t1.start();  
       t2.start();  
    }  
}  
class MyThread1 extends Thread{  
    @Override  
    public void run() {  
       for(int i=0;i<10;i++){  
           System.out.println("线程1第"+i+"次执行！");  
           try {  
              Thread.sleep(100);  
           } catch (InterruptedException e) {  
              e.printStackTrace();  
           }  
       }  
    }    
}  
class MyRunnable implements Runnable{  
    @Override  
    public void run() {       
       for(int i=0;i<10;i++){  
           System.out.println("线程2第"+i+"次执行！");  
           try {  
              Thread.sleep(100);  
           } catch (InterruptedException e) {  
              e.printStackTrace();  
           }  
       }  
    }    
}  
```
执行结果：

```java
线程1第0次执行！  
线程1第1次执行！  
线程1第2次执行！  
线程2第0次执行！  
线程1第3次执行！  
线程2第1次执行！  
线程1第4次执行！  
线程2第2次执行！  
线程1第5次执行！  
线程2第3次执行！  
线程1第6次执行！  
线程2第4次执行！  
线程1第7次执行！  
线程2第5次执行！  
线程1第8次执行！  
线程2第6次执行！  
线程1第9次执行！  
线程2第7次执行！
线程2第8次执行！
线程2第9次执行！
``` 

## Java线程：线程的调度-让步
线程的让步含义就是使当前运行着线程让出CPU资源，但是让给谁不知道，仅仅是让出，线程状态回到可运行状态。

线程的让步使用Thread.yield()方法，yield()为静态方法，功能是暂停当前正在执行的线程对象，并执行其他线程

```java
/** 
 * Java线程：线程的调度-让步 
 */  
public class Test {  
    public static void main(String[] args) {  
       Thread t1=new MyThread1();  
       Thread t2=new Thread(new MyRunnable());  
       t1.start();  
       t2.start();  
    }  
}  
class MyThread1 extends Thread{  
    @Override  
    public void run() {  
       for(int i=0;i<10;i++){  
           System.out.println("线程1第"+i+"次执行！");          
       }  
    }    
}  
class MyRunnable implements Runnable{  
    @Override  
    public void run() {       
       for(int i=0;i<10;i++){  
           System.out.println("线程2第"+i+"次执行！");  
           Thread.yield();  
       }  
    }    
}
```
执行结果：

```java
线程2第0次执行！  
线程1第0次执行！  
线程1第1次执行！  
线程1第2次执行！  
线程1第3次执行！  
线程1第4次执行！  
线程1第5次执行！  
线程1第6次执行！  
线程1第7次执行！  
线程1第8次执行！  
线程1第9次执行！  
线程2第1次执行！  
线程2第2次执行！  
线程2第3次执行！  
线程2第4次执行！  
线程2第5次执行！  
线程2第6次执行！  
线程2第7次执行！  
线程2第8次执行！  
线程2第9次执行！
```
## Java线程：线程的调度-合并

线程的合并的含义就是将几个并行线程的线程合并为一个单线程执行，应用场景是当一个线程必须等待另一个线程执行完毕才能执行时可以使用join方法。

join为非静态方法，定义如下：

```java
void join()——等待该线程终止。     
void join(longmillis)——等待该线程终止的时间最长为 millis毫秒。     
void join(longmillis,int nanos)——等待该线程终止的时间最长为 millis毫秒 + nanos 纳秒。  
```

```java
/** 
 * Java线程：线程的调度-合并 
 */  
public class Test {  
    public static void main(String[] args) {  
       Thread t1=new MyThread1();       
       t1.start();  
       for (int i = 0; i < 20; i++) {  
           System.out.println("主线程第" + i +"次执行！");  
           if (i>2) {  
              try {  
                  ///t1线程合并到主线程中，主线程停止执行过程，转而执行t1线程，直到t1执行完毕后继续。  
                  t1.join();  
              } catch (InterruptedException e) {  
                  e.printStackTrace();  
              }  
           }  
       }  
    }  
}  
class MyThread1 extends Thread{  
    @Override  
    public void run() {  
       for(int i=0;i<10;i++){  
           System.out.println("线程1第"+i+"次执行！");          
       }  
    }    
} 
```

执行结果：

```java
主线程第0次执行！  
主线程第1次执行！  
主线程第2次执行！  
主线程第3次执行！  
线程1第0次执行！  
线程1第1次执行！  
线程1第2次执行！  
线程1第3次执行！  
线程1第4次执行！  
线程1第5次执行！  
线程1第6次执行！  
线程1第7次执行！  
线程1第8次执行！  
线程1第9次执行！  
主线程第4次执行！  
主线程第5次执行！  
主线程第6次执行！  
主线程第7次执行！  
主线程第8次执行！  
主线程第9次执行！  
主线程第10次执行！  
主线程第11次执行！  
主线程第12次执行！  
主线程第13次执行！  
主线程第14次执行！  
主线程第15次执行！  
主线程第16次执行！  
主线程第17次执行！  
主线程第18次执行！  
主线程第19次执行！  
``` 

## Java线程：线程的调度-守护线程
守护线程与普通线程写法上基本么啥区别，调用线程对象的方法setDaemon(true)，则可以将其设置为守护线程。

 守护线程使用的情况较少，但并非无用，举例来说，JVM的垃圾回收、内存管理等线程都是守护线程。还有就是在做数据库应用时候，使用的数据库连接池，连接池本身也包含着很多后台线程，监控连接个数、超时时间、状态等等。
 
 setDaemon方法的详细说明：
 
 ```java
// 将该线程标记为守护线程或用户线程。当正在运行的线程都是守护线程时，Java虚拟机退出。  
public final void setDaemon(boolean on)
```

该方法必须在启动线程前调用。

该方法首先调用该线程的 checkAccess方法，且不带任何参数。这可能抛出 SecurityException（在当前线程中）。

参数：on - 如果为true，则将该线程标记为守护线程。

抛出：

IllegalThreadStateException- 如果该线程处于活动状态。

SecurityException- 如果当前线程无法修改该线程。

另请参见：

isDaemon(), checkAccess()


```java
/** 
 * Java线程：线程的调度-守护线程 
 */  
public class Test {  
    public static void main(String[] args) {  
       Thread t1=new MyCommon();  
       Thread t2=new Thread(new MyDaemon());  
       t2.setDaemon(true);//设置为守护线程  
       t2.start();  
       t1.start();        
    }  
}  
class MyCommon extends Thread{  
    @Override  
    public void run() {  
       for(int i=0;i<5;i++){  
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
```

执行结果：

```java
线程1第0次执行！  
后台线程第0次执行！  
后台线程第1次执行！  
线程1第1次执行！  
后台线程第2次执行！  
线程1第2次执行！  
后台线程第3次执行！  
线程1第3次执行！  
后台线程第4次执行！  
线程1第4次执行！  
后台线程第5次执行！  
后台线程第6次执行！  
后台线程第7次执行！  
后台线程第8次执行！  
后台线程第9次执行！  
后台线程第10次执行！
```
从上面的执行结果可以看出：

前台线程是保证执行完毕的，后台线程还没有执行完毕就退出了。 

实际上：JRE判断程序是否执行结束的标准是所有的前台执线程行完毕了，而不管后台线程的状态，因此，在使用后台县城时候一定要注意这个问题。

