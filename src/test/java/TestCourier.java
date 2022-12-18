import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public class TestCourier {
    Courier courier = new Courier("login123321", "pass");

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check create Courier mandatory fields") // имя теста
    public void createCourierLogPass() {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response.then().assertThat().body("ok", is(true))
                .and()
                .statusCode(201);

    }

    @Test
    @DisplayName("Check negative create two Couriers mandatory fields") // имя теста
    public void createTwoCouriersLogPass() {
        RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().assertThat().statusCode(201);

        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
                //.then().assertThat().statusCode(409);
        response.then().assertThat().body("message", is("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);


    }



    @After
    public void clean(){
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
