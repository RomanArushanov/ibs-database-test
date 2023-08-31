package jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import product.Product;

public class AddingProductTest extends BaseTestJdbc {

    @DisplayName("Тест добавления продукта")
    @Tag("DBT_1")
    @ParameterizedTest
    @MethodSource("testData")
    public void addingProductTest(int foodId, String foodName, String foodType, boolean foodExotic){
        Product newProduct = new Product(foodId, foodName, foodType, foodExotic);

        // добавляем продукт в таблицу
        addingProduct(foodId, foodName, foodType, foodExotic);

        // проверяем, что продукт появился в таблице
        Assertions.assertEquals(newProduct, checkProduct(foodId),
                "Запись с ID " + foodId + " отсутствует");

        // удаляем продукт из таблицы
        deleteProduct(foodId);

        // проверяем, что продукт удален из таблицы
        Assertions.assertNotEquals(newProduct, checkProduct(foodId),
                "Запись с ID " + foodId + " осталась в таблице");
    }
}
