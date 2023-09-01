package entiteti;

public class Dog{
    String name;

    public Dog(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Builder{
        private String name;

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Dog build(){
            Dog dog = new Dog(name);
            return dog;
        }

    }
}
