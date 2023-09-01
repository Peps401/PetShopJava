package entiteti;

public class City extends Id{

    private String country;
    private Integer postalCode;

    public City(Long id, String name, String country, Integer postalCode) {
        super(id, name);
        this.country = country;
        this.postalCode = postalCode;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return getName() + " " + country + " " + postalCode;
    }
}
