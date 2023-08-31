package jdbc;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    // метод добавления объекта
    public void addingProduct(int foodId, String foodName, String foodType, boolean foodExotic) {
        String SQL = "INSERT INTO FOOD VALUES (?, ?, ?, ?)";
        try (Connection conn = getH2DataSource().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
            preparedStatement.setInt(1, foodId);
            preparedStatement.setString(2, foodName);
            preparedStatement.setString(3, foodType);
            preparedStatement.setBoolean(4, foodExotic);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //метод проверки наличия объекта
    public Product checkProduct(int foodId) {
        Product product = new Product();
        String sql = "SELECT * FROM FOOD WHERE FOOD_ID = ?";
        try (Connection conn = getH2DataSource().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);) {
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
    public void deleteProduct(int foodId) {
        String SQL = "DELETE FROM FOOD WHERE FOOD_ID = ?;";
        try (Connection conn = getH2DataSource().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
            preparedStatement.setInt(1, foodId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
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
    private DataSource getH2DataSource() {
        Properties dataBaseProperties = new Properties();
        JdbcDataSource dataSource = new JdbcDataSource();

        try {
            InputStream resouerceAsStream = BaseTestJdbc.class.getClassLoader()
                    .getResourceAsStream("database.properties");
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
