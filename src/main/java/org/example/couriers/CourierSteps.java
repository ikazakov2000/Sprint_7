package org.example.couriers;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.constants.ApiEndPoints;

import static io.restassured.RestAssured.given;
import static org.example.constants.ApiEndPoints.*;

public class CourierSteps {
    public static RequestSpecification requestSpecification() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(ApiEndPoints.BASE_URL);
    }
    @Step("Регистрация нового курьера")
    public ValidatableResponse courierCreate(CourierCreate courierCreate){
        return requestSpecification()
                .body(courierCreate)
                .when()
                .post(COURIER_POST_CREATE)
                .then();
    }

    @Step("Логин курьера")
    public ValidatableResponse courierLogin(CourierLogin courierLogin){
        return requestSpecification()
                .body(courierLogin)
                .when()
                .post(COURIER_POST_LOGIN)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse courierDeleted(int courierID){
        return requestSpecification()
                .when()
                .delete(COURIER_DELETE + courierID)
                .then();
    }
}

