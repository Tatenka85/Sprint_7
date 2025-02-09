package order;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/*import java.util.List;*/

/*import static constants.Constants.*;*/
import static constants.Constants.ORDER_CREATE_URL;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;


public class OrderCreationSteps {

    @Step("Успешное создание заказа")
    /*public static Response OrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        Order orderTest = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        return given()
                .contentType(ContentType.JSON)
                .body(orderTest)
                .when()
                .post(ORDER_CREATE_URL);
    }*/

    public static Response OrderTest(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDER_CREATE_URL);
    }

    @Step("Код ответа SC_CREATED и наличие 'track' в ответе при успешном создании заказа")
    public static void orderCreationSuccess(Response response) {
        response.then()
                .statusCode(SC_CREATED)
                .and()
                .body("track", notNullValue());
    }

    @Step("Получение списка заказов")
    public static Response getOrdersList() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                /*.get(GET_ORDERS_URL);*/
                .get(ORDER_CREATE_URL);
    }

    @Step("Код ответа SC_OK и список заказов не пустой")
    public static void checkResponseAndStatusCodeWhenGetOrderList(Response response) {
        response.then()
                .statusCode(SC_OK)
                .and()
                .body("orders", notNullValue());
    }
    @Step("Отмена заказа")
    public static Response cancelOrder(int track) {
        return given()
                .contentType(ContentType.JSON)
                /*.body("{\"track\": " + track + "}")*/
                .when()
                /*.put(ORDER_CANCEL_URL);
                .put(ORDER_CREATE_URL);*/
                .put(ORDER_CREATE_URL + "/cancel?track=" + track);
    }

    @Step("Код ответа SC_OK при отмене заказа")
    public static void cancelOrderSuccess(Response response) {
        response.then()
                .statusCode(SC_OK)
                .and()
                .body("ok", notNullValue());
    }

}