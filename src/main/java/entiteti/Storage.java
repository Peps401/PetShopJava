package entiteti;

import java.util.List;

public class Storage extends Id{
    private List<Item> items;
    private List<Worker> workers;
    private Address address;

    private List<Shelf> boxesOf;

    public Storage(Long id, String name, List<Item> items, List<Worker> workers, Address address, List<Shelf> boxesOf) {
        super(id, name);
        this.items = items;
        this.workers = workers;
        this.address = address;
        this.boxesOf = boxesOf;
    }

    public List<Shelf> getBoxesOf() {
        return boxesOf;
    }

    public void setBoxesOf(List<Shelf> boxesOf) {
        this.boxesOf = boxesOf;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address adress) {
        this.address = address;
    }

    @Override
    public String toString() {
        return getName();
    }
}
