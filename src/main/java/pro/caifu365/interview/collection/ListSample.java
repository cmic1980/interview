package pro.caifu365.interview.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ListSample {
    public static void main(String[] args) {
        ConcurrentHashMap map = new ConcurrentHashMap();
        for (int i = 0; i < 30; i++) {
            map.put("k" + i, "s" + i);
        }

        map.get("k1");

        ArrayList<Integer> list1 = new ArrayList();
        list1.add(1);
        list1.add(2);
        list1.add(2);
        list1.add(3);
        list1.add(4);

        ArrayList<Integer> list2 = new ArrayList();
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list2.add(6);

        // list1.retainAll(list2);
        // list1 = null;

        list2.removeAll(list1);
        list1.addAll(list2);
        list1 = null;


    }


}
