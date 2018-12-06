package pro.caifu365.interview.thread;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TestNameList {
    public static void main(String[] args) {
        TestNameList testNameList = new TestNameList();
        testNameList.run();
    }

    class NameList {
        // private List nameList = Collections.synchronizedList(new LinkedList());
        private List nameList = new LinkedList();
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

    public void run(){
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
