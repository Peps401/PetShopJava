package entiteti;

import java.time.LocalDate;

public class Boss extends Worker{

    public Boss(Long id, String name, String surname, LocalDate dateOfBirth, Enum<Gender> genderEnum) {
        super(id, name, surname, dateOfBirth, genderEnum);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Boss){
            Long id = ((Boss) o).getId();
            return id != null && id.equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

}
