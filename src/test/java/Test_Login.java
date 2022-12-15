import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class Test_Login {

    Courier courier = new Courier("login123321", "pass");

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";

        RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().assertThat().statusCode(201);

    }

    @Test
    @DisplayName("Check login correct cred")
    public void loginCourierCorrectCred() {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);

    }


    @Test
    @DisplayName("Check negative not correct pass")
    public void loginCourierNotCorrectPass() {

         courier.setPassword("NoPass");


        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        courier.setPassword("pass");

    }

    @Test
    @DisplayName("Check negative not correct login")
    public void loginCourierNotCorrectLogin() {

        courier.setLogin("NoLogin");


        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);


        courier.setLogin("login123321");

    }

    @Test
    @DisplayName("Check negative not login")
    public void loginCourierNotLogin() {

        courier.setLogin("");


        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        response.then().assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);


        courier.setLogin("login123321");

    }

    @Test
    @DisplayName("Check negative not pass")
    public void loginCourierNotPass() {

        courier.setPassword("");


        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        response.then().assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);

        courier.setPassword("pass");

    }

    @Test
    @DisplayName("Check negative not this Courier")
    public void loginCourierNotCorrect() {

        courier.setPassword("NoThisPAss");
        courier.setLogin("NoThisLogin");


        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        courier.setPassword("pass");
        courier.setLogin("login123321");

    }


    @After
    public void Clean(){
        int id = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");

        String str = Integer.toString(id);
        RestAssured.given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/" + str)
                .then().statusCode(200);

    }

}
