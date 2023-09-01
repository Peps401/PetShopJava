package entiteti;

import java.util.Objects;

public class Animal<T> {
    Long id;
    T animal;

    public Animal(Long id, T animal) {
        this.id = id;
        this.animal = animal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public T getAnimal() {
        return animal;
    }

    public void setAnimal(T animal) {
        this.animal = animal;
    }



    @Override
    public boolean equals(Object o) {
        if(o instanceof Animal<?>){
            Long id = ((Animal<Object>) o).getId();
            return id != null && id.equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
