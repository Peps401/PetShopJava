package entiteti;

public class Address extends Id {
    private int StreetNo;
    private City city;

    public Address(Long id, String name, int streetNo, City city) {
        super(id, name);
        StreetNo = streetNo;
        this.city = city;
    }

    public int getStreetNo() {
        return StreetNo;
    }

    public void setStreetNo(int streetNo) {
        StreetNo = streetNo;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return getName() + " " + getStreetNo() + "." + " " + getCity();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Address){
            Long id = ((Address) o).getId();
            return id != null && id.equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
