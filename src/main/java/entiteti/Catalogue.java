package entiteti;

public class Catalogue<T, S>{

    private T category;
    private S objects;

    public Catalogue(T category, S objects) {
        this.category = category;
        this.objects = objects;
    }

    public T getCategory() {
        return category;
    }

    public void setCategory(T category) {
        this.category = category;
    }

    public S getObjects() {
        return objects;
    }

    public void setObjects(S objects) {
        this.objects = objects;
    }
}
