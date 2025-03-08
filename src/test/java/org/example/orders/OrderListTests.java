package org.example.orders;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;

public class OrderListTests {
    private OrderSteps orderSteps;

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем, что список заказов успешно получен, что он не пустой")
    public void orderList() {
        orderSteps = new OrderSteps();
        ValidatableResponse validatableResponse = orderSteps.orderGetList();
        validatableResponse.assertThat()
                .statusCode(SC_OK)
                .body("orders", CoreMatchers.notNullValue());
    }
}