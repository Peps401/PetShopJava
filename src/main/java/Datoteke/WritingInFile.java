package Datoteke;

import entiteti.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalLong;

public class WritingInFile {
    public static void writeNewStore(Store store){
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/stores.txt"));
            lines.add(String.valueOf(store.getId()));
            lines.add(store.getName());
            lines.add(String.valueOf(store.getCity().getId()));
            lines.add(String.valueOf(store.getAddress().getId()));

            String workersId = "";
            for (Worker w:
                    store.getWorkers()) {
                workersId = workersId + w.getId() + " ";
            }

            lines.add(workersId);

            String itemsId = "";
            for (Item i:
                    store.getItems()) {
                itemsId = itemsId + i.getId() +" ";
            }

            lines.add(itemsId);

            lines.add(String.valueOf(store.getBoss().getId()));

            PrintWriter pw = new PrintWriter("dat/stores.txt");
            for (int i = 0; i < lines.size(); i++) {
                pw.write(lines.get(i));
                if(i < lines.size()-1){
                    pw.write("\n");
                }
            }
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewBoss(Boss boss){
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/bosses.txt"));
            lines.add(String.valueOf(boss.getId()));
            lines.add(boss.getName());
            lines.add(boss.getSurname());

            String date = "";
            if(boss.getDateOfBirth().getDayOfMonth() < 10){
                date = String.valueOf(0);
            }
            date = date + boss.getDateOfBirth().getDayOfMonth() + ".";

            if(boss.getDateOfBirth().getMonthValue() < 10) {
                date = date + String.valueOf(0) + boss.getDateOfBirth().getMonthValue() + "." + boss.getDateOfBirth().getYear() + ".";
            }
            else date = date + boss.getDateOfBirth().getMonthValue() + "." + boss.getDateOfBirth().getYear() + ".";
            lines.add(date);

            if(boss.getGenderEnum().name().equals("MALE")){
                lines.add("M");
            }
            else lines.add("F");
            PrintWriter pw = new PrintWriter("dat/bosses.txt");
            for (int i = 0; i < lines.size(); i++) {
                pw.write(lines.get(i));
                if(i < lines.size()-1){
                    pw.write("\n");
                }
            }
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeNewWorker(Worker worker){
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/workers.txt"));
            lines.add(String.valueOf(worker.getId()));
            lines.add(worker.getName());
            lines.add(worker.getSurname());

            String date = "";
            if(worker.getDateOfBirth().getDayOfMonth() < 10){
                date = String.valueOf(0);
            }
            date = date + worker.getDateOfBirth().getDayOfMonth() + ".";

            if(worker.getDateOfBirth().getMonthValue() < 10) {
                date = date + String.valueOf(0) + worker.getDateOfBirth().getMonthValue() + "." + worker.getDateOfBirth().getYear() + ".";
            }
            else date = date + worker.getDateOfBirth().getMonthValue() + "." + worker.getDateOfBirth().getYear() + ".";
            lines.add(date);

            if(worker.getGenderEnum().name().equals("MALE")){
                lines.add("M");
            }
            else lines.add("F");
            PrintWriter pw = new PrintWriter("dat/workers.txt");
            for (int i = 0; i < lines.size(); i++) {
                pw.write(lines.get(i));
                if(i < lines.size()-1){
                    pw.write("\n");
                }
            }
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewLogIn(Long userId, LocalDateTime time, int discount) {
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/trackLogins.txt"));
            lines.add(String.valueOf(userId));
            String stringDay = "";
            String stringMonth = "";
            String stringHour = "";
            String stringMin = "";
            if(time.getDayOfMonth() < 10){
                stringDay = "0" + time.getDayOfMonth();
            }
            else stringDay = String.valueOf(time.getDayOfMonth());

            if(time.getMonthValue() < 10){
                stringMonth = "0" + time.getMonthValue();
            }
            if(time.getHour() < 10){
                stringHour = "0" + time.getHour();
            }else stringHour = String.valueOf(time.getHour());

            if(time.getMinute() < 10){
                stringMin = "0" + time.getMinute();
            }else stringMin = String.valueOf(time.getMinute());

            String Stringtime = stringDay  + "." + stringMonth + "." + time.getYear() + "." + " " + stringHour + ":" + stringMin;
            lines.add(Stringtime);
            lines.add(String.valueOf(discount));
            PrintWriter pw = new PrintWriter("dat/trackLogins.txt");
            for (int i = 0; i < lines.size(); i++) {
                pw.write(lines.get(i));
                if(i < lines.size() - 1){
                    pw.write("\n");
                }
            }
            pw.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewUser(String chosenName, String chosenPass) {
        OptionalLong maxId = ReadingFiles.getAllUsers().stream().mapToLong(user -> user.getId()).max();
        User user = new User(maxId.getAsLong() + 1, chosenName, chosenPass);

        try {
            List<String> lines = Files.readAllLines(Path.of("dat/users.txt"));
            lines.add(String.valueOf(maxId.getAsLong() + 1));
            lines.add(chosenName);
            lines.add(chosenPass);

            PrintWriter printWriter = new PrintWriter("dat/users.txt");
            for (int i = 0; i < lines.size(); i++) {
                printWriter.write(lines.get(i));
                if(i < lines.size() - 1){
                    printWriter.write("\n");
                }
            }

            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewAddress(Address adress) {
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/addresses.txt"));
            lines.add(String.valueOf(adress.getId()));
            lines.add(adress.getName());
            lines.add(String.valueOf(adress.getStreetNo()));
            lines.add(String.valueOf(adress.getCity().getId()));

            PrintWriter printWriter = new PrintWriter("dat/addresses.txt");
            for (int i = 0; i < lines.size(); i++) {
                printWriter.write(lines.get(i));
                if(i < lines.size() - 1){
                    printWriter.write("\n");
                }
            }

            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewItem(Item item) {
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/items.txt"));
            lines.add(String.valueOf(item.getId()));
            lines.add(item.getName());
            lines.add(String.valueOf(item.getDescription()));
            lines.add(String.valueOf(item.getCategory().getId()));
            lines.add(String.valueOf(item.getPrice()));

            PrintWriter printWriter = new PrintWriter("dat/items.txt");
            for (int i = 0; i < lines.size(); i++) {
                printWriter.write(lines.get(i));
                if(i < lines.size() - 1){
                    printWriter.write("\n");
                }
            }

            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewCategory(Category category) {
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/categories.txt"));
            lines.add(String.valueOf(category.getId()));
            lines.add(category.getName());
            lines.add(String.valueOf(category.getDescription()));

            PrintWriter printWriter = new PrintWriter("dat/categories.txt");
            for (int i = 0; i < lines.size(); i++) {
                printWriter.write(lines.get(i));
                if(i < lines.size() - 1){
                    printWriter.write("\n");
                }
            }

            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewCity(City city) {
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/cities.txt"));
            lines.add(String.valueOf(city.getId()));
            lines.add(city.getName());
            lines.add(city.getCountry());
            lines.add(String.valueOf(city.getPostalCode()));

            PrintWriter printWriter = new PrintWriter("dat/cities.txt");
            for (int i = 0; i < lines.size(); i++) {
                printWriter.write(lines.get(i));
                if(i < lines.size() - 1){
                    printWriter.write("\n");
                }
            }

            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewStorage(Storage storage) {
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/storage.txt"));
            lines.add(String.valueOf(storage.getId()));
            lines.add(storage.getName());
            lines.add(String.valueOf(storage.getAddress().getId()));

            String workersId = "";
            for (Worker w:
                    storage.getWorkers()) {
                workersId = workersId + w.getId() + " ";
            }

            lines.add(workersId);

            String itemsId = "";
            for (Item i:
                    storage.getItems()) {
                itemsId = itemsId + i.getId() +" ";
            }

            lines.add(itemsId);


            PrintWriter pw = new PrintWriter("dat/storage.txt");
            for (int i = 0; i < lines.size(); i++) {
                pw.write(lines.get(i));
                if(i < lines.size()-1){
                    pw.write("\n");
                }
            }
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewTransaction(Transaction transaction) {
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/transactions.txt"));
            lines.add(String.valueOf(transaction.getConsumer().getId()));

            String itemsId = "";
            for (Item i:
                    transaction.getItemList()) {
                itemsId = itemsId + i.getId() +" ";
            }

            lines.add(itemsId);
            lines.add(String.valueOf(transaction.getPrice()));


            PrintWriter pw = new PrintWriter("dat/transactions.txt");
            for (int i = 0; i < lines.size(); i++) {
                pw.write(lines.get(i));
                if(i < lines.size()-1){
                    pw.write("\n");
                }
            }
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
