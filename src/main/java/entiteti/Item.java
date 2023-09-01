package entiteti;

import java.math.BigDecimal;
import java.util.Objects;

public class Item extends Id implements Discount{
    public String description;
    public Category category;
    public BigDecimal price;

    public Item(Long id, String name, String description, Category category, BigDecimal price) {
        super(id, name);
        this.description = description;
        this.category = category;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Item){
            Long id = ((Item) o).getId();
            return id != null && id.equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
       return this.getId().hashCode();
    }
}
