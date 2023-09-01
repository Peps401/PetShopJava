package entiteti;

public enum Gender {
    MALE("Male", 1),
    FEMALE("Female", 2),
    UNIDENTIFIED("Unidentified", 3);
    public final String gender;
    public final int no;

    Gender(String gender, int no) {
        this.gender = gender;
        this.no = no;
    }

    public String getGender() {
        return gender;
    }

    public int getNo() {
        return no;
    }
}
