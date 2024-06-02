package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.dto.CourierCreateRequest;
import ru.praktikum.dto.CourierLoginRequest;

import static io.restassured.RestAssured.given;
import static ru.praktikum.EndPoints.*;

public class CourierSteps {
    @Step("Создание курьера с логином '{login}', паролем '{password}' и именем '{firstName}'")
    public Response createCourier(String login, String password, String firstName) {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setPassword(password);
        courierCreateRequest.setLogin(login);
        courierCreateRequest.setFirstName(firstName);

        return given()
                .body(courierCreateRequest)
                .when()
                .post(COURIER);
    }

    @Step("Логин курьера с логином '{login}' и паролем '{password}'")
    public Response login(String login, String password) {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);
        courierLoginRequest.setPassword(password);

        return given()
                .body(courierLoginRequest)
                .when()
                .post(LOGIN);
    }

    @Step("Удаление курьера с ID '{id}'")
    public Response delete(Integer id) {
        return given()
                .pathParam("id", id)
                .when()
                .delete(COURIER_DELETE);
    }
}
