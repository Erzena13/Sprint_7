import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.steps.OrderSteps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;

public class OrderListTest extends AbstractTest {
    private OrderSteps orderSteps = new OrderSteps();

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void getOrderList() {
        Response response = orderSteps.getOrderList();

        response.then()
                .statusCode(200);
        assertThat(response.getBody().asString(), not(isEmptyOrNullString()));
    }
}
