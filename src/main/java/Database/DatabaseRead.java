package Database;
import Exceptions.ObjectWithThatIdNotFoundException;
import entiteti.*;

import java.math.BigDecimal;
import java.sql.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static Datoteke.ReadingFiles.logger;
//NEMA ANIMALFOOD TABLE
//NEMA CATALOGUE TABLE
//NEMA DOG NI DOGTOYS TABLE


public class DatabaseRead {
    public static Connection connectToDB(){
        Properties properties = new Properties();
        Connection connection;
        try {
            properties.load(new FileReader("dat/databaseData.properties"));
            String databaseUrl = properties.getProperty("databaseUrl");
            String user = properties.getProperty("username");
            String password = properties.getProperty("password");

            connection = DriverManager.getConnection(databaseUrl,user,password);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }


    public static List<AdoptionStore> getAllAdoptionStoresFromDB(){
        List<AdoptionStore> list = new ArrayList<>(0);

        try {
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM ADOPTIONSTORE");
            while (rs.next()){
                Long id = rs.getLong("ID");
                String name = rs.getString("NAME");

                Long bossId = rs.getLong("BOSS_ID");
                Boss boss = getBossFromId_DB(bossId);
                Long cityId = rs.getLong("CITY_ID");
                City city = getCityFromId_DB(cityId);

                List<Worker> workers = addWorkersToAdoptionStore(id);
                List<Animal> animals = addAnimalsToAdoptionStore(id);

                AdoptionStore a = new AdoptionStore(id, name, workers, animals, boss, city);
                list.add(a);

            }
            connection.close();
        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazi kod AdoptionStores");
        }
        return list;
    }

    public static List<Store> getAllStoresFromDB(){
        List<Store> list = new ArrayList<>(0);

        try {
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM STORE");

            while (rs.next()){
                Long id = rs.getLong("ID");
                String name = rs.getString("NAME");

                Long bossId = rs.getLong("BOSS_ID");
                Boss boss = getBossFromId_DB(bossId);
                Long cityId = rs.getLong("CITY_ID");
                City city = getCityFromId_DB(cityId);

                Long addressId = rs.getLong("ADDRESS_ID");
                Address address = getAddressFromId_DB(addressId);

                List<Worker> workers = addWorkersToStore(id);
                List<Item> items = addItemsToStore(id);

                Store s = new Store(id, name, city, address, workers, items, boss);
                list.add(s);

            }
            connection.close();
        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazi kod AdoptionStores");
        }
        return list;
    }

    public static List<Storage> getAllStoragesFromDB(){
        List<Storage> list = new ArrayList<>(0);

        try {
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM STORAGE");
            while (rs.next()){
                Long id = rs.getLong("ID");
                String name = rs.getString("NAME");

                List<Item> items = addItemsToStorage(id);
                List<Worker> workers = addWorkersToStorage(id);

                Address address = getAddressFromId_DB(rs.getLong("ADDRESS_ID"));

                List<Shelf> shelvesList = getItemsFromShelvesFromSpecificStorage(id);

                Storage a = new Storage(id, name, items, workers, address, shelvesList);
                list.add(a);

            }
            connection.close();
        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazi kod AdoptionStores");
        }
        return list;
    }

    public static List<Shelf> getItemsFromShelvesFromSpecificStorage(Long id){
        List<Shelf> list = new ArrayList<>(0);
        List<Item> i = new ArrayList<>(0);
        try {
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM SHELVES WHERE STORAGE_ID = " + id);
            while (rs.next()){
                Long shelves_id = rs.getLong("SHELVES_ID");
                Long storage_id = rs.getLong("STORAGE_ID");
                Long item_id = rs.getLong("ITEM_ID");

                Item item = DatabaseRead.getItemFromId_DB(item_id);
                i.add(item);

            }
            Shelf<Item> a = new Shelf<>(i);
            list.add(a);
            connection.close();
        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazi kod getItemsFromShelvesFromSpecificStorage");
        }
        return list;
    }

    public static List<Address> getAddressesFromDB(){
        List<Address> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM ADDRESS");
            while (resultSet.next()){
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                int streetNo = resultSet.getInt("STREET_NO");

                City city = getCityFromId_DB(resultSet.getLong("CITY_ID"));
                Address address = new Address(id, name, streetNo, city);
                list.add(address);
            }
            connection.close();
        }catch (SQLException e){throw new RuntimeException();
        }
        return list;
    }

