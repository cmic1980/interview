package pro.caifu365.interview.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Stack<T> {
    ArrayList arrayList = new ArrayList();

    public void put(T t) {
        arrayList.add(t);
    }

    public void putAll(List<? super T> l) {
        arrayList.addAll(l);
    }

    public static void main(String[] args) {
        Stack<Number> stack = new Stack<>();
        Integer i = 1;
        stack.put(i);

        List<Object> list = new ArrayList<>();
        list.add(i);

        stack.putAll(list);

        Integer i1 = new Integer(5);
        Integer i2 = new Integer(5);

        Integer i3 = 300;
        int i4 = 300;

        Integer i5 = Integer.valueOf(200);
        Integer i6 = Integer.valueOf(200);

        Integer i7 = Integer.valueOf(100);
        int i8 = 100;;

        System.out.println(i1 == i2);
        System.out.println(i3.equals(i4));
        System.out.println(i5==i6);
        System.out.println(i7.equals(i8));

    }

    public static <E extends Comparable<? super E>> Set<E> uninon() {
        Set<E> set = null;
        return set;
    }
}
