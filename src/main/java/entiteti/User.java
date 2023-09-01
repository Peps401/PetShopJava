package entiteti;

import java.time.LocalDateTime;
import java.util.Objects;

public class User extends Id{
    private String pass;
    public User(Long id, String name, String pass) {
        super(id, name);
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public boolean equals(Object o) {

        if(o instanceof User){
            Long id = ((User) o).getId();
            return id != null && id.equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
