package jdbctemplate;

import jdbc.BaseTestJdbc;
import product.Product;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseTestJdbcTemplate {
    static JdbcTemplate jdbcTemplate;

    // установка коннекта
    @BeforeAll
    static void init() {
        jdbcTemplate = new JdbcTemplate(getH2DataSource());
    }

    // создание данных для теста
    public static Object[][] testData() {
        return new Object[][]{
                {"100", "Огурец", "VEGETABLE", false},
                {"1000", "Apple", "FRUIT", true},
                {"10000", "Gurke", "VEGETABLE", true},
                {"99", "胡蘿蔔", "VEGETABLE", false},
                {"101", "موز", "FRUIT", true}
        };
    }

    //метод проверки наличия объекта
    public Product checkProduct(int foodId) {
        String sql = "SELECT * FROM FOOD WHERE FOOD_ID = ?";
        RowMapper<Product> rowMapper = (rs, rowNum) -> {
            Product product = new Product();
            product.setId(rs.getInt("FOOD_ID"));
            product.setName(rs.getString("FOOD_NAME"));
            product.setType(rs.getString("FOOD_TYPE"));
            product.setExotic(rs.getBoolean("FOOD_EXOTIC"));
            return product;
        };
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, foodId);
        } catch (Exception ex) {
            return new Product();
        }
    }

    // метод добавления объекта
    public void addProductById(int foodId, String foodName, String foodType, boolean foodExotic) {
        jdbcTemplate.update(
                "INSERT INTO FOOD VALUES (?, ?, ?, ?)",
                foodId, foodName, foodType, foodExotic);
    }

    // метод удаления объекта
    public void deleteProductById(int foodId) {
        jdbcTemplate.update(
                "DELETE FROM FOOD WHERE FOOD_ID = ?;",
                foodId);
    }

    // чтение проперти и возвращение соединения
    private static DataSource getH2DataSource() {
        Properties dataBaseProperties = new Properties();
        JdbcDataSource dataSource = new JdbcDataSource();

        try {
            InputStream resourceAsStream = BaseTestJdbc.class.getClassLoader()
                    .getResourceAsStream("database.properties");
            if (resourceAsStream != null) {
                dataBaseProperties.load(resourceAsStream);
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
