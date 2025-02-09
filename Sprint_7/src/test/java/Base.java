import io.restassured.RestAssured;

public class Base {

    public static void baseUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

}