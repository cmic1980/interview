package pro.caifu365.interview.clone;

public class Product implements Cloneable {
    private int id;
    private String name;

    public Product()
    {
        System.out.println("构造函数被调用");
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
