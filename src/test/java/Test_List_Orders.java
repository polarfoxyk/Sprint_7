import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;


import static org.hamcrest.Matchers.notNullValue;

public class Test_List_Orders {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Get Order list")
    public void GetOrderList(){
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");

        response.then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(200);

    }

}
