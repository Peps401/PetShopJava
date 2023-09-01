package entiteti;

import java.math.BigDecimal;
import java.util.List;

public class Store extends Id implements Discount{

    private  City city;
    public Address address;
    public List<Worker> workers;
    public List<Item> items;
    public Boss boss;

    public Store(Long id, String name, City city, Address address, List<Worker> workers, List<Item> items, Boss boss) {
        super(id, name);
        this.city = city;
        this.address = address;
        this.workers = workers;
        this.items = items;
        this.boss = boss;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setAddress(Address adress) {
        this.address = address;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public City getCity() {
        return city;
    }

    public Address getAddress() {
        return address;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public Boss getBoss() {
        return boss;
    }

    @Override
    public List<Item> calculateCheapestItem(List<Item> itemsList, BigDecimal discount) {
        return Discount.super.calculateCheapestItem(itemsList, discount);
    }
}
