package pro.caifu365.interview.string;

public class Sample {
    public static void main(String[] args) {
        String s1 = "chenssy";
        String s2 = "chenssy";


        System.out.println("字符串比较，两个字符串使用==操作符比较，结果：" + (s1 == s2));
        System.out.println("字符串比较，两个字符串使用equals函数比较，结果：" + s1.equals(s2));

        String s3 = new String("chenssy");
        System.out.println("字符串比较，两个字符串使用==操作符比较，结果：" + (s1 == s3));
        System.out.println("字符串比较，两个字符串使用equals函数比较，结果：" + s1.equals(s3));

        s1.intern();
    }
}
