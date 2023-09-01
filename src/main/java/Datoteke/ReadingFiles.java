package Datoteke;

import entiteti.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadingFiles {

    public static User LastUserToLogIn;
    public static LocalDateTime LastLogInTime;
    public static final Logger logger = LoggerFactory.getLogger(ReadingFiles.class);
    public static List<AdoptionStore> getAllAdoptionStores(){
        List<AdoptionStore> list = new ArrayList<>(0);
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/adoptionStore.txt"));
            for (int i = 0; i < lines.size(); i = i + 5) {
                Long id = Long.valueOf(lines.get(i));

                String name = lines.get(i + 1);

                String stringWorkersId = lines.get(i + 2);

                stringWorkersId = stringWorkersId.replaceAll("\\s", "");
                List<Worker> workers = getWorkersFromId(stringWorkersId);

                Long bossId = Long.valueOf(lines.get(i + 3));
                Boss boss = getBossfromId(bossId);

                Long cityId = Long.valueOf(lines.get(i + 4));
                City city = getCityFromId(cityId);

                List<Animal> animals = new ArrayList<>(0);
                Animal<Dog> dog = new Animal<>(1L, new Dog("doberman"));
                Animal<Cat> cat = new Animal<>(2L, new Cat("perzijska macka"));
                AdoptionStore adoptionStore = new AdoptionStore(id, name, workers, animals, boss, city);
                list.add(adoptionStore);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static Boss getBossfromId(Long id){
        List<Boss> bosses = getAllBosses();
        Boss boss = bosses.get(0);
        for (Boss b:
             bosses) {
            if(b.getId().equals(id)){
                boss = b;
                break;
            }
        }
        return boss;
    }

    public static List<Boss> getAllBosses(){
        List<Boss> list = new ArrayList<>(0);
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/bosses.txt"));
            for (int i = 0; i < lines.size(); i = i+5) {
                Long id = Long.valueOf(lines.get(i));

                String name = lines.get(i + 1);
                String surname = lines.get(i + 2);

                String dateString = lines.get(i + 3);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                LocalDate date = LocalDate.parse(dateString, dtf);

                Gender gender = getGender(lines.get(i + 4));

                Boss boss = new Boss(id, name, surname, date, gender);
                list.add(boss);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
    public static Store getStoreFromId(String idSTring){
        Long id = Long.valueOf(idSTring);
        List<Store> stores = getAllStores();
        Store s = stores.get(0);
        for (Store store:
             stores) {
            if(store.getId().equals(id)){
                s = store;
                break;
            }
        }
        return s;
    }
    public static List<Store> getAllStores(){
        List<Store> list = new ArrayList<>(0);
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/stores.txt"));
            for (int i = 0; i < lines.size(); i = i + 7) {
                Long id = Long.valueOf(lines.get(i));

                String name = lines.get(i + 1);
                Long cityId = Long.valueOf(lines.get(i + 2));
                City city = getCityFromId(cityId);

                Long addressId = Long.valueOf(lines.get(i + 3));
                Address address = getAddressFromId(addressId);

                String workersId = lines.get(i + 4);
                workersId = workersId.replaceAll("\\s", "");
                List<Worker> workers = getWorkersFromId(workersId);

                String itemsId = lines.get(i + 5);
                itemsId = itemsId.replaceAll("\\s", "");
                List<Item> items = getItemsFromId(itemsId);

                Long bossid = Long.valueOf(lines.get(i + 6));
                Boss boss = getBossfromId(bossid);

                Store store = new Store(id, name, city, address, workers, items, boss);
                list.add(store);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public static List<Item> getItemsFromId(String id){
        List<Item> items = getAllItems();
        List<Item> list = new ArrayList<>(0);

        for (int j = 0; j < id.length(); j++) {
            int a = id.length();
            Long idEqul = (long) Integer.parseInt(String.valueOf(id.charAt(j)));
            for (int k = 0; k < items.size(); k++) {
                Long itemId = items.get(k).getId();
                if(itemId.equals(idEqul)){
                    list.add(items.get(k));
                    break;
                }
            }
        }
        return list;

    }
    public static Address getAddressFromId(Long id){
        List<Address> adressList = getAllAddresses();
        Address address = adressList.get(0);
        for (Address a:
                adressList) {
            if(a.getId().equals(id)){
                address = a;
                break;
            }
        }

        return address;
    }

    public static List<Address> getAllAddresses(){
        List<Address> list = new ArrayList<>(0);
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/addresses.txt"));
            for (int i = 0; i < lines.size(); i = i + 4) {
                Long id = Long.valueOf(lines.get(i));
                String name = lines.get(i + 1);
                Integer streetNo = Integer.valueOf(lines.get( i + 2));
                City city = getCityFromId(Long.valueOf(lines.get( i + 3)));

                Address address = new Address(id, name,streetNo, city);
                list.add(address);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<Worker> getWorkersFromId(String stringWorkersId){
        List<Worker> list= new ArrayList<>(0);
        List<Worker> workerList = getAllWorkers();

        for (int j = 0; j < stringWorkersId.length(); j++) {
            for (int k = 0; k < workerList.size(); k++) {
                String workerId = String.valueOf(workerList.get(k).getId());
                String idEqul = String.valueOf(stringWorkersId.charAt(j));
                if(workerId.equals(idEqul)){
                    list.add(workerList.get(j));
                }
            }
        }
        return list;
    }

    public static City getCityFromId(Long id){
        List<City> cities = getAllCities();
        City city = cities.get(0);
        for (City c:
             cities) {
            if(c.getId().equals(id)){
                city = c;
                break;
            }
        }

        return city;
    }

    public static List<City> getAllCities(){
        List<City> list = new ArrayList<>(0);

        try {
            List<String> lines = Files.readAllLines(Path.of("dat/cities.txt"));
            for (int i = 0; i < lines.size(); i = i+4) {
                Long id = Long.valueOf(lines.get(i));
                String name = lines.get(i + 1);
                String country = lines.get(i + 2);
                Integer postalCode = Integer.valueOf(lines.get( i + 3));
                City city = new City(id, name, country, postalCode);
                list.add(city);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
    public static List<Worker> getAllWorkers(){
        List<Worker> list = new ArrayList<>(0);
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/workers.txt"));
            for (int i = 0; i < lines.size(); i = i + 5) {
                Long id = Long.valueOf(lines.get(i));
                String name = lines.get(i + 1);
                String surname = lines.get(i + 2);

                String dateString = lines.get(i + 3);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                LocalDate date = LocalDate.parse(dateString, dtf);
                
                Gender genderEnum = getGender(lines.get( i + 4));
                
                Worker w =  new Worker(id, name, surname, date, genderEnum);
                list.add(w);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
    
    public static Gender getGender(String g){
        Gender gender;
        if(g.equals("M")){
            gender = Gender.MALE;
        }
        else if(g.equals("F")){
            gender = Gender.FEMALE;
        }
        else gender = Gender.UNIDENTIFIED;
        return gender;
    }


    public static List<Category> getAllCategories(){
        List<Category> list = new ArrayList<>(0);
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/categories.txt"));
            for (int i = 0; i < lines.size(); i = i+3) {
                Long id = Long.valueOf(lines.get(i));
                String name = lines.get(i + 1);
                String description = lines.get(i + 2);
                Category c = new Category(id, name, description);
                list.add(c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public static Category getCategoryFromId(Long id){
        List<Category> categories = getAllCategories();
        Category category = categories.get(0);
        for (Category c:
                categories) {
            if(c.getId().equals(id)){
                category = c;
                break;
            }
        }

        return category;
    }

    public static List<Item> getAllItems(){
        List<Item> list = new ArrayList<>(0);
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/items.txt"));
            for (int i = 0; i < lines.size(); i = i + 5) {
                Long id = Long.valueOf(lines.get(i));
                String name = lines.get(i + 1);
                String description = lines.get(i + 2);
                Long categoryId = Long.valueOf(lines.get(i + 3));
                Category category = getCategoryFromId(categoryId);
                String price = lines.get(i + 4);
                BigDecimal p = new BigDecimal(price);

                Item item = new Item(id, name, description, category, p);
                list.add(item);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public static List<User> getAllUsers(){
        List<User> list = new ArrayList<>(0);
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/users.txt"));
            for (int i = 0; i < lines.size(); i = i+3) {
                Long id = Long.valueOf(lines.get(i));
                String name = lines.get(i + 1);
                String p = lines.get(i + 2);
                User u = new User(id, name, p);
                list.add(u);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public static User getUserFromId(Long id){
        List<User> users = getAllUsers();
        User u = users.get(0);
        for (User user:
                users) {
            if(user.getId().equals(id)){
                u = user;
                break;
            }
        }

        return u;
    }

    public static Map<User, List> getUsersLogins(){
        Map<User, List> map = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/trackLogins.txt"));
            for (int i = 0; i < lines.size(); i = i + 3) {
                Long id = Long.valueOf(lines.get(i));

                String dateString = lines.get(i + 1);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
                LocalDateTime time= LocalDateTime.parse(dateString, dtf);

                Integer discount = Integer.valueOf(lines.get(i + 2));
                User user = getUserFromId(id);

                List list = new ArrayList<>(0);
                list.add(time);
                list.add(discount);

                map.put(user, list);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public static User lastLogInUser(){
        User user = new User(0L, "NE POSTOJI", "NE POSTOJI");
        List list = new ArrayList<>(0);
        try {
            List<String> lines = lines = Files.readAllLines(Path.of("dat/trackLogins.txt"));

            for (int i = lines.size() - 3 ; i < lines.size(); i = i + 3) {
                Long id = Long.valueOf(lines.get(i));

                String dateString = lines.get(i + 1);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
                LocalDateTime time= LocalDateTime.parse(dateString, dtf);

                Integer discount = Integer.valueOf(lines.get(i + 2));
                user = getUserFromId(id);

                LastUserToLogIn = user;
                LastLogInTime = time= LocalDateTime.parse(dateString, dtf);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public static LogIn lastLogInData(){
        User user = new User(0L, "NE POSTOJI", "NE POSTOJI");
        LogIn logIn = new LogIn(0L, user, LocalDateTime.now(), BigDecimal.valueOf(0));
        try {
            List<String> lines = lines = Files.readAllLines(Path.of("dat/trackLogins.txt"));

            for (int i = lines.size() - 3 ; i < lines.size(); i = i + 3) {
                Long id = Long.valueOf(lines.get(i));

                String dateString = lines.get(i + 1);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
                LocalDateTime time= LocalDateTime.parse(dateString, dtf);

                Integer discount = Integer.valueOf(lines.get(i + 2));
                user = getUserFromId(id);

                logIn = new LogIn(0L, user, time,  BigDecimal.valueOf(0));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return logIn;
    }

    public static List<Catalogue> s(){
        List<Item> items = getAllItems();
        List<Catalogue> c = new ArrayList<>(0);

        for (Item item : items) {
            if (item.getPrice().compareTo(BigDecimal.valueOf(0)) > 0) {
                Catalogue<Item, BigDecimal> catalogue = new Catalogue<Item, BigDecimal>(item, item.getPrice());
                c.add(catalogue);
            } else {
                Catalogue<Item, String> catalogue = new Catalogue<Item, String>(item, "Cijena jos nije potavljena");
                c.add(catalogue);
            }
        }

        return c;
    }

    public static List<Storage> getAllStorages(){
        List<Storage> list = new ArrayList<>(0);
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/storage.txt"));
            for (int i = 0; i < lines.size(); i = i+5) {
                Long id = Long.valueOf(lines.get(i));
                String name = lines.get(i + 1);

                String itemsId = lines.get(i + 2);
                itemsId = itemsId.replaceAll("\\s", "");
                List<Item> items = getItemsFromId(itemsId);

                String workersId =lines.get(i + 3);
                List<Worker> workers = getWorkersFromId(workersId);

                String addressId = lines.get(i + 4);
                addressId = addressId.replaceAll("\\s", "");
                Address address = getAddressFromId(Long.valueOf(addressId));

                AnimalFood food = new AnimalFood("Hrana za pse");
                AnimalFood food2 = new AnimalFood("Hrana za pse2");
                List<AnimalFood> listOfFood = new ArrayList<>(2);
                listOfFood.add(food);
                listOfFood.add(food2);
                DogToys toys = new DogToys("Igracke za maltezere");
                DogToys toys2 = new DogToys("Igracke za dobermane");
                List<DogToys> dogToysList = new ArrayList<>(2);
                dogToysList.add(toys);
                dogToysList.add(toys2);

                Shelf<AnimalFood> dogFoodShelf = new Shelf<>(listOfFood);
                Shelf<DogToys> dogToyShelf = new Shelf<>(dogToysList);

                List<Shelf> shelves = new ArrayList<>(2);
                shelves.add(dogFoodShelf);
                shelves.add(dogToyShelf);

                Storage storage = new Storage(id, name, items, workers, address, shelves);
                list.add(storage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public static List<Transaction> getAllTransactions(){
        List<Transaction> list = new ArrayList<>(0);
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/transactions.txt"));
            for (int i = 0; i < lines.size(); i = i + 4) {
                Long transId = Long.valueOf(lines.get(i));
                Long consumerId = Long.valueOf(lines.get(i + 1));
                User consumer = getUserFromId(consumerId);
                String itemsId = lines.get(i + 2);
                itemsId = itemsId.replaceAll("\\s", "");
                List<Item> itemsList = getItemsFromId(itemsId);

                String stringPrice = lines.get(i + 3);
                BigDecimal price = new BigDecimal(stringPrice);

                Transaction transaction = new Transaction(transId, consumer, itemsList, price);
                list.add(transaction);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

}
