package pro.caifu365.interview.clone;

public class Sample {
    public static void main(String[] args) {
        Order o1 = new Order();
        o1.setId(1);

        Product p1 = new Product();
        p1.setId(1);
        p1.setName("测试1");
        o1.setProduct(p1);
        System.out.println("o1 id: " + p1.getId());

        Order o2 = (Order) o1.clone();

        System.out.println("o1 id: " + o1.getId());
        System.out.println("p1 id: " + o1.getProduct().getId());
        System.out.println("p1 name: " + o1.getProduct().getName());


        System.out.println("o2 id: " + o2.getId());
        System.out.println("p2 id: " + o2.getProduct().getId());
        System.out.println("p2 name: " + o2.getProduct().getName());




        o2.setId(2);
        o2.getProduct().setId(2);
        o2.getProduct().setName("p2");
        System.out.println("需改o2 后结果：" );

        System.out.println("o1 id: " + o1.getId());
        System.out.println("p1 id: " + o1.getProduct().getId());
        System.out.println("p1 name: " + o1.getProduct().getName());


        System.out.println("o2 id: " + o2.getId());
        System.out.println("p2 id: " + o2.getProduct().getId());
        System.out.println("p2 name: " + o2.getProduct().getName());


        int i =3^3;
        System.out.println(i);






    }



}
