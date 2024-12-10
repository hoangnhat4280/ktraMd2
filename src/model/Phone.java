package model;

public abstract class Phone {
    protected String id;
    protected String name;
    protected double price;
    protected int quantity;
    protected String manufacturer;

    public Phone(String id, String name, double price, int quantity, String manufacturer) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.manufacturer = manufacturer;
    }

    public abstract void displayInfo();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public abstract String toCSV();

    @Override
    public String toString() {
        return "ID: " + id +
                ", Tên: " + name +
                ", Giá: " + price +
                ", Số lượng: " + quantity +
                ", Nhà sản xuất: " + manufacturer;
    }
}
