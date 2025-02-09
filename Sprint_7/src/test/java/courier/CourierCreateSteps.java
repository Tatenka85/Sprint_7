package courier;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static constants.Constants.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateSteps {

    @Step("Создаём курьера")
    public static Response createCourier(String login, String password, String firstName) {
        CreateCourier createCourier = new CreateCourier(login, password, firstName);
        return given()
                .contentType(ContentType.JSON)
                .body(createCourier)
                .when()
                .post(COURIER_CREATE_URL);
    }

    @Step("Статус ответа: SC_CREATED, поле 'ok': true при успешном создании")
    public static void checkCreatingCourier(@org.jetbrains.annotations.NotNull Response response) {
        response.then()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", equalTo(true));
    }

    @Step("Проверка создания курьера только с обязательными полями")
    public static Response createCourierWithoutFirstName(String login, String password) {
        CreateCourier createCourier = new CreateCourier(login, password, "");
        return given()
                .contentType(ContentType.JSON)
                .body(createCourier)
                .when()
                .post(COURIER_CREATE_URL);
    }

    @Step("Статус ответа: SC_BAD_REQUEST, поле 'message': 'Недостаточно данных для создания учетной записи' при создании учетной записи без логина/пароля")
    public static void checkCreatingCourierWithoutLoginPassword(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Статус ответа: SC_CONFLICT, поле 'message': 'Этот логин уже используется. Попробуйте другой.' при создании учетной записи с дублирующимся логином")
    public static void checkCreatingCourierWithDuplicatedLogin(Response response) {
        response.then()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется."));
    }

    @Step("Удаление курьера")
    public static Response deleteCourier(String id) {
        Response responseOfDeleteCourier =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .delete(COURIER_CREATE_URL + "/" + id);
        responseOfDeleteCourier.then().statusCode(SC_OK)
                .and()
                .assertThat().body("ok", equalTo(true));
        return responseOfDeleteCourier;
    }
}