import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.steps.CourierSteps;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreateTest extends AbstractTest {
    private CourierSteps courierSteps = new CourierSteps();
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    public void successCreateCourier() {
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(5);

        courierSteps
                .createCourier(login, password, firstName)
                .then()
                .statusCode(201)
                .body("ok", Matchers.is(true));
    }

    @Test
    public void createErrorDuplicateCourier() {
        login = "duplicate_login";
        password = "duplicate_password";
        firstName = "duplicate_firstName";

        courierSteps
                .createCourier(login, password, firstName)
                .then()
                .statusCode(201);

        courierSteps
                .createCourier(login, password, firstName)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void createWithoutLogin() {
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(5);

        courierSteps
                .createCourier(null, password, firstName)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createWithoutPassword() {
        login = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(5);

        courierSteps
                .createCourier(login, null, firstName)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createCourierWithExistingLogin() {
        login = "existing_login_courier";
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(5);

        courierSteps
                .createCourier(login, password, firstName)
                .then()
                .statusCode(201);
        courierSteps
                .createCourier(login, password, firstName)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @After
    public void tearDown() {
        if (login != null && password != null) {
            Response response = courierSteps.login(login, password);
            if (response.getStatusCode() == 200 && response.body().jsonPath().get("id") != null) {
                int id = response.body().jsonPath().get("id");
                courierSteps.delete(id);
            }
        }
    }
}
