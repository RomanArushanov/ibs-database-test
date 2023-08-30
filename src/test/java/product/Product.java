package product;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private String type;
    private boolean exotic;

    public Product() {
    }

    public Product(int id, String name, String type, boolean exotic) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.exotic = exotic;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isExotic() {
        return exotic;
    }

    public void setExotic(boolean exotic) {
        this.exotic = exotic;
    }

    @Override
    public String toString() {
        return "jdbc.test.Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", exotic=" + exotic +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && exotic == product.exotic && Objects.equals(name, product.name) && Objects.equals(type, product.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, exotic);
    }
}
