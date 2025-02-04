package org.example.couriers;

public class CourierLogin {
    private String login;
    private String password;

    public CourierLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierLogin from(CourierCreate courierCreate){
        return new CourierLogin(courierCreate.getLogin(), courierCreate.getPassword());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void getPassword(String password) {
        this.password = password;
    }
}
