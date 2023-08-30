package jdbc;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import product.Product;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BaseTestJdbc {
    static Connection connection;

    // открытие коннекта
    @BeforeAll
    static void init() throws SQLException {
        connection = getH2DataSource().getConnection();
    }

    // закрытие коннекта
    @AfterAll
    static void close() throws SQLException {
        connection.close();
    }

    // метод добавления объекта
    @Test
    public boolean addingProduct(int foodId, String foodName, String foodType, boolean foodExotic) {
        String SQL = "INSERT INTO FOOD VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL);) {
            preparedStatement.setInt(1, foodId);
            preparedStatement.setString(2, foodName);
            preparedStatement.setString(3, foodType);
            preparedStatement.setBoolean(4, foodExotic);
            if (preparedStatement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //метод проверки наличия объекта
    @Test
    public Product checkProduct(int foodId) {
        Product product = new Product();
        String sql = "SELECT * FROM FOOD WHERE FOOD_ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setInt(1, foodId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                product.setId(rs.getInt("FOOD_ID"));
                product.setName(rs.getString("FOOD_NAME"));
                product.setType(rs.getString("FOOD_TYPE"));
                product.setExotic(rs.getBoolean("FOOD_EXOTIC"));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        if (product.getId() == foodId) {
            return product;
        } else {
            return new Product();
        }
    }

    // метод удаления объекта
    public boolean deleteProduct(int foodId) {
        String SQL = "DELETE FROM FOOD WHERE FOOD_ID = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL);) {
            preparedStatement.setInt(1, foodId);
            if (preparedStatement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    // данные для теста
    public static Object[][] testData() {
        return new Object[][]{
                {"100", "Огурец", "VEGETABLE", false},
                {"1000", "Apple", "FRUIT", true},
                {"10000", "Gurke", "VEGETABLE", true},
                {"99", "胡蘿蔔", "VEGETABLE", false},
                {"101", "موز", "FRUIT", true}
        };
    }


    // чтение проперти и возвращение соединения
    private static DataSource getH2DataSource() {
        Properties dataBaseProperties = new Properties();
        JdbcDataSource dataSource = new JdbcDataSource();

        try {
            InputStream resouerceAsStream = BaseTestJdbc.class.getClassLoader().getResourceAsStream("database.properties");
            if (resouerceAsStream != null) {
                dataBaseProperties.load(resouerceAsStream);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Не удалось загрузить файл database.properties", e);
        }

        dataSource.setURL(dataBaseProperties.getProperty("db.url"));
        dataSource.setUser(dataBaseProperties.getProperty("db.user"));
        dataSource.setPassword(dataBaseProperties.getProperty("db.password"));

        return dataSource;
    }
}
