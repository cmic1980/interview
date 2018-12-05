package pro.caifu365.interview.clone;

public class Order implements Cloneable{
    private int id;
    private Product product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;

        product.setId(222);
    }

    public Object clone() {
        try {
            Object object = super.clone();



            return object;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}
