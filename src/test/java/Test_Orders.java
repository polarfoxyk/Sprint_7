import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class Test_Orders {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    List<Order> orders = new ArrayList<Order>();


    public List<Order> getOrders() {
        return orders;
    }

    @Test
    @DisplayName("Create order with different colors")
    public void SetMetro() {

        orders.add(new Order("firstName", "lastName", "address", "Сокол", "phone", 5, "2020-06-06", "comment"));
        orders.add(new Order("firstName", "lastName", "address", "Сокольники", "phone", 5, "2020-06-06", "comment", new String[]{"BLACK"}));
        orders.add(new Order("firstName", "lastName", "address", "Соколиная Гора", "phone", 5, "2020-06-06", "comment", new String[]{"GREY"}));

        String metro = orders.get(0).getMetroStation();
        String id = RestAssured.given()
                .header("Content-type", "application/json")
                .get("/api/v1/stations/search" + metro)
                .then().extract().body().path("number");
        orders.get(0).setMetroStation(id);

        String metro_two = orders.get(1).getMetroStation();
        String id_two = RestAssured.given()
                .header("Content-type", "application/json")
                .get("/api/v1/stations/search" + metro_two)
                .then().extract().body().path("number");
        orders.get(1).setMetroStation(id_two);

        String metro_three = orders.get(2).getMetroStation();
        String id_three = RestAssured.given()
                .header("Content-type", "application/json")
                .get("/api/v1/stations/search" + metro_three)
                .then().extract().body().path("number");
        orders.get(2).setMetroStation(id_three);




        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(orders)
                .when()
                .post("/api/v1/orders");

                response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);

    }

}
