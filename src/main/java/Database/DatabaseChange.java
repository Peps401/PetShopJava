package Database;

import entiteti.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseChange {

    public static void deleteBossFromDB(Boss boss) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM BOSS WHERE ID = ?");
            ps.setLong(1, boss.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteWorkerFromDB(Worker worker) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM WORKER WHERE ID = ?");
            ps.setLong(1, worker.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateBossFromDB(Boss boss) {

        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE BOSS SET NAME = ?, SURNAME = ?, DATUM = ?, GENDER = ? WHERE ID = ?");
            ps.setString(1, boss.getName());
            ps.setString(2, boss.getSurname());
            ps.setDate(3, Date.valueOf(boss.getDateOfBirth()));
            ps.setLong(4, boss.getGenderEnum().ordinal()+1);
            ps.setLong(5, boss.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateWorkerFromDB(Worker w) {

        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE WORKER SET NAME = ?, SURNAME = ?, DATUM_I_VRIJEME = ?, GENDER_ID = ? WHERE ID = ?");
            ps.setString(1, w.getName());
            ps.setString(2, w.getSurname());
            ps.setDate(3, Date.valueOf(w.getDateOfBirth()));
            ps.setLong(4, w.getGenderEnum().ordinal()+1);
            ps.setLong(5, w.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateItemFromDB(Item i) {

        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement  ps = connection.prepareStatement("UPDATE ITEM SET NAME = ?,  DESCRIPTION = ?, PRICE = ?, CATEGORY_ID = ? WHERE ID = ?");
            ps.setString(1, i.getName());
            ps.setString(2, i.getDescription());
            ps.setBigDecimal(3, i.getPrice());
            ps.setLong(4, i.getCategory().getId());
            ps.setLong(5, i.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateCategoryFromDB(Category c) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE CATEGORY SET NAME = ?,  DESCRIPTION = ? WHERE ID = ?");
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setLong(3, c.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteCategoryFromDB(Category c) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM CATEGORY WHERE ID = ?");
            ps.setLong(1, c.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void deleteItemFromDB(Item i) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM ITEM WHERE ID = ?");
            ps.setLong(1, i.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public static void deleteItemFromTransactions(Item i) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM TRANSACTION_ITEMS WHERE ITEM_ID = ?");
            ps.setLong(1, i.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void deleteItemFromUsersTransaction(Item i) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM USER_ITEMS WHERE ITEM_ID = ?");
            ps.setLong(1, i.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void deleteItemFromStore(Item i) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM STORE_ITEMS WHERE ITEM_ID = ?");
            ps.setLong(1, i.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void deleteItemFromStorage(Item i) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM STORAGE_ITEMS WHERE ITEM_ID = ?");
            ps.setLong(1, i.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void deleteStorageFromDB(Storage s) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM STORAGE WHERE ID = ?");
            ps.setLong(1, s.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void deleteWorkersFromStore(Store s) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM STORE_WORKERS WHERE STORE_ID = ?");
            ps.setLong(1, s.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void deleteStoreFromDB(Store s) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM STORE WHERE ID = ?");
            ps.setLong(1, s.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void deleteItemsFromStoreItem(Long id){
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM STORE_ITEMS WHERE ITEM_ID = ?");
            ps.setLong(1, id);

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteWorkersFromStorage(Storage s){
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM STORAGE_WORKERS WHERE STORAGE_ID = ?");
            ps.setLong(1, s.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteItemsFromStorageItem(Storage s){
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM STORAGE_ITEMS WHERE STORAGE_ID = ?");
            ps.setLong(1, s.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteShelvesFromStorage(Storage s){
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM SHELVES WHERE STORAGE_ID = ?");
            ps.setLong(1, s.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateStorageFromDB(Storage s) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE STORAGE SET NAME = ?,  ADDRESS_ID = ? WHERE ID = ?");
            ps.setString(1, s.getName());
            ps.setLong(2, s.getAddress().getId());
            ps.setLong(3, s.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateStorageItemsFromDB(Long s, Long i) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE STORAGE_ITEMS SET ITEM_ID = ? WHERE STORAGE_ID = ?");
            ps.setLong(1, s);
            ps.setLong(2, i);

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateStorageWorkersFromDB(Long s, Long w) {
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE STORAGE_WORKERS SET WORKER_ID = ? WHERE STORAGE_ID = ?");
            ps.setLong(1, s);
            ps.setLong(2, w);

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateStoreFromDB(Store s){
        Connection connection = DatabaseRead.connectToDB();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE STORE SET NAME = ?, BOSS_ID = ?, CITY_ID = ?, ADDRESS_ID = ? WHERE ID = ?");
            ps.setString(1, s.getName());
            ps.setLong(2, s.getBoss().getId());
            ps.setLong(3, s.getCity().getId());
            ps.setLong(4, s.getAddress().getId());
            ps.setLong(5, s.getId());

            ps.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
