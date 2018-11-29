package pro.caifu365.interview.primitive;


public class Sample {

    public static void main(String[] args) {
        int i11 = 1;
        int i12 = 1;
        System.out.println("1. 两个int == 操作符比较，结果:" + (i11==i12));
        Integer i13 = 1;
        System.out.println("2. 一个int和一个Integer == 操作符比较时，Integer会自动拆箱为int，之后就是两个int的==操作符比较了，结果:" + (i11==i13));

        Integer i21 = 127;
        Integer i22 = 127;
        System.out.println("3. 两个Integer == 操作符比较，当Integer小于128时（Integer.valueOf()的缓冲对象有关，缓存-128到127之间数字），结果:" + (i21==i22));

        i21 = 128;
        i22 = 128;
        System.out.println("4. 两个Integer == 操作符比较，当Integer大于128时（Integer.valueOf()的缓冲对象有关，缓存-128到127之间数字），结果:" + (i21==i22));


        Integer i31 = 1;
        Integer i32 = 1;
        System.out.println("5. 两个Integer使用equals函数比较，实际上是使用它们的封装int的值做==操作符比较，之后就是两个int的==操作符比较了， 结果:" + (i31.equals(i32)));

        int i33 = 1;
        System.out.println("6. 一个Integer使用equals函数一个int比较，int会被自动装箱为Integer，之后同5相同，结果:" + (i31.equals(i33)));
    }
}
