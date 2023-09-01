package entiteti;

import java.util.List;

public class AdoptionStore extends Id{

    public List<Worker> workers;
    public List<Animal> animalList;
    public Boss boss;
    public City city;

    public AdoptionStore(Long id, String name, List<Worker> workers, List<Animal> animalList, Boss boss, City city) {
        super(id, name);
        this.workers = workers;
        this.animalList = animalList;
        this.boss = boss;
        this.city = city;
    }

    public List<Animal> getAnimalList() {
        return animalList;
    }

    public void setAnimalList(List<Animal> animalList) {
        this.animalList = animalList;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }


    public void setCity(City city) {
        this.city = city;
    }

    public Boss getBoss() {
        return boss;
    }


    public City getCity() {
        return city;
    }
}
