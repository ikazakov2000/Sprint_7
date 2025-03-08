package org.example.orders;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.greaterThan;

public class OrderResult {

    @Step("Успешное создание заказа")
    public int orderResultCreate(ValidatableResponse response) {
        return response.assertThat()
                .statusCode(SC_CREATED)
                .body("track", greaterThan(0))
                .extract()
                .path("track");
    }
}

