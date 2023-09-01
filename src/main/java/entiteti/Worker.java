package entiteti;

import java.time.LocalDate;

public class Worker extends Id{
    public String surname;
    public LocalDate dateOfBirth;
    public Enum<Gender> genderEnum;

    public Worker(Long id, String name, String surname, LocalDate dateOfBirth, Enum<Gender> genderEnum) {
        super(id, name);
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.genderEnum = genderEnum;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Enum<Gender> getGenderEnum() {
        return genderEnum;
    }

    public void setGenderEnum(Enum<Gender> genderEnum) {
        this.genderEnum = genderEnum;
    }

    @Override
    public String toString() {
        return  getName() + " " + surname ;
    }

    @Override
    public boolean equals(Object o) {

        if(o instanceof Worker){
            Long id = ((Worker) o).getId();
            return id != null && id.equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
