import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.OrderCreationSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class OrderTest {
    private final List<String> color;
    private int track;

    public OrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Collection<Object[]> colorParameters() {
        return Arrays.asList(new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {List.of()}
        });
    }

    @Before
    public void setUp() {
        Base.baseUrl();
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с разными цветами самокатов " +
            "либо вообще без указанного цвета и проверка, что значение 'track' в ответе не пустое и код ответа 200")
    /*public void OrderCreate() {
        String firstName = "Tatyana";
        String lastName = "Belanova";
        String address = "Dostyk, 10";
        int metroStation = 2;
        String phone = "+7 700 777 77 77";
        int rentTime = 2;
        String deliveryDate = "2025-06-06";
        String comment = "А вообще, я хотела розовый";
        Response response = OrderCreationSteps.OrderTest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        track = response.jsonPath().getInt("track");
        OrderCreationSteps.orderCreationSuccess(response);
    }*/

    public void OrderCreate() {
        // Используем Builder для создания объекта Order с дефолтными значениями
        Order order = Order.builder()
                .firstName("Tatyana")
                .lastName("Belanova")
                .address("Dostyk, 10")
                .metroStation(2)
                .phone("+7 700 777 77 77")
                .rentTime(2)
                .deliveryDate("2025-06-06")
                .comment("А вообще, я хотела розовый")
                .color(color) // Передаем список цветов
                .build();

        // Создаем заказ
        Response response = OrderCreationSteps.OrderTest(order);
        track = response.jsonPath().getInt("track");
        OrderCreationSteps.orderCreationSuccess(response);
    }

    @After
    public void cancel() {
        if (track != 0) {
            Response cancelResponse = OrderCreationSteps.cancelOrder(track);
            OrderCreationSteps.cancelOrderSuccess(cancelResponse);
        }
    }
}