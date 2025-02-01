package tests;

import data.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class OrderListTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = CourierData.API_BASE_URL;
    }
    @Test
    @Description("Проверка получения списка заказов")
    public void orderListIsReturned() {
        int statusCode = getOrderList().getStatusCode();
        assertTrue("Status code is not 200 or 504. Actual status code: " + statusCode, statusCode == 200 || statusCode == 504);
        getOrderList().then().assertThat().body("orders", notNullValue());
    }
    @Step("Получение списка заказов")
    public Response getOrderList() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/orders");
    }
}