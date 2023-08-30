package jdbctemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import product.Product;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class AddingProductTest extends BaseTestJdbcTemplate {

    @DisplayName("Тест добавления продукта")
    @Tag("DBT_1")
    @ParameterizedTest
    @MethodSource("testData")
    public void addingProductTest(int foodId, String foodName, String foodType, boolean foodExotic) {
        Product newProduct = new Product(foodId, foodName, foodType, foodExotic);

        // проверяем, что записи нет в таблице
        Assertions.assertNotEquals(newProduct, checkProduct(foodId),
                "Запись с ID " + foodId + " уже есть в таблице");

        // добавляем запись в таблицу
        updateProductById(foodId, foodName, foodType, foodExotic);

        // проверяем, что продукт появился в таблице
        Assertions.assertEquals(newProduct, checkProduct(foodId),
                "Запись с ID " + foodId + " отсутствует");

        // удаляем запись из таблицы
        deleteProductById(foodId);

        // проверяем, что записи нет в таблице
        Assertions.assertNotEquals(newProduct, checkProduct(foodId),
                "Запись с ID " + foodId + " уже есть в таблице");
    }
}
