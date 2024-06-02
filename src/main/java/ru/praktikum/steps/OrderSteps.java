package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum.dto.OrderCreateRequest;

import static ru.praktikum.EndPoints.*;

public class OrderSteps {
    @Step("Создание заказа")
    public Response createOrder(String[] colors) {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setColor(colors);

        return RestAssured.given()
                .body(orderCreateRequest)
                .post(CREATE_ORDER);
    }

    @Step("Получения списка заказа")
    public Response getOrderList() {
        return RestAssured.given()
                .get(ORDER_LIST);
    }
}