    public static List<Address> getAddressesForSpecificCity(Long id){
        List<Address> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM CITY_ADDRESSES");
            while (resultSet.next()){
                Long idCity = resultSet.getLong("CITY_ID");
                if(idCity.equals(id)) {
                    Long idAddress = resultSet.getLong("ADDRESSES_ID");
                    Address address = getAddressFromId_DB(idAddress);
                    list.add(address);
                }
            }
            connection.close();
        }catch (SQLException e){throw new RuntimeException();
        }
        return list;
    }

    public static Address getAddressFromId_DB(Long id){
        Address address = new Address(0L, "PROVJERA", 0, new City(0L, "PROVJERA", "PROVJERA", 0));
        List<Address> list = getAddressesFromDB();
        for (Address a:
                list) {
            if(a.getId().equals(id)){
                address = a;
                break;
            }
        }

        if (address.getName().equals("PROVJERA")){
            logger.info("Nije pronaden grad s tim ID-em");
            throw new ObjectWithThatIdNotFoundException("Nije pronaden grad s ID-em; " + id);
        }
        return address;
    }
    public static City getCityFromId_DB(Long id){
        City city = new City(0L, "PROVJERA", "PROVJERA", 0);
        List<City> list = getCitiesFromDB();
        for (City c:
                list) {
            if(c.getId().equals(id)){
                city = c;
                break;
            }
        }

        if (city.getName().equals("PROVJERA")){
            logger.info("Nije pronaden grad s tim ID-em");
            throw new ObjectWithThatIdNotFoundException("Nije pronaden grad s ID-em; " + id);
        }
        return city;
    }

    public static List<City> getCitiesFromDB(){
        List<City> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM CITY");
            while (resultSet.next()){
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                String country = resultSet.getString("COUNTRY");
                int postalCode = resultSet.getInt("POSTAL_CODE");
                City city = new City(id, name, country, postalCode);
                list.add(city);
            }
            connection.close();
        }catch (SQLException e){throw new RuntimeException();
        }
        return list;
    }

    public static List<Animal> getAnimalsFromDB(){
        List<Animal> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM ANIMAL");
            while (resultSet.next()){
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                int type = resultSet.getInt("ANIMAL_TYPE");

                Animal a;
                if(type == 1){
                    a = new Animal<>(id, new Cat(name));
                }
                else a = new Animal<>(id, new Dog(name));
                list.add(a);
            }
            connection.close();
        }catch (SQLException e){throw new RuntimeException();
        }
        return list;
    }
    public static List<Boss> getBossesFromDB(){
        List<Boss> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM BOSS");
            while (resultSet.next()){
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                String surname = resultSet.getString("SURNAME");

                LocalDate date = resultSet.getDate("DATUM").toLocalDate();
                Long genderId = resultSet.getLong("GENDER");
                Gender gender = getGenderFromId_DB(genderId);
                Boss boss = new Boss(id, name, surname, date, gender);
                list.add(boss);
            }
            connection.close();
        }catch (SQLException e){throw new RuntimeException();
        }
        return list;
    }

    public static Boss getBossFromId_DB(Long id){
        Boss boss = new Boss(0L, "PROVJERA", "PROVJERA", LocalDate.now(), Gender.UNIDENTIFIED);
        List<Boss> list = getBossesFromDB();
        for (Boss b:
             list) {
            if(b.getId().equals(id)){
                boss = b;
                break;
            }
        }
        if (boss.getName().equals("PROVJERA")){
            logger.info("Nije pronaden sef s tim ID-em");
            throw new ObjectWithThatIdNotFoundException("Nije pronaden sef s ID-em; " + id);
        }
        return boss;
    }
    //PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ADOPTIONSTORE_ITEM WHERE ADOPTIONSTORE_ID = ?");

    public static List<Worker> addWorkersToAdoptionStore(Long id){
        List<Worker> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM ADOPTIONSTORE_WORKERS ");

            while (rs.next()){
                Long idAS = rs.getLong("ADOPTIONSTORE_ID");
                Long workerId = rs.getLong("WORKER_ID");
                if(id.equals(idAS)){
                    Worker worker = getWorkerFromId_DB(workerId);
                    list.add(worker);
                }
            }

            connection.close();

        }catch (SQLException e){
            logger.info("Pogreska kod spajanja na bazu ADOPTIONSTORE_WORKERS");
        }
        return list;
    }

    public static List<Worker> addWorkersToStore(Long id){
        List<Worker> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM STORE_WORKERS ");

            while (rs.next()){
                Long idAS = rs.getLong("STORE_ID");
                Long workerId = rs.getLong("WORKER_ID");
                if(id.equals(idAS)){
                    Worker worker = getWorkerFromId_DB(workerId);
                    list.add(worker);
                }
            }

            connection.close();

        }catch (SQLException e){
            logger.info("Pogreska kod spajanja na bazu ADOPTIONSTORE_WORKERS");
        }
        return list;
    }

    public static List<Item> getItemsForSpecificCategory(Long id){
        List<Item> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM CATEGORY_ITEMS ");

            while (rs.next()){
                Long idAS = rs.getLong("CATEGORY_ID");
                Long itemId = rs.getLong("ITEM_ID");
                if(id.equals(idAS)){
                    Item item = getItemFromId_DB(itemId);
                    list.add(item);
                }
            }

            connection.close();

        }catch (SQLException e){
            logger.info("Pogreska kod spajanja na bazu ADOPTIONSTORE_WORKERS");
        }
        return list;
    }

    public static List<Worker> addWorkersToStorage(Long id){
        List<Worker> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM STORAGE_WORKERS WHERE STORAGE_ID = " + id);

            while (rs.next()){
                Long workerId = rs.getLong("WORKER_ID");
                Worker worker = getWorkerFromId_DB(workerId);
                list.add(worker);
            }

            connection.close();

        }catch (SQLException e){
            logger.info("Pogreska kod spajanja na bazu ADOPTIONSTORE_WORKERS");
        }
        return list;
    }

    public static List<Animal> addAnimalsToAdoptionStore(Long id){
        List<Animal> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM ADOPTIONSTORE_ANIMALS ");

            while (rs.next()){
                Long idAS = rs.getLong("ADOPTIONSTORE_ID");
                Long animalId = rs.getLong("ANIMAL_ID");
                if(id.equals(idAS)){
                    Animal animal = getAnimalFromId_DB(animalId);
                    list.add(animal);
                }
            }

            connection.close();

        }catch (SQLException e){
            logger.info("Pogreska kod spajanja na bazu ADOPTIONSTORE_WORKERS");
        }
        return list;
    }

    public static Animal getAnimalFromId_DB(Long id) {
        List<Animal> list = getAnimalsFromDB();
        Animal animal = list.get(0);
        for (Animal a:
                list) {
            if (a.getAnimal().equals(id)) {
                animal = a;
                break;
            }
        }

        return animal;
    }
    public static List<Item> addItemsToStorage(Long id){
        List<Item> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM STORAGE_ITEMS ");

            while (rs.next()){
                Long idAS = rs.getLong("STORAGE_ID");
                Long itemId = rs.getLong("ITEM_ID");
                if(id.equals(idAS)){
                    Item item = getItemFromId_DB(itemId);
                    list.add(item);
                }
            }

            connection.close();

        }catch (SQLException e){
            logger.info("Pogreska kod spajanja na bazu ADOPTIONSTORE_WORKERS");
        }
        return list;
    }

    public static List<Item> addItemsToStore(Long id){
        List<Item> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM STORE_ITEMS ");

            while (rs.next()){
                Long idAS = rs.getLong("STORE_ID");
                Long itemId = rs.getLong("ITEM_ID");
                if(id.equals(idAS)){
                    Item item = getItemFromId_DB(itemId);
                    list.add(item);
                }
            }

            connection.close();

        }catch (SQLException e){
            logger.info("Pogreska kod spajanja na bazu ADOPTIONSTORE_WORKERS");
        }
        return list;
    }

    public static List<Worker> getAllWorkersFromDB(){
        List<Worker> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM WORKER");

            while (resultSet.next()){
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                String surname = resultSet.getString("SURNAME");
                LocalDate date = resultSet.getTimestamp("DATUM_I_VRIJEME").toLocalDateTime().toLocalDate();

                Long genderId = resultSet.getLong("GENDER_ID");
                Gender gender = getGenderFromId_DB(genderId);

                Worker worker = new Worker(id, name, surname, date, gender);
                list.add(worker);

            }
            connection.close();
        } catch (SQLException e) {
            logger.info("Pogreska kod spajanja na bazu WORKER");
            throw new RuntimeException(e);
        }

        return list;
    }
    public static Worker getWorkerFromId_DB(Long id) {
        List<Worker> list = getAllWorkersFromDB();
        Worker worker = new Worker(0L, "PROVJERA", "PROVJERA", LocalDate.now(), Gender.UNIDENTIFIED);
        for (Worker w:
             list) {
            if (w.getId().equals(id)) {
                worker = w;
                break;
            }
        }

        if (worker.getName().equals("PROVJERA")){
            logger.info("Nije pronaden radnik s tim ID-em");
            throw new ObjectWithThatIdNotFoundException("Nije pronaden radnik s ID-em; " + id);
        }
        return worker;
    }

    public static Item getItemFromId_DB(Long id) {
        List<Item> list = getAllItemsFromDB();
        Item item = list.get(0);
        for (Item i:
                list) {
            if (i.getId().equals(id)) {
                item = i;
                break;
            }
        }

        return item;
    }

    public static Gender[] getAllGendersFromDB(){
        Gender[] array = new Gender[3];
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM GENDER");
            while (resultSet.next()){
                String name = resultSet.getString("NAME");
                switch (name){
                    case "Male" -> array[0] = Gender.MALE;
                    case "Female" -> array[1] = Gender.FEMALE;
                    case "Undefines" -> array[2] = Gender.UNIDENTIFIED;
                }
            }
            connection.close();
        }catch (SQLException e){
            logger.info("Pogreska kod spajanja na bazu GENDER");
            throw new RuntimeException(e);
        }
        return array;
    }

    public static Gender getGenderFromId_DB(Long genderId){
        Gender[] genderArray = getAllGendersFromDB();
        Gender gender = genderArray[Math.toIntExact(genderId - 1)];
        return gender;
    }

    public static List<Category> getAllCategoriesFromDB(){
        List<Category> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CATEGORY");

            while (resultSet.next()){
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                String description = resultSet.getString("DESCRIPTION");

                Category category = new Category(id, name, description);
                list.add(category);

            }
            connection.close();
        } catch (SQLException e) {
            logger.info("Pogreska kod spajanja na bazu WORKER");
            throw new RuntimeException(e);
        }

        return list;
    }

    public static Category getCategoryFromId_DB(Long id) {
        List<Category> list = getAllCategoriesFromDB();
        Category category = new Category(0L, "PROVJERA", "PROVJERA");
        for (Category c:
                list) {
            if (c.getId().equals(id)) {
                category = c;
                break;
            }
        }

        if (category.getName().equals("PROVJERA")){
            logger.info("Nije pronaden radnik s tim ID-em");
            throw new ObjectWithThatIdNotFoundException("Nije pronaden radnik s ID-em; " + id);
        }
        return category;
    }

    public static List<Item> getAllItemsFromDB(){
        List<Item> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ITEM");

            while (resultSet.next()){
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                String description = resultSet.getString("DESCRIPTION");

                Long categoryId = resultSet.getLong("CATEGORY_ID");
                Category category = getCategoryFromId_DB(categoryId);

                BigDecimal price = resultSet.getBigDecimal("PRICE");

                Item item = new Item(id, name, description, category, price);
                list.add(item);

            }
            connection.close();
        } catch (SQLException e) {
            logger.info("Pogreska kod spajanja na bazu WORKER");
            throw new RuntimeException(e);
        }

        return list;
    }

    public static List<User> getAllUsersFromDB(){
        List<User> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");

            while (resultSet.next()){
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                String pass = resultSet.getString("PASSWORD");

                User user = new User(id, name, pass);
                list.add(user);

            }
            connection.close();
        } catch (SQLException e) {
            logger.info("Pogreska kod spajanja na bazu USERS");
            throw new RuntimeException(e);
        }

        return list;
    }

    public static User getUserFromId_DB(Long id){
        List<User> list = getAllUsersFromDB();
        User user = list.get(0);

        for (User u:
             list) {
            if(u.getId().equals(id)){
                user = u;
                break;
            }
        }

        return user;
    }

    public static List<Item> getBoughtItemsFromUser(User u){
        List<Item> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USER_ITEMS WHERE USER_ID");

            while (resultSet.next()){
                Long itemId = resultSet.getLong("ITEM_ID");
                list.add(getItemFromId_DB(itemId));


            }
            connection.close();
        } catch (SQLException e) {
            logger.info("Pogreska kod spajanja na bazu USERS");
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<LogIn> getAllLogInsFromDB(){
        List<LogIn> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM LOGIN");

            while (resultSet.next()){
                Long i = resultSet.getLong("ID");
                Long id = resultSet.getLong("USER_ID");
                User u = getUserFromId_DB(id);
                LocalDateTime time = resultSet.getTimestamp("DATUM_I_VRIJEME").toLocalDateTime();
                BigDecimal discount = resultSet.getBigDecimal("DISCOUNT");

                LogIn logIn = new LogIn(i, u, time, discount);
                list.add(logIn);

            }
            connection.close();
        } catch (SQLException e) {
            logger.info("Pogreska kod spajanja na bazu LOGIN");
            throw new RuntimeException(e);
        }

        return list;
    }

    public static List<Transaction> getAllTransactionsFromDB(){
        List<Transaction> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TRANSACTIONS");

            while (resultSet.next()){
                Long id = resultSet.getLong("ID");
                Long userId = resultSet.getLong("USER_ID");
                User user = getUserFromId_DB(userId);
                BigDecimal price = resultSet.getBigDecimal("PRICE");

                List<Item> boughtItems = getBoughtItemsFromUser(user);

                Transaction t = new Transaction(id, user, boughtItems, price);
                list.add(t);

            }
            connection.close();
        } catch (SQLException e) {
            logger.info("Pogreska kod spajanja na bazu USERS");
            throw new RuntimeException(e);
        }

        return list;
    }

    public static List<Transaction> getAllTransactionsForUserFromDB(Long userId){
        List<Transaction> list = new ArrayList<>(0);
        try{
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TRANSACTIONS WHERE USER_ID = " + userId);

            while (resultSet.next()){
                Long id = resultSet.getLong("ID");
                Long uId = resultSet.getLong("USER_ID");
                BigDecimal price = resultSet.getBigDecimal("PRICE");

                User u = getUserFromId_DB(userId);
                List<Item> boughtItems = getBoughtItemsFromUser(u);

                Transaction t = new Transaction(id, u, boughtItems, price);
                list.add(t);

            }
            connection.close();
        } catch (SQLException e) {
            logger.info("Pogreska kod spajanja na bazu USERS");
            throw new RuntimeException(e);
        }

        return list;
    }

}
