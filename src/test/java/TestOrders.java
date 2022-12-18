import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class TestOrders {

    private final Order order;

    public TestOrders(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                {new Order("firstName", "lastName", "address", "Сокол", "phone", 5, "2020-06-06", "comment")},
                {new Order("firstName", "lastName", "address", "Сокольники", "phone", 5, "2020-06-06", "comment", new String[]{"BLACK"})},
                {new Order("firstName", "lastName", "address", "Соколиная Гора", "phone", 5, "2020-06-06", "comment", new String[]{"GREY"})},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Create order with different colors")
    public void setMetroAndCheckCreateOrder() {


        String metro = order.getMetroStation();
        String id = RestAssured.given()
                .header("Content-type", "application/json")
                .get("/api/v1/stations/search" + metro)
                .then().extract().body().path("number");
        order.setMetroStation(id);

        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");

                response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);

    }

}
