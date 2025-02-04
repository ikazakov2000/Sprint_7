package org.example.couriers;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class CourierCreationTests {
    private CourierSteps courierSteps;
    private CourierCreate courierCreate;
    private CourierResult courierResult;

    int courierID;

    @Before
    @Step("Создание тестовых данных курьера")
    public void setUp() {
        courierSteps = new CourierSteps();
        courierCreate = new CourierCreate("Login33", "2pppp", "Maks");
        courierResult = new CourierResult();
    }

    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверяем, что курьера можно создать")
    public void courierCreateOk() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        String loginRandom = fakeValuesService.bothify("????###");

        courierCreate.setLogin(loginRandom);
        ValidatableResponse validatableResponse = courierSteps.courierCreate(courierCreate);
        courierResult.courierCreateValid(validatableResponse);
        CourierLogin courierLogin = CourierLogin.from(courierCreate);
        courierID = courierSteps.courierLogin(courierLogin).extract().jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Создание курьера с уже существующими данными")
    @Description("Проверяем, что курьера нельзя создать с уже существующими данными")
    public void courierCreateExistData() {
        courierSteps.courierCreate(courierCreate);
        ValidatableResponse validatableResponse = courierSteps.courierCreate(courierCreate);
        courierResult.courierCreateExistingData(validatableResponse);
    }

    @Test
    @DisplayName("Создание курьера с пустым полем логина")
    @Description("Проверяем, что курьера нельзя создать без логина")
    public void courierCreateWithOutLogin() {
        courierCreate.setLogin(null);
        ValidatableResponse validatableResponse = courierSteps.courierCreate(courierCreate);
        courierResult.courierCreateError(validatableResponse);
    }

    @Test
    @DisplayName("Создание курьера с пустым полем пароля")
    @Description("Проверяем, что курьера нельзя создать без пароля")
    public void courierCreateWithOutPassword() {
        courierCreate.setPassword(null);
        ValidatableResponse validatableResponse = courierSteps.courierCreate(courierCreate);
        courierResult.courierCreateError(validatableResponse);
    }

    @Test
    @DisplayName("Создание курьера с пустым полем пароля")
    @Description("Проверяем, что курьера нельзя создать без пароля")
    public void courierCreateWithOutDate() {
        courierCreate.setLogin(null);
        courierCreate.setPassword(null);
        ValidatableResponse validatableResponse = courierSteps.courierCreate(courierCreate);
        courierResult.courierCreateError(validatableResponse);
    }

    @After
    @Step("Удаление тестовых данных курьера")
    public void courierDeleted(){
        if(courierID != 0) {
            courierSteps.courierDeleted(courierID);
        }
    }
}