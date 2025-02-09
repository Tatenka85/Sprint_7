import com.github.javafaker.Faker;
import courier.CourierCreateSteps;
import courier.CourierLoginSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_OK;

public class CourierCreateTest {

    private String courierId;
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        login = faker.name().username();
        password = faker.internet().password();
        firstName = faker.name().firstName();
        Base.baseUrl();
    }

    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверка, что возвращаемые тело ответа 'ok: true' и код ответа '201'")
    public void createNewCourier() {
        Response response = CourierCreateSteps.createCourier(login, password, firstName);
        courierId = response.jsonPath().getString("id");
        CourierCreateSteps.checkCreatingCourier(response);
    }

    @Test
    @DisplayName("Создание курьера с заполнением только обязательных полей")
    @Description("Проверка, что возвращаемые тело ответа 'ok: true' и код ответа '201'")
    public void createCourierWithOnlyRequiredFields() {
        Response response = CourierCreateSteps.createCourierWithoutFirstName(login, password);
        courierId = response.jsonPath().getString("id");
        CourierCreateSteps.checkCreatingCourier(response);
    }

    @Test
    @DisplayName("Невозможность создать курьера, логин которого уже есть в базе данных")
    @Description("Проверка, что возвращаемые тело ответа 'message: Этот логин уже используется. Попробуйте другой' и код ответа '409'")
    public void cantCreateDuplicateCourier() {
        CourierCreateSteps.createCourier(login, password, firstName);
        Response response = CourierCreateSteps.createCourier(login, password, firstName);
        courierId = CourierLoginSteps.loginCourierId(login, password);
        CourierCreateSteps.checkCreatingCourierWithDuplicatedLogin(response);
    }

    @Test
    @DisplayName("Невозможность создать курьера, если не ввести логин")
    @Description("Проверка, что возвращаемые тело ответа 'message: Недостаточно данных для создания учетной записи' и код ответа '400'")
    public void checkResponseWithoutLogin() {
        Response response = CourierCreateSteps.createCourier("", password, firstName);
        CourierCreateSteps.checkCreatingCourierWithoutLoginPassword(response);
    }

    @Test
    @DisplayName("Невозможность создать курьера, если не ввести пароль")
    @Description("Проверка, что возвращаемые тело ответа 'message: Недостаточно данных для создания учетной записи' и код ответа '400'")
    public void checkResponseWithoutPassword() {
        Response response = CourierCreateSteps.createCourier(login, "", firstName);
        CourierCreateSteps.checkCreatingCourierWithoutLoginPassword(response);
    }

    @After
    public void deleteCourierAfterTest() {
        if (courierId != null) {
            Response response = CourierCreateSteps.deleteCourier(courierId);
            response.then().statusCode(SC_OK);
        }
    }
}