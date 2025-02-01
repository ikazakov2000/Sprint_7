package data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class CourierData {
    public static final String API_BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1";
    private static final Faker faker = new Faker(new Locale("en"));


    public static String getRandomLogin(){
        return faker.name().username();
    }

    public static String getRandomPassword(){
        return faker.internet().password(6, 10, true, true);
    }

    public static String getRandomFirstName(){
        return faker.name().firstName();
    }
}
