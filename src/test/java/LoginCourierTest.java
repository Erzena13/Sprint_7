import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.steps.CourierSteps;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginCourierTest extends AbstractTest {
    private CourierSteps courierSteps = new CourierSteps();
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void courierCanLogin() {
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(5);

        courierSteps
                .createCourier(login, password, firstName)
                .then()
                .statusCode(201);

        courierSteps
                .login(login, password)
                .then()
                .statusCode(200)
                .body("id", Matchers.notNullValue());
    }

    @Test
    public void loginWithoutLogin() {
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(5);

        courierSteps
                .createCourier(login, password, firstName)
                .then()
                .statusCode(201);

        courierSteps
                .login(null, password)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginWithoutPassword() {
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(5);

        courierSteps
                .createCourier(login, password, firstName)
                .then()
                .statusCode(201);

        courierSteps
                .login(login, "")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginWithWrongLogin() {
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(5);

        courierSteps
                .createCourier(login, password, firstName)
                .then()
                .statusCode(201);

        courierSteps
                .login("wrong_" + login, password)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginWithWrongPassword() {
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(5);

        courierSteps
                .createCourier(login, password, firstName)
                .then()
                .statusCode(201);

        courierSteps
                .login(login, "wrong_" +  password)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginWithNonExistentUser() {
        courierSteps
                .login("non_existent_user", "some_password")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
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
