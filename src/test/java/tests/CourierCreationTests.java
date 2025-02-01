package tests;

import data.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class CourierCreationTests {
    private String login;
    private String password;
    private String firstName;
    private int courierId;


    @Before
    public void setUp() {
        RestAssured.baseURI = CourierData.API_BASE_URL;
        login = CourierData.getRandomLogin();
        password = CourierData.getRandomPassword();
        firstName = CourierData.getRandomFirstName();
    }

    @Test
    @Description("Проверка успешного создания курьера")
    public void courierCanBeCreated() {
        Response response = createCourier();
        checkCourierCreation(response, 201, true);
        deleteCourier();
    }

    @Test
    @Description("Проверка невозможности создания двух курьеров с одинаковыми данными")
    public void cannotCreateTwoSameCouriers() {
        createCourier();
        createCourier().then().statusCode(409);
        deleteCourier();
    }

    @Test
    @Description("Проверка ошибки при отсутствии обязательных полей")
    public void createCourierWithoutRequiredFields() {
        createCourierWithoutField("login");//.then().statusCode(400);
        createCourierWithoutField("password");//.then().statusCode(400);
        createCourierWithoutField("firstName");//.then().statusCode(400);

    }

    @Test
    @Description("Проверка ошибки при создании курьера с существующим логином")
    public void createCourierWithExistingLogin() {
        createCourier();
        String login2 = login;
        String password2 = CourierData.getRandomPassword();
        String firstName2 = CourierData.getRandomFirstName();

        given()
                .header("Content-type", "application/json")
                .body("{\n" +
                        "  \"login\": \"" + login2 + "\",\n" +
                        "  \"password\": \"" + password2 + "\",\n" +
                        "  \"firstName\": \"" + firstName2 + "\"\n" +
                        "}")
                .when()
                .post("/courier")
                .then().statusCode(409);
        deleteCourier();
    }


    @Step("Создание курьера")
    public Response createCourier() {
        return given()
                .header("Content-type", "application/json")
                .body("{\n" +
                        "  \"login\": \"" + login + "\",\n" +
                        "  \"password\": \"" + password + "\",\n" +
                        "  \"firstName\": \"" + firstName + "\"\n" +
                        "}")
                .when()
                .post("/courier");
    }
    @Step("Проверка создания курьера")
    public void checkCourierCreation(Response response, int expectedStatusCode, boolean expectedOk) {
        response.then().assertThat()
                .statusCode(expectedStatusCode)
                .body("ok", is(expectedOk));
    }
    @Step("Создание курьера без поля {field}")
    public Response createCourierWithoutField(String field) {
        String requestBody;
        switch (field) {
            case "login":
                requestBody = "{\n" +
                        "  \"password\": \"" + password + "\",\n" +
                        "  \"firstName\": \"" + firstName + "\"\n" +
                        "}";
                break;
            case "password":
                requestBody = "{\n" +
                        "  \"login\": \"" + login + "\",\n" +
                        "  \"firstName\": \"" + firstName + "\"\n" +
                        "}";
                break;
            case "firstName":
                requestBody = "{\n" +
                        "  \"login\": \"" + login + "\",\n" +
                        "  \"password\": \"" + password + "\"\n" +
                        "}";
                break;
            default:
                requestBody = "";
        }
        return given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post("/courier");

    }
    @Step("Логин курьера для получения id")
    public void loginCourier() {
        courierId = given()
                .header("Content-type", "application/json")
                .body("{\n" +
                        "  \"login\": \"" + login + "\",\n" +
                        "  \"password\": \"" + password + "\"\n" +
                        "}")
                .when()
                .post("/courier/login")
                .then().assertThat()
                .statusCode(200)
                .extract().path("id");

    }


    @Step("Удаление курьера")
    public void deleteCourier() {
        loginCourier();
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/courier/" + courierId)
                .then()
                .statusCode(200);
    }
}