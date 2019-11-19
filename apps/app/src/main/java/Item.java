public class Item {

    private String id;
    private String name;
    private int price;
    private int count;

    public Item(String id, String name, int price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public Item(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = 1;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }
}
