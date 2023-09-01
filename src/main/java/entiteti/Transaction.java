package entiteti;

import java.math.BigDecimal;
import java.util.List;

public class Transaction extends Id {
    private User consumer;
    private List<Item> itemList;
    private BigDecimal price;

    public Transaction(Long id, User consumer, List<Item> itemList, BigDecimal price) {
        super(id, "Racun");
        this.consumer = consumer;
        this.itemList = itemList;
        this.price = price;
    }

    public User getConsumer() {
        return consumer;
    }

    public void setConsumer(User consumer) {
        this.consumer = consumer;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return consumer.getName() +
                ", kupljeni proizvodi=" + itemList +
                ", iznos=" + price;
    }
}
