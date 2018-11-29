# Java的基础数据类型（Primitive）

有一系列类需特别对待；可将它们想象成“基本”、“主要”或者“主”（Primitive）类型，进行程序设计时要频繁用到它们。

| 类型   |      bit位数      |  值范围 | 默认值 | 
|----------|:-------------:|------:|------:|
|boolean |1| true/false |false|
| char | 16| 占用2个字节，采用unicode编码 (\u0000~u\FFFF) |\u0000|
| byte | 8| -128~127 |0|
| short | 16| -32768~32767 |0|
| int | 32|-2^31到2^31-1之间的任意整数 |0|
| long | 64|-2^63到2^63-1之间的任意整数|0L|
| float | 32|根据IEEE754-1985标准 |0.0f|
| double | 64|根据IEEE754-1985标准|0.0d|




## 1. BigInteger和BigDecimal
Java1.1增加了两个类，用于进行高精度的计算：BigInteger和BigDecimal。
尽管它们大致可以划分为“封装器”类型，但两者都没有对应的“主类型”。
这两个类都有自己特殊的“方法”，对应于我们针对主类型执行的操作。
也就是说，能对int 或float 做的事情，对BigInteger 和BigDecimal 一样可以做。只是必须使用方法调用，不能使用运算符。
此外，由于牵涉更多，所以运算速度会慢一些。我们牺牲了速度，但换来了精度。
BigInteger 支持任意精度的整数。也就是说，我们可精确表示任意大小的整数值，同时在运算过程中不会丢失任何信息。BigDecimal 支持任意精度的定点数字。例如，可用它进行精确的币值计算。



## 2. 基本数据类型位置
基本数据类型是放在栈中还是放在堆中，这取决于基本类型声明的位置

- 在方法中声明的变量，即该变量是局部变量，每当程序调用方法时，系统都会为该方法建立一个方法栈，
其所在方法中声明的变量就放在方法栈中，当方法结束系统会释放方法栈，
其对应在该方法中声明的变量随着栈的销毁而结束，这就局部变量只能在方法中有效的原因。

 在方法中声明的变量可以是基本类型的变量，也可以是引用类型的变量

（1）当声明是基本类型的变量的时，其变量名及值（变量名及值是两个概念）是放在方法栈中
 
（2）当声明的是引用变量时，所声明的变量（该变量实际上是在方法中存储的是内存地址值）是放在方法的栈中，该变量所指向的对象是放在堆类存中的。

- 在类中声明的变量是成员变量，也叫全局变量，放在堆中的（因为全局变量不会随着某个方法执行结束而销毁）。

同样在类中声明的变量即可是基本类型的变量 也可是引用类型的变量

（1）当声明的是基本类型的变量其变量名及其值放在堆内存中的

（2）引用类型时，其声明的变量仍然会存储一个内存地址值，该内存地址值指向所引用的对象。引用变量名和对应的对象仍然存储在相应的堆中

## 3. 基本类型封装类
- 八种基本数据类都有它们的封装类分别是：Integer、Short、Float、Double、Long、Boolean、Byte、Character。

- 原始数据类型在传递参数时都是按值传递，封装类都是按引用传递。
- "=="和"equal（）"方法：

（1）基本型和基本型封装型进行“==”运算符的比较，基本型封装型将会自动拆箱变为基本型后再进行比较，
因此Integer(0)会自动拆箱为int类型再进行比较，显然返回true。

（2）两个Integer类型进行“==”比较，如果其值在-128至127，那么返回true，否则返回false, 
跟Integer.valueOf()的缓冲对象有关。
    
    ```java
    @HotSpotIntrinsicCandidate
    public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
    }
    
    private static class IntegerCache {
        static final int low = -128;
        static final int high;
        static final Integer cache[];

        static {
            // high value may be configured by property
            int h = 127;
            String integerCacheHighPropValue =
                VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
            if (integerCacheHighPropValue != null) {
                try {
                    int i = parseInt(integerCacheHighPropValue);
                    i = Math.max(i, 127);
                    // Maximum array size is Integer.MAX_VALUE
                    h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
                } catch( NumberFormatException nfe) {
                    // If the property cannot be parsed into an int, ignore it.
                }
            }
            high = h;

            cache = new Integer[(high - low) + 1];
            int j = low;
            for(int k = 0; k < cache.length; k++)
                cache[k] = new Integer(j++);

            // range [-128, 127] must be interned (JLS7 5.1.7)
            assert IntegerCache.high >= 127;
        }

        private IntegerCache() {}
    }
        
    ```

（3）两个封装类进行equals()比较，首先equals()会比较类型，如果类型相同，则继续比较值，如果值也相同，返回true。

（4）基本型封装类型调用equals(),但是参数是基本类型，这时候，先会进行自动装箱，基本型转换为其封装类型，再进行3中的比较。

**综上所述：除了两个int类型比较使用==外，Integer和int或两个Integer之间比较相等时都应该使用equals函数，
因为Integer.valueOf()的缓冲可能会引起你意料之外的结果。**
  


  

　





