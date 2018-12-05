# 转载------------java 进程创建

作者：WitsMakeMen

出处：http://blog.csdn.net/witsmakemen/

声明：本文采用以下协议进行授权： 自由转载-非商用-非衍生-保持署名|Creative Commons BY-NC-ND 3.0 ，转载请注明作者及出处。

## Java创建进程

### 1 进程的概念 

#### 1.1 进程的概念 

进程是操作系统结构的基础；是一个正在执行的程序；计算机中正在运行的程序实例；可以分配给处理器并由处理器执行的一个实体；由单一顺序的执行显示，一个当前状态和一组相关的系统资源所描述的活动单元。 

第一，进程是一个实体。每一个进程都有它自己的地址空间，一般情况下，包括文本区域（text region）、数据区域（data region）和堆栈（stack region）。文本区域存储处理器执行的代码；数据区域存储变量和进程执行期间使用的动态分配的内存；堆栈区域存储着活动过程调用的指令和本地变量。 
  
第 二，进程是一个“执行中的程序”。程序是一个没有生命的实体，只有处理器赋予程序生命时，它才能成为一个活动的实体，我们称其为进程。 

#### 1.2 进程的特征
动态性：进程的实质是程序在多道程序系统中的一次执行过程，进程是动态产生，动态消亡的。 

并发性：任何进程都可以同其他进程一起并发执行
 
独立性：进程是一个能独立运行的基本单位，同时也是系统分配资源和调度的独立单位； 

异步性：由于进程间的相互制约，使进程具有执行的间断性，即进程按各自独立的、不可预知的速度向前推进 

结构特征：进程由程序、数据和进程控制块三部分组成。 

多个不同的进程可以包含相同的程序：一个程序在不同的数据集里就构成不同的进程，能得到不同的结果；但是执行过程中，程序不能发生改变。      

#### 1.3 进程与线程区别 
进程和线程的主要差别在于它们是不同的操作系统资源管理方式。进程有独立的地址空间，一个进程崩溃后，在保护模式下不会对其它进程产生影 响，而线程只是一个进程中的不同执行路径。线程有自己的堆栈和局部变量，但线程之间没有单独的地址空间，一个线程死掉就等于整个进程死掉，所以多进程的程 序要比多线程的程序健壮，但在进程切换时，耗费资源较大，效率要差一些。但对于一些要求同时进行并且又要共享某些变量的并发操作，只能用线程，不能用进 程。 

### 2 进程的创建 
#### 2.1 Java进程的创建 

Java提供了两种方法用来启动进程或其它程序： 

（1）使用Runtime的exec()方法 

（2）使用ProcessBuilder的start()方法 

##### 2.1.1 ProcessBuilder 
ProcessBuilder类是J2SE 1.5在java.lang中新添加的一个新类，此类用于创建操作系统进程，它提供一种启动和管理进程（也就是应用程序）的方法。在J2SE 1.5之前，都是由Process类处来实现进程的控制管理。

每个 ProcessBuilder 实例管理一个进程属性集。start() 方法利用这些属性创建一个新的 Process 实例。start() 方法可以从同一实例重复调用，以利用相同的或相关的属性创建新的子进程。 

每个进程生成器管理这些进程属性：

命令 是一个字符串列表，它表示要调用的外部程序文件及其参数（如果有）。在此，表示有效的操作系统命令的字符串列表是依赖于系统的。例如，每一个总体变量，通 常都要成为此列表中的元素，但有一些操作系统，希望程序能自己标记命令行字符串——在这种系统中，Java 实现可能需要命令确切地包含这两个元素。 

环境 是从变量 到值 的依赖于系统的映射。初始值是当前进程环境的一个副本（请参阅 System.getenv()）。  

工作目录。默认值是当前进程的当前工作目录，通常根据系统属性 user.dir 来命名。 redirectErrorStream 属性。

最初，此属性为 false，意思是子进程的标准输出和错误输出被发送给两个独立的流，这些流可以通过 Process.getInputStream() 和 Process.getErrorStream() 方法来访问。如果将值设置为 true，标准错误将与标准输出合并。这使得关联错误消息和相应的输出变得更容易。在此情况下，合并的数据可从 Process.getInputStream() 返回的流读取，而从 Process.getErrorStream() 返回的流读取将直接到达文件尾。 

