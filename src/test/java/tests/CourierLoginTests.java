package tests;

import data.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
public class CourierLoginTests {
    private String login;
    private String password;
    private int courierId;

    private boolean courierCreated = false;
    @Before
    public void setUp() {
        RestAssured.baseURI = CourierData.API_BASE_URL;
        login = CourierData.getRandomLogin();
        password = CourierData.getRandomPassword();

    }

    @After
    public void tearDown() {
        deleteCourier();
    }

    @Test
    @Description("Проверка успешной авторизации курьера")
    public void courierCanLogin() {
        createCourier();
        loginCourier().then().assertThat()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @Description("Проверка ошибки при отсутствии обязательных полей для логина")
    public void loginWithoutRequiredFields() {
        createCourier();
        loginCourierWithoutField("login");
        loginCourierWithoutField("password");
    }

    @Test
    @Description("Проверка ошибки при неверных учетных данных")
    public void loginWithIncorrectCredentials() {
        createCourier();
        loginCourierWithIncorrectCredentials().then().statusCode(404);
    }

    @Test
    @Description("Проверка ошибки при попытке логина несуществующего курьера")
    public void loginWithNonExistingCourier() {
        String login2 = CourierData.getRandomLogin();
        String password2 = CourierData.getRandomPassword();
        loginCourierWithNonExistingCourier(login2, password2).then().statusCode(404);
    }

    @Step("Логин курьера")
    public Response loginCourier() {
        if (courierCreated) {
            return given()
                    .header("Content-type", "application/json")
                    .body("{\n" +
                            "  \"login\": \"" + login + "\",\n" +
                            "  \"password\": \"" + password + "\"\n" +
                            "}")
                    .when()
                    .post("/courier/login");
        }
        return null;
    }
    @Step("Создание курьера")
    public Response createCourier() {
        String firstName = CourierData.getRandomFirstName();

        Response response = given()
                .header("Content-type", "application/json")
                .body("{\n" +
                        "  \"login\": \"" + login + "\",\n" +
                        "  \"password\": \"" + password + "\",\n" +
                        "  \"firstName\": \"" + firstName + "\"\n" +
                        "}")
                .when()
                .post("/courier");

        if (response.getStatusCode() == 201){
            courierCreated = true;
        }
        return response;
    }

    @Step("Логин курьера без поля {field}")
    public Response loginCourierWithoutField(String field) {
        String requestBody;
        switch (field) {
            case "login":
                requestBody = "{\n" +
                        "  \"password\": \"" + password + "\"\n" +
                        "}";
                break;
            case "password":
                requestBody = "{\n" +
                        "  \"login\": \"" + login + "\"\n" +
                        "}";
                break;
            default:
                requestBody = "";
        }
        return given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post("/courier/login");

    }
    @Step("Логин курьера с некорректными данными")
    public Response loginCourierWithIncorrectCredentials() {
        String incorrectPassword = CourierData.getRandomPassword();
        return given()
                .header("Content-type", "application/json")
                .body("{\n" +
                        "  \"login\": \"" + login + "\",\n" +
                        "  \"password\": \"" + incorrectPassword + "\"\n" +
                        "}")
                .when()
                .post("/courier/login");
    }

    @Step("Логин курьера с несуществующими данными")
    public Response loginCourierWithNonExistingCourier(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body("{\n" +
                        "  \"login\": \"" + login + "\",\n" +
                        "  \"password\": \"" + password + "\"\n" +
                        "}")
                .when()
                .post("/courier/login");
    }

    @Step("Получение id курьера")
    public int getCourierId(){
        if (courierCreated) {
            Response response = loginCourier();
            if (response != null && response.getStatusCode() == 200) {
                courierId = response.then().extract().path("id");
                return courierId;
            }
        }

        return 0;
    }
    @Step("Удаление курьера")
    public void deleteCourier() {
        int id = getCourierId();
        if (id !=0){
            given()
                    .header("Content-type", "application/json")
                    .when()
                    .delete("/courier/" + id)
                    .then()
                    .statusCode(200);
        }
    }
}