package entiteti;

public class AnimalFood {
   private String animalFood;

    public AnimalFood(String animalFood) {
        this.animalFood = animalFood;
    }

    public String getAnimalFood() {
        return animalFood;
    }

    public void setAnimalFood(String animalFood) {
        this.animalFood = animalFood;
    }

    @Override
    public String toString() {
        return animalFood ;
    }
}
