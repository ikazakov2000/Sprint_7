package org.example.orders;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.example.constants.ApiEndPoints.*;

public class OrderSteps {

    public static RequestSpecification requestSpecification() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }

    @Step("Создание заказа")
    public ValidatableResponse orderCreate(OrderCreate orderCreate){
        return requestSpecification()
                .body(orderCreate)
                .when()
                .post(ORDER_POST_CREATE)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse orderGetList() {
        return requestSpecification()
                .when()
                .get(ORDER_GET_LIST)
                .then();
    }
}
