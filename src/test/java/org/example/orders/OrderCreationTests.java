package org.example.orders;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreationTests {
    private OrderSteps orderSteps;
    private OrderCreate orderCreate;
    private OrderResult orderResult;
    private List<String> color;

    public OrderCreationTests(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters (name = "Color Scooter - {0}")
    public static Object[][] dataGen() {
        return new Object[][] {
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of()}
        };
    }

    @Before
    public void setUp() {
        orderSteps = new OrderSteps();
        orderResult = new OrderResult();
    }

    @Test
    @DisplayName("Создание заказа с самокатами разных цветов")
    @Description("Цвет самоката")
    public void orderCreateColorWithParam() {
        orderCreate = new OrderCreate(color);
        ValidatableResponse validatableResponse = orderSteps.orderCreate(orderCreate);
        orderResult.orderResultCreate(validatableResponse);
    }
}