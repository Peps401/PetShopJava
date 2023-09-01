package entiteti;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChangesLog implements Serializable{

    private String oldData;
    private String newData;
    private String user;
    private String time;

    public ChangesLog(String oldData, String newData, String user, String time) {
        this.oldData = oldData;
        this.newData = newData;
        this.user = user;
        this.time = time;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static void write(String oldData, String newData,
                             String user, String time){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
        LocalDateTime timeInFormat = LocalDateTime.parse(time, dtf);


        ChangesLog newChangeLog = new ChangesLog(oldData, newData,
                user, timeInFormat.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")));

        List<ChangesLog> changesLogList;
        changesLogList = getChangesList();
        changesLogList.add(newChangeLog);

        savePromjena(changesLogList);

    }

    private static void savePromjena(List<ChangesLog> list){
        try(ObjectOutputStream objectWriter = new ObjectOutputStream(new FileOutputStream("dat/serializacija.dat"))) {

            objectWriter.writeObject(list);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<ChangesLog> getChangesList(){

        List<ChangesLog> list = new ArrayList<>();
        try (ObjectInputStream objetctReader = new ObjectInputStream(new FileInputStream("dat/serializacija.dat"))){

            list= (List<ChangesLog>) objetctReader.readObject();

        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return list;

    }

    @Override
    public String toString() {
        return "BiljeskePromjena{" +
                "staraVrijednost='" + oldData + '\'' +
                ", novaVrijednost='" + newData + '\'' +
                ", korisnik='" + user + '\'' +
                ", datumIVrijeme='" + time + '\'' +
                '}';
    }
}
