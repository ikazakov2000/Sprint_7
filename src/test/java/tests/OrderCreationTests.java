package tests;

import data.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTests {
    private String[] colors;

    public OrderCreationTests(String[] colors) {
        this.colors = colors;
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = CourierData.API_BASE_URL;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}},
        });
    }

    @Test
    @Description("Проверка создания заказа с различными цветами")
    public void orderCanBeCreatedWithDifferentColors() {
        createOrder(colors).then().assertThat().statusCode(201).body("track", notNullValue());
    }

    @Step("Создание заказа с цветами {colors}")
    public Response createOrder(String[] colors) {
        String colorString = colors.length == 0 ? "null" : "[\"" + String.join("\", \"", colors) + "\"]";
        return given()
                .header("Content-type", "application/json")
                .body("{\n" +
                        "  \"firstName\": \"Naruto\",\n" +
                        "  \"lastName\": \"Uzumaki\",\n" +
                        "  \"address\": \"Konoha, 1\",\n" +
                        "  \"metroStation\": 1,\n" +
                        "  \"phone\": \"+79999999999\",\n" +
                        "  \"rentTime\": 5,\n" +
                        "  \"deliveryDate\": \"2024-02-20\",\n" +
                        "  \"comment\": \"Test Order\",\n" +
                        "  \"color\": " + colorString + "\n" +
                        "}")
                .when()
                .post("/orders");

    }
}