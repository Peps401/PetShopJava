package entiteti;

import java.time.LocalDate;

public final class ProtectionTeam extends Worker implements Alarm{


    public ProtectionTeam(Long id, String name, String surname, LocalDate dateOfBirth, Enum<Gender> genderEnum) {
        super(id, name, surname, dateOfBirth, genderEnum);
    }

    @Override
    public void method(String nameOfAlarm) {
        System.out.println("You are being protected by " + nameOfAlarm);
    }
}