修改进程构建器的属性将影响后续由该对象的 start() 方法启动的进程，但从不会影响以前启动的进程或 Java 自身的进程。大多数错误检查由 start() 方法执行。可以修改对象的状态，但这样 start() 将会失败。例如，将命令属性设置为一个空列表将不会抛出异常，除非包含了 start()。 

注意，此类不是同步的。如果多个线程同时访问一个 ProcessBuilder，而其中至少一个线程从结构上修改了其中一个属性，它必须 保持外部同步。


```java
构造方法摘要  
ProcessBuilder(List<String> command)   
          利用指定的操作系统程序和参数构造一个进程生成器。    
ProcessBuilder(String... command)   
          利用指定的操作系统程序和参数构造一个进程生成器。    
  
方法摘要  
 List<String> command()   
          返回此进程生成器的操作系统程序和参数。  
 ProcessBuilder command(List<String> command)   
          设置此进程生成器的操作系统程序和参数。  
 ProcessBuilder command(String... command)   
          设置此进程生成器的操作系统程序和参数。  
 File directory()   
          返回此进程生成器的工作目录。  
 ProcessBuilder directory(File directory)   
          设置此进程生成器的工作目录。  
 Map<String,String> environment()   
          返回此进程生成器环境的字符串映射视图。  
 boolean redirectErrorStream()   
          通知进程生成器是否合并标准错误和标准输出。  
 ProcessBuilder redirectErrorStream(boolean redirectErrorStream)   
          设置此进程生成器的 redirectErrorStream 属性。  
 Process start()   
          使用此进程生成器的属性启动一个新进程。  
```
 
 ##### 2.1.2 Runtime 
 每个 Java 应用程序都有一个 Runtime 类实例，使应用程序能够与其运行的环境相连接。可以通过 getRuntime 方法获取当前运行时。 
 应用程序不能创建自己的 Runtime 类实例。但可以通过 getRuntime 方法获取当前Runtime运行时对象的引用。一旦得到了一个当前的Runtime对象的引用，就可以调用Runtime对象的方法去控制Java虚拟机的状态和行为。 
 
 ```java
void addShutdownHook(Thread hook)   
          注册新的虚拟机来关闭挂钩。  
 int availableProcessors()   
          向 Java 虚拟机返回可用处理器的数目。  
 Process exec(String command)   
          在单独的进程中执行指定的字符串命令。  
 Process exec(String[] cmdarray)   
          在单独的进程中执行指定命令和变量。  
 Process exec(String[] cmdarray, String[] envp)   
          在指定环境的独立进程中执行指定命令和变量。  
 Process exec(String[] cmdarray, String[] envp, File dir)   
          在指定环境和工作目录的独立进程中执行指定的命令和变量。  
 Process exec(String command, String[] envp)   
          在指定环境的单独进程中执行指定的字符串命令。  
 Process exec(String command, String[] envp, File dir)   
          在有指定环境和工作目录的独立进程中执行指定的字符串命令。  
 void exit(int status)   
          通过启动虚拟机的关闭序列，终止当前正在运行的 Java 虚拟机。  
 long freeMemory()   
          返回 Java 虚拟机中的空闲内存量。  
 void gc()   
          运行垃圾回收器。  
 InputStream getLocalizedInputStream(InputStream in)   
          已过时。 从 JDK 1.1 开始，将本地编码字节流转换为 Unicode 字符流的首选方法是使用 InputStreamReader 和 BufferedReader 类。  
 OutputStream getLocalizedOutputStream(OutputStream out)   
          已过时。 从 JDK 1.1 开始，将 Unicode 字符流转换为本地编码字节流的首选方法是使用 OutputStreamWriter、BufferedWriter 和 PrintWriter 类。  
static Runtime getRuntime()   
          返回与当前 Java 应用程序相关的运行时对象。  
 void halt(int status)   
          强行终止目前正在运行的 Java 虚拟机。  
 void load(String filename)   
          加载作为动态库的指定文件名。  
 void loadLibrary(String libname)   
          加载具有指定库名的动态库。  
 long maxMemory()   
          返回 Java 虚拟机试图使用的最大内存量。  
 boolean removeShutdownHook(Thread hook)   
          取消注册某个先前已注册的虚拟机关闭挂钩。  
 void runFinalization()   
          运行挂起 finalization 的所有对象的终止方法。  
static void runFinalizersOnExit(boolean value)   
          已过时。 此方法本身具有不安全性。它可能对正在使用的对象调用终结方法，而其他线程正在操作这些对象，从而导致不正确的行为或死锁。  
 long totalMemory()   
          返回 Java 虚拟机中的内存总量。  
 void traceInstructions(boolean on)   
          启用／禁用指令跟踪。  
 void traceMethodCalls(boolean on)   
          启用／禁用方法调用跟踪。  

```  

