package entiteti;

public class DogToys{
    private String dogToys;

    public DogToys(String dogToys) {
        this.dogToys = dogToys;
    }

    public String getDogToys() {
        return dogToys;
    }

    public void setDogToys(String dogToys) {
        this.dogToys = dogToys;
    }

    @Override
    public String toString() {
        return dogToys ;
    }
}
