# 如何简单理解GC roots和 gc的标记过程

## 基本思路
 
 通过一系列的称为“GC Roots”的对象作为起始点，从这些节点开始向下搜索，搜索所走过的路径称为引用链（Reference Chain），当一个对象到GC Roots没有任何引用链相连时，则证明此对象是不可用的。 
 
 ![avatar](https://img-blog.csdn.net/20180417100126811?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xpbl93ajE5OTU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
 
 
## 哪些可以作为GC Roots
 - 虚拟机栈（栈帧中的本地变量表）中引用的对象。
 - 方法区中类静态属性引用的对象。
 - 方法区中常量引用的对象。
 - 本地方法栈中JNI（即一般说的Native方法）引用的对象。
 
## 对象自我拯救
注意，当一个对象实例被标记为不可达对象时，并不是一定会被回收，只是将该对象添加到回收的列表中。 

那当被添加到回收列表中时，如果将自身从该列表移除呢？也就是说将该对象重新变成有用的对象呢？ 

Java Object类提供了一个对象自我救赎的方法：

```java
protected void finalize();//当垃圾回收器确定不存在对该对象的更多引用时，由对象的垃圾回收器调用此方法。
```

如果对象要在 finalize() 中成功拯救自己——只要重新与引用链上的任何一个对象建立关联即可，譬如把自己（this关键字）赋值给某个类变量或者对象的成员变量。 