##### 2.1.3 Process
不管通过那种方法启动进程后，都会返回一个Process类的实例代表启动的进程，该实例可用来控制进程并获得相关信息。Process 类提供了执行从进程输入、执行输出到进程、等待进程完成、检查进程的退出状态以及销毁（杀掉）进程的方法： 

```java
void destroy()   
          杀掉子进程。  
         一般情况下，该方法并不能杀掉已经启动的进程，不用为好。  
int exitValue()   
          返回子进程的出口值。   
          只有启动的进程执行完成、或者由于异常退出后，exitValue()方法才会有正常的返回值，否则抛出异常。  
InputStream getErrorStream()   
          获取子进程的错误流。  
         如果错误输出被重定向，则不能从该流中读取错误输出。  
InputStream getInputStream()   
          获取子进程的输入流。  
          可以从该流中读取进程的标准输出。  
OutputStream getOutputStream()   
          获取子进程的输出流。  
          写入到该流中的数据作为进程的标准输入。  
int waitFor()   
          导致当前线程等待，如有必要，一直要等到由该 Process 对象表示的进程已经终止。  
```

通过该类提供的方法，可以实现与启动的进程之间通信，达到交互的目的。 

#### 2.2 实例
##### 2.2.1 创建子进程 

要创建子进程可以通过使用使用ProcessBuilder的start()方法和Runtime的exec()方法。 
（１）Runtime.exec() 

```java
import java.io.BufferedReader;  
import java.io.File;  
import java.io.InputStreamReader;  
  
public class Test1 {  
public static void main(String[] args) {  
   try {  
    Process p = null;  
    String line = null;  
    BufferedReader stdout = null;  
  
    //list the files and directorys under C:\  
    p = Runtime.getRuntime().exec("CMD.exe /C dir", null, new File("C:\\"));  
    stdout = new BufferedReader(new InputStreamReader(p  
      .getInputStream()));  
    while ((line = stdout.readLine()) != null) {  
     System.out.println(line);  
    }  
    stdout.close();  
  
    //echo the value of NAME  
    p = Runtime.getRuntime().exec("CMD.exe /C echo %NAME%", new String[] {"NAME=TEST"});     
    stdout = new BufferedReader(new InputStreamReader(p  
      .getInputStream()));  
    while ((line = stdout.readLine()) != null) {  
     System.out.println(line);  
    }  
    stdout.close();  
   } catch (Exception e) {  
    e.printStackTrace();  
   }  
}
```

（２）ProcessBuilder 

```java
import java.io.BufferedReader;  
import java.io.File;  
import java.io.InputStreamReader;  
import java.util.ArrayList;  
import java.util.List;  
  
public class Test2 {  
public static void main(String[] args) {  
   try {  
    List<String> list = new ArrayList<String>();  
    ProcessBuilder pb = null;  
    Process p = null;  
    String line = null;  
    BufferedReader stdout = null;  
     
    //list the files and directorys under C:\  
    list.add("CMD.EXE");  
    list.add("/C");  
    list.add("dir");  
    pb = new ProcessBuilder(list);  
    pb.directory(new File("C:\\"));  
    p = pb.start();  
     
    stdout = new BufferedReader(new InputStreamReader(p  
      .getInputStream()));  
    while ((line = stdout.readLine()) != null) {  
     System.out.println(line);  
    }  
    stdout.close();  
  
    //echo the value of NAME  
    pb = new ProcessBuilder();  
    pb.command(new String[] {"CMD.exe", "/C", "echo %NAME%"});  
    pb.environment().put("NAME", "TEST");  
    p = pb.start();  
     
    stdout = new BufferedReader(new InputStreamReader(p  
      .getInputStream()));  
    while ((line = stdout.readLine()) != null) {  
     System.out.println(line);  
    }  
    stdout.close();  
   } catch (Exception e) {  
    e.printStackTrace();  
   }  
}
```

从启动其他程序的Java进程看，已启动的其他程序输出就是一个普通的输入流，可以通过getInputStream()和getErrorStream 来获取。对于一般输出文本的进程来说，可以将InputStream封装成BufferedReader，然后就可以一行一行的对进程的标准输出进行处 理。 

通常，一个程序/进程在执行结束后会向操作系统返回一个整数值，0一般代表执行成功，非0表示执行出现问题。有两种方式可以用来获取进程的返回值。一是利 用waitFor()，该方法是阻塞的，执导进程执行完成后再返回。该方法返回一个代表进程返回值的整数值。另一个方法是调用exitValue()方 法，该方法是非阻塞的，调用立即返回。但是如果进程没有执行完成，则抛出异常。

##### 2.2.2 进程阻塞问题 
由Process代表的进程在某些平台上有时候并不能很好的工作，特别是在对代表进程的标准输入流、输出流和错误输出进行操作时，如果使用不慎，有可能导致进程阻塞，甚至死锁。 

如果将以上事例中的从标准输出重读取信息的语句修改为从错误输出流中读取： 

stdout = new BufferedReader(new InputStreamReader(p.getErrorStream())); 

那么程序将发生阻塞，不能执行完成，而是hang在那里。 

当进程启动后，就会打开标准输出流和错误输出流准备输出，当进程结束时，就会关闭他们。在以上例子中，错误输出流没有数据要输出，标准输出流中有数据输 出。由于标准输出流中的数据没有被读取，进程就不会结束，错误输出流也就不会被关闭，因此在调用readLine()方法时，整个程序就会被阻塞。为了解 决这个问题，可以根据输出的实际先后，先读取标准输出流，然后读取错误输出流。 

但是，很多时候不能很明确的知道输出的先后，特别是要操作标准输入的时候，情况就会更为复杂。这时候可以采用线程来对标准输出、错误输出和标准输入进行分别处理，根据他们之间在业务逻辑上的关系决定读取那个流或者写入数据。 

针对标准输出流和错误输出流所造成的问题，可以使用ProcessBuilder的redirectErrorStream()方法将他们合二为一，这时候只要读取标准输出的数据就可以了。 

当在程序中使用Process的waitFor()方法时，特别是在读取之前调用waitFor()方法时，也有可能造成阻塞。可以用线程的方法来解决这个问题，也可以在读取数据后，调用waitFor()方法等待程序结束。

总之，解决阻塞的方法应该有两种：

（1）使用ProcessBuilder类，利用redirectErrorStream方法将标准输出流和错误输出流合二为一，在用start()方法启动进程后，先从标准输出中读取数据，然后调用waitFor()方法等待进程结束。 

如： 

```java
import java.io.BufferedReader;  
import java.io.File;  
import java.io.InputStreamReader;  
import java.util.ArrayList;  
import java.util.List;  
  
public class Test3 {  
public static void main(String[] args) {  
   try {  
    List<String> list = new ArrayList<String>();  
    ProcessBuilder pb = null;  
    Process p = null;  
    String line = null;  
    BufferedReader stdout = null;  
    //list the files and directorys under C:\  
    list.add("CMD.EXE");  
    list.add("/C");  
    list.add("dir1");  
    pb = new ProcessBuilder(list);  
    pb.directory(new File("C:\\"));  
    //merge the error output with the standard output  
    pb.redirectErrorStream(true);  
    p = pb.start();  
    //read the standard output  
    stdout = new BufferedReader(new InputStreamReader(p  
      .getInputStream()));  
    while ((line = stdout.readLine()) != null) {  
     System.out.println(line);  
    }  
    int ret = p.waitFor();  
    System.out.println("the return code is " + ret);  
    stdout.close();  
   } catch (Exception e) {  
    e.printStackTrace();  
   }  
}
```

（2）使用线程 

```java
import java.util.*;  
import java.io.*;  
  
class StreamWatch extends Thread {  
    InputStream is;  
    String type;  
    List<String> output = new ArrayList<String>();  
    boolean debug = false;  
    StreamWatch(InputStream is, String type) {  
       this(is, type, false);  
    }  
      
    StreamWatch(InputStream is, String type, boolean debug) {  
       this.is = is;  
       this.type = type;  
       this.debug = debug;  
    }  
      
    public void run() {  
       try {  
        PrintWriter pw = null;  
        InputStreamReader isr = new InputStreamReader(is);  
        BufferedReader br = new BufferedReader(isr);  
        String line = null;  
        while ((line = br.readLine()) != null) {  
         output.add(line);  
         if (debug)  
          System.out.println(type + ">" + line);  
        }  
        if (pw != null)  
         pw.flush();  
       } catch (IOException ioe) {  
        ioe.printStackTrace();  
       }  
    }  
      
    public List<String> getOutput() {  
       return output;  
    }  
}  
```
   
```java
public class Test5 {  
    public static void main(String args[]) {  
       try {  
        List<String> list = new ArrayList<String>();  
        ProcessBuilder pb = null;  
        Process p = null;  
        // list the files and directorys under C:\  
        list.add("CMD.EXE");  
        list.add("/C");  
        list.add("dir1");  
        pb = new ProcessBuilder(list);  
        pb.directory(new File("C:\\"));  
        p = pb.start();  
      
        // process error and output message  
        StreamWatch errorWatch = new StreamWatch(p.getErrorStream(),  
          "ERROR");  
        StreamWatch outputWatch = new StreamWatch(p.getInputStream(),  
          "OUTPUT");  
        // start to watch  
        errorWatch.start();  
        outputWatch.start();  
        //wait for exit  
        int exitVal = p.waitFor();  
        //print the content from ERROR and OUTPUT  
        System.out.println("ERROR: " + errorWatch.getOutput());  
        System.out.println("OUTPUT: " + outputWatch.getOutput());  
        System.out.println("the return code is " + exitVal);  
       } catch (Throwable t) {  
        t.printStackTrace();  
       }  
    }  
}  

```

##### 2.2.3 在java中执行java程序 

执行一个Java程序的关键在于： 

（1）知道JAVA虚拟机的位置，即java.exe或者java的路径 

（2）知道要执行的java程序的位置 

（3）知道该程序所依赖的其他类的位置 

举一个例子，一目了然。 

（1）待执行的Java类 

```java
public class MyTest {  
    public static void main(String[] args) {  
       System.out.println("OUTPUT one");  
       System.out.println("OUTPUT two");  
       System.err.println("ERROR 1");  
       System.err.println("ERROR 2");    
       for(int i = 0; i < args.length; i++)  
       {  
        System.out.printf("args[%d] = %s.", i, args[i]);  
       }  
    }  
}  
```

（2）执行该类的程序 

```java
import java.util.*;  
import java.io.*;  
  
class StreamWatch extends Thread {  
    InputStream is;  
    String type;  
    List<String> output = new ArrayList<String>();  
    boolean debug = false;  
      
    StreamWatch(InputStream is, String type) {  
       this(is, type, false);  
    }  
      
    StreamWatch(InputStream is, String type, boolean debug) {  
       this.is = is;  
       this.type = type;  
       this.debug = debug;  
    }  
      
    public void run() {  
       try {  
        PrintWriter pw = null;  
        InputStreamReader isr = new InputStreamReader(is);  
        BufferedReader br = new BufferedReader(isr);  
        String line = null;  
        while ((line = br.readLine()) != null) {  
         output.add(line);  
         if (debug)  
          System.out.println(type + ">" + line);  
        }  
        if (pw != null)  
         pw.flush();  
       } catch (IOException ioe) {  
        ioe.printStackTrace();  
       }  
    }  
      
    public List<String> getOutput() {  
       return output;  
    }  
}  

```    


```java
public class Test6 {  
    public static void main(String args[]) {  
       try {  
        List<String> list = new ArrayList<String>();  
        ProcessBuilder pb = null;  
        Process p = null;  
         
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";  
        String classpath = System.getProperty("java.class.path");  
        // list the files and directorys under C:\  
        list.add(java);  
        list.add("-classpath");  
        list.add(classpath);  
        list.add(MyTest.class.getName());  
        list.add("hello");  
        list.add("world");  
        list.add("good better best");  
         
        pb = new ProcessBuilder(list);  
        p = pb.start();  
         
        System.out.println(pb.command());  
        // process error and output message  
        StreamWatch errorWatch = new StreamWatch(p.getErrorStream(),  
          "ERROR");  
        StreamWatch outputWatch = new StreamWatch(p.getInputStream(),  
          "OUTPUT");  
        // start to watch  
        errorWatch.start();  
        outputWatch.start();  
        //wait for exit  
        int exitVal = p.waitFor();  
        //print the content from ERROR and OUTPUT  
        System.out.println("ERROR: " + errorWatch.getOutput());  
        System.out.println("OUTPUT: " + outputWatch.getOutput());  
        System.out.println("the return code is " + exitVal);  
       } catch (Throwable t) {  
        t.printStackTrace();  
       }  
    }
}
```