import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public class TestCourierNoPass {
    Courier courier_two = new Courier("newLogin");

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    @DisplayName("Check negative create Couriers no mandatory field")
    public void createCourierNoPass() {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(courier_two)
                .when()
                .post("/api/v1/courier");

        response.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

    }
}
