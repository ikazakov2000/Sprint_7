package org.example.couriers;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourierLoginTests {
    private CourierSteps courierSteps;
    private CourierCreate courierCreate;
    private CourierLogin courierLogin;
    private CourierResult courierResult;
    int courierID;

    @Before
    @Step("Создание тестовых данных курьера")
    public void setUp() {
        courierSteps = new CourierSteps();
        courierCreate = new CourierCreate("13p3pp", "2pppp", "q22");
        courierLogin = CourierLogin.from(courierCreate);
        courierResult = new CourierResult();
    }

    @Test
    @DisplayName("Логин курьера")
    @Description("Проверяем, что курьер может авторизоваться")
    public void courierLogin(){
        ValidatableResponse validatableResponse = courierSteps.courierLogin(courierLogin);
        courierResult.courierLoginValid(validatableResponse);
        courierID = validatableResponse.extract().path("id");
    }

    @Test
    @DisplayName("Логин курьера с пустым полем логина")
    @Description("Проверяем, что курьер не может войти в систему без логина")
    public void courierLoginWithOutLogin(){
        courierLogin.setLogin(null);
        ValidatableResponse validatableResponse = courierSteps.courierLogin(courierLogin);
        courierResult.courierLoginError(validatableResponse);
    }

    @Test
    @DisplayName("Логин курьера с пустым полем пароля")
    @Description("Проверяем, что курьер не может войти в систему без пароля")
    public void courierLoginWithOutPassword(){
        courierLogin.setPassword("");
        ValidatableResponse validatableResponse = courierSteps.courierLogin(courierLogin);
        courierResult.courierLoginError(validatableResponse);
    }

    @Test
    @DisplayName("Логин курьера с пустыми полями пароля и логина")
    @Description("Проверяем, что курьер не может войти в систему без пароля и логина")
    public void courierLoginWithOutDate(){
        courierLogin.setLogin(null);
        courierLogin.setPassword("");
        ValidatableResponse validatableResponse = courierSteps.courierLogin(courierLogin);
        courierResult.courierLoginError(validatableResponse);
    }
    @Test
    @DisplayName("Логин курьера с несуществующей парой логин-пароль")
    @Description("Проверяем, что курьер не может войти в систему с несуществующей парой логин-пароль")
    public void courierLoginNonExistData(){
        courierLogin.setLogin("Aqaqa");
        courierLogin.setPassword("Aqaqa");
        ValidatableResponse validatableResponse = courierSteps.courierLogin(courierLogin);
        courierResult.courierLoginNonExistingData(validatableResponse);
    }

    @After
    @Step("Удаление курьера")
    public void deleteCourier() {
        if (courierID != 0) {
            courierSteps.courierDeleted(courierID);
        }
    }
}

