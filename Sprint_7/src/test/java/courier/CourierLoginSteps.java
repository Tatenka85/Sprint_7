package courier;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static constants.Constants.COURIER_LOGIN_URL;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginSteps {

    @Step("Авторизация")
    public static String loginCourierId(String login, String password) {
        CourierLogin courierLogin = new CourierLogin(login, password);
        Response responseOfLoginCourier =
                given()
                        .contentType(ContentType.JSON)
                        .body(courierLogin)
                        .when()
                        .post(COURIER_LOGIN_URL);
        responseOfLoginCourier.then().statusCode(SC_OK)
                .and()
                .assertThat().body("id", notNullValue());

        return responseOfLoginCourier.jsonPath().getString("id");
    }

    @Step("Авторизация курьера на сайте")
    public static Response loginCourierResponse(String login, String password) {
        CourierLogin courierLogin = new CourierLogin(login, password);
        return given()
                .contentType(ContentType.JSON)
                .body(courierLogin)
                .when()
                .post(COURIER_LOGIN_URL);
    }

    @Step("После успешной авторизации получаем id и код ответа SC_OK")
    public static void responseAfterSuccessfulAuthorization(Response response) {
        response.then()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());
    }

    @Step("Ошибка и статус SC_NOT_FOUND при некорректном логине/пароле")
    public static void errorWhenLoginOrPasswordIncorrect(Response response) {
        response.then()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Ошибка и статус SC_BAD_REQUEST при пустых полях логин/пароль")
    public static void errorWhenLoginPasswordEmpty(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}