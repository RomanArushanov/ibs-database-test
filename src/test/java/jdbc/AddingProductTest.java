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
    public void test(int foodId, String foodName, String foodType, boolean foodExotic){
        Product newProduct = new Product(foodId, foodName, foodType, foodExotic);

        // проверяем, что записи нет в таблице
        Assertions.assertNotEquals(newProduct, checkProduct(foodId),
                "Запись с ID " + foodId + " уже есть в таблице");

        // добавляем продукт в таблицу
        Assertions.assertTrue(addingProduct(foodId, foodName, foodType, foodExotic),
                "Продукт с ID " + foodId + " не добавлен");

        // проверяем, что продукт появился в таблице
        Assertions.assertEquals(newProduct, checkProduct(foodId),
                "Запись с ID " + foodId + " отсутствует");

        // удаляем продукт из таблицы
        Assertions.assertTrue(deleteProduct(foodId), "Продукт с ID " + foodId + " не удален");

        // проверяем, что продукт удален из таблицы
        Assertions.assertNotEquals(newProduct, checkProduct(foodId),
                "Запись с ID " + foodId + " осталась в таблице");
    }
}
