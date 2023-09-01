package Database;

import entiteti.*;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

import static Datoteke.ReadingFiles.logger;

public class DatabaseInsert {
    public static Connection connectToDB() {
        Properties properties = new Properties();
        Connection connection;
        try {
            properties.load(new FileReader("dat/databaseData.properties"));
            String url = properties.getProperty("databaseUrl");
            String user = properties.getProperty("username");
            String password = properties.getProperty("password");

            connection = DriverManager.getConnection(url, user, password);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static Long getLastAdoptionStoreIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM ADOPTIONSTORE");

        Long storeID = 0L;

        while (rs.next()) {
            storeID = rs.getLong("MAX(ID)");
        }

        return storeID;

    }

    public static void createNewAdoptionStore(AdoptionStore a) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ADOPTIONSTORE(NAME, BOSS_ID, CITY_ID ) VALUES(?, ?, ?)");
            ps.setString(1, a.getName());
            ps.setLong(2, a.getBoss().getId());
            ps.setLong(3, a.getCity().getId());
            ps.executeUpdate();

            a.setId(getLastAdoptionStoreIndex());

            connection.close();
        } catch (SQLException | IOException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewAdoptionStore");
            throw new RuntimeException(e);
        }
    }

    public static Long getLastStoreIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM STORE");

        Long storeID = 0L;

        while (rs.next()) {
            storeID = rs.getLong("MAX(ID)");
        }

        return storeID;

    }

    public static Long getLastStorageIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM STORAGE");

        Long storageID = 0L;

        while (rs.next()) {
            storageID = rs.getLong("MAX(ID)");
        }

        return storageID;

    }

    public static void createNewStore(Store s) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO STORE(NAME, BOSS_ID, CITY_ID, ADDRESS_ID ) VALUES(?, ?, ?, ?)");
            ps.setString(1, s.getName());
            ps.setLong(2, s.getBoss().getId());
            ps.setLong(3, s.getCity().getId());
            ps.setLong(4, s.getAddress().getId());
            ps.executeUpdate();

            s.setId(getLastStoreIndex());

            for (int i = 0; i < s.getItems().size(); i++) {
                DatabaseInsert.insertNewItemIntoStore(s.getId(), s.getItems().get(i).getId());
            }
            for (int i = 0; i < s.getWorkers().size(); i++) {
                DatabaseInsert.insertNewWorkerIntoStore(s.getId(), s.getWorkers().get(i).getId());
            }

            connection.close();
        } catch (SQLException | IOException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewStore");
            throw new RuntimeException(e);
        }
    }

    public static void createNewStorage(Storage s) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO STORAGE(NAME,ADDRESS_ID ) VALUES(?, ?)");
            ps.setString(1, s.getName());
            ps.setLong(2, s.getAddress().getId());
            ps.executeUpdate();

            s.setId(getLastStorageIndex());

            for (int i = 0; i < s.getItems().size(); i++) {
                DatabaseInsert.insertNewItemIntoStorage(s.getId(), s.getItems().get(i).getId());
            }

            for (int i = 0; i < s.getWorkers().size(); i++) {
                DatabaseInsert.insertNewWorkerIntoStorage(s.getId(), s.getWorkers().get(i).getId());
            }


            connection.close();
        } catch (SQLException | IOException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewAdoptionStore");
            throw new RuntimeException(e);
        }
    }


    public static void addItemsOnShelfInStorage(Storage s, Item item, int position) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO SHELVES(SHELVES_ID, STORAGE_ID, ITEM_ID) VALUES(?, ?, ?)");
            ps.setLong(1, position);
            ps.setLong(2, s.getId());
            ps.setLong(3, item.getId());
            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod addItemsOnShelfInStorage");
            throw new RuntimeException(e);
        }
    }

    public static void insertNewItemIntoAdoptionStore(Long asId, Long itemId) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ADOPTIONSTORE_ITEMS(ADOPTIONSTORE_ID, ITEM_ID) VALUES(?, ?)");
            ps.setLong(1, asId);
            ps.setLong(2, itemId);
            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod insertNewItemIntoAdoptionStore");
            throw new RuntimeException(e);
        }
    }

    public static void insertNewItemIntoStorage(Long asId, Long itemId) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO STORAGE_ITEMS(STORAGE_ID, ITEM_ID) VALUES(?, ?)");
            ps.setLong(1, asId);
            ps.setLong(2, itemId);
            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod insertNewItemIntoStorage");
            throw new RuntimeException(e);
        }
    }

    public static void insertNewItemIntoStore(Long sId, Long itemId) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO STORE_ITEMS(STORE_ID, ITEM_ID) VALUES(?, ?)");
            ps.setLong(1, sId);
            ps.setLong(2, itemId);
            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod insertNewItemIntoStore");
            throw new RuntimeException(e);
        }
    }

    public static void insertNewWorkerIntoAdoptionStore(Long asId, Long workerId) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ADOPTIONSTORE_WORKERS(ADOPTIONSTORE_ID, WORKER_ID) VALUES(?, ?)");
            ps.setLong(1, asId);
            ps.setLong(2, workerId);
            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod insertNewWorkerIntoAdoptionStore");
            throw new RuntimeException(e);
        }
    }

    public static void insertNewWorkerIntoStore(Long asId, Long workerId) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO STORE_WORKERS(STORE_ID, WORKER_ID) VALUES(?, ?)");
            ps.setLong(1, asId);
            ps.setLong(2, workerId);
            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod insertNewWorkerIntoStore");
            throw new RuntimeException(e);
        }
    }

    public static void insertNewWorkerIntoStorage(Long asId, Long workerId) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO STORAGE_WORKERS(STORAGE_ID, WORKER_ID) VALUES(?, ?)");
            ps.setLong(1, asId);
            ps.setLong(2, workerId);
            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod insertNewWorkerIntoStorage");
            throw new RuntimeException(e);
        }
    }

    public static Long getLastAddressIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM ADDRESS");

        Long addressID = 0L;

        while (rs.next()) {
            addressID = rs.getLong("MAX(ID)");
        }

        return addressID;

    }

    public static void createNewAddress(Address a) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ADDRESS(NAME,STREET_NO,CITY_ID) VALUES( ?, ?, ?)");
            ps.setString(1, a.getName());
            ps.setInt(2, a.getStreetNo());
            ps.setLong(3, a.getCity().getId());
            ps.executeUpdate();

            a.setId(getLastAddressIndex());

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewAddress");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getLastBossIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM BOSS");

        Long bossID = 0L;

        while (rs.next()) {
            bossID = rs.getLong("MAX(ID)");
        }

        return bossID;

    }

    public static void createNewBoss(Boss b) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO BOSS(NAME , SURNAME, DATUM, GENDER) VALUES(?, ?, ?, ?)");

            ps.setString(1, b.getName());
            ps.setString(2, b.getSurname());
            ps.setDate(3, Date.valueOf(b.getDateOfBirth()));
            ps.setLong(4, b.getGenderEnum().ordinal() + 1);
            ps.executeUpdate();

            b.setId(getLastBossIndex());

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewBoss");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getLastCategoryIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM CATEGORY");

        Long categoryID = 0L;

        while (rs.next()) {
            categoryID = rs.getLong("MAX(ID)");
        }

        return categoryID;

    }

    public static void createNewCategory(Category c) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO CATEGORY(NAME, DESCRIPTION) VALUES(?, ?)");
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());

            ps.executeUpdate();

            c.setId(getLastCategoryIndex());

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewCategory");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getLastItemIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM ITEM");

        Long itemID = 0L;

        while (rs.next()) {
            itemID = rs.getLong("MAX(ID)");
        }

        return itemID;

    }

    public static void createNewItem(Item i) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ITEM(NAME, DESCRIPTION, PRICE, CATEGORY_ID) VALUES(?, ?, ?, ?)");
            ps.setString(1, i.getName());
            ps.setString(2, i.getDescription());
            ps.setBigDecimal(3, i.getPrice());
            ps.setLong(4, i.getCategory().getId());

            ps.executeUpdate();

            i.setId(getLastItemIndex());
            DatabaseInsert.insertNewItemIntoCategory(i.getId(), i.getCategory().getId());

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewItem");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertNewItemIntoCategory(Long itemId, Long categoryId) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO CATEGORY_ITEMS(CATEGORY_ID, ITEM_ID) VALUES(?, ?)");
            ps.setLong(1, categoryId);
            ps.setLong(2, itemId);
            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod insertNewItemIntoCategory");
            throw new RuntimeException(e);
        }
    }

    public static Long getLastCityIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM CITY");

        Long iD = 0L;

        while (rs.next()) {
            iD = rs.getLong("MAX(ID)");
        }

        return iD;

    }

    public static void createNewCity(City c) {
        try {
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO CITY(NAME, COUNTRY, POSTAL_CODE) VALUES(?, ?, ?)");
            ps.setString(1, c.getName());
            ps.setString(2, c.getCountry());
            ps.setInt(3, c.getPostalCode());

            ps.executeUpdate();

            c.setId(getLastCityIndex());

            connection.close();
        } catch (SQLException | IOException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewCity");
            throw new RuntimeException(e);
        }
    }


    public static void insertNewAddressIntoCity(Address a){
        try{
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO CITY_ADDRESSES(CITY_ID, ADDRESSES_ID) VALUES(?, ?)");
            ps.setLong(1, a.getCity().getId());
            ps.setLong(2, a.getId());
            ps.executeUpdate();

            connection.close();
        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazu kod insertNewAddress");
            throw new RuntimeException(e);
        }
    }

    public static Long getLastWorkerIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT ID FROM WORKER  ORDER BY ID DESC LIMIT 1;");

        Long iD = 0L;

        while (rs.next()) {
            iD = rs.getLong("ID");
        }

        return iD;

    }
    public static void createNewWorker(Worker w){
        try{
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO WORKER(NAME, SURNAME, DATUM_I_VRIJEME, GENDER_ID) VALUES(?, ?, ?, ?)");
            ps.setString(1, w.getName());
            ps.setString(2, w.getSurname());
            ps.setTimestamp(3, Timestamp.valueOf(w.getDateOfBirth().atStartOfDay()));
            ps.setLong(4, w.getGenderEnum().ordinal()+1);
            ps.executeUpdate();

            w.setId(getLastWorkerIndex());

            connection.close();
        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewWorker");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getLastLogInIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM LOGIN");

        Long iD = 0L;

        while (rs.next()) {
            iD = rs.getLong("MAX(ID)");
        }

        return iD;

    }

    public static void createNewLogIn(LogIn l){
        try{
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO LOGIN(USER_ID, DATUM_I_VRIJEME, DISCOUNT) VALUES(?, ?, ?)");
            ps.setLong(1, l.getUser().getId());
            ps.setTimestamp(2, Timestamp.valueOf(l.getTime()));
            ps.setInt(3, l.getDiscount().intValue());
            ps.executeUpdate();

            l.setId(getLastLogInIndex());
            connection.close();

        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewLogIn");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getLastUserIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM USERS");

        Long iD = 0L;

        while (rs.next()) {
            iD = rs.getLong("MAX(ID)");
        }

        return iD;

    }

    public static void createNewUser(User u){
        try{
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO USERS(NAME, PASSWORD) VALUES( ?, ?)");
            ps.setString(1, u.getName());
            ps.setString(2, u.getPass());
            ps.executeUpdate();

            u.setId(getLastUserIndex());

            connection.close();

        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewUser");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getLastTransactionIndex() throws SQLException, IOException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM TRANSACTIONS");

        Long iD = 0L;

        while (rs.next()) {
            iD = rs.getLong("MAX(ID)");
        }

        return iD;

    }

    public static void createNewTransaction(Transaction t){
        try{
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO TRANSACTIONS(USER_ID, PRICE) VALUES( ?, ?)");
            ps.setLong(1, t.getConsumer().getId());
            ps.setBigDecimal(2, t.getPrice());
            ps.executeUpdate();

            t.setId(getLastTransactionIndex());

            for (int i = 0; i < t.getItemList().size(); i++) {
                insertNewItemIntoTrans(t.getId(), t.getItemList().get(i).getId());
            }
            connection.close();

        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazu kod createNewTransaction");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertNewItemForUser(Long uId, Long iId){
        try{
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO USER_ITEMS(USER_ID, ITEM_ID ) VALUES( ?, ?)");
            ps.setLong(1, uId);
            ps.setLong(2, iId);
            ps.executeUpdate();


        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazu kod UserItems");
            throw new RuntimeException(e);
        }
    }

    public static void insertNewItemIntoTrans(Long tId, Long iId){
        try{
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO TRANSACTION_ITEMS(TRANSACTION_ID, ITEM_ID ) VALUES( ?, ?)");
            ps.setLong(1, tId);
            ps.setLong(2, iId);
            ps.executeUpdate();


        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazu kod Transaction_Items");
            throw new RuntimeException(e);
        }
    }

    public static void discountUsed(LogIn l){
        try{
            Connection connection = connectToDB();
            PreparedStatement ps = connection.prepareStatement("UPDATE LOGIN SET DISCOUNT = 0 WHERE ID = ?");
            ps.setLong(1, l.getId());
            ps.executeUpdate();

            connection.close();

        }catch (SQLException e){
            logger.info("Dogodila se pogreska pri spajanju na bazu kod discountUsed");
            throw new RuntimeException(e);
        }
    }
    public static LogIn getLastLogIn() {
        LogIn l = null;
        try {
            Connection connection = connectToDB();
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM LOGIN ORDER BY ID DESC LIMIT 1");
            while (rs.next()) {
                Long id = rs.getLong("ID");
                Long userId = rs.getLong("USER_ID");
                User user = DatabaseRead.getUserFromId_DB(userId);
                LocalDateTime time = rs.getTimestamp("DATUM_I_VRIJEME").toLocalDateTime();
                BigDecimal discount = rs.getBigDecimal("DISCOUNT");

                l = new LogIn(id, user, time, discount);
            }
            connection.close();

        } catch (SQLException e) {
            logger.info("Dogodila se pogreska pri spajanju na bazu kod discountUsed");
            throw new RuntimeException(e);
        }

        return l;
    }


}
