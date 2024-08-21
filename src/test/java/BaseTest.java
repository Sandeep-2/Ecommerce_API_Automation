import clients.UserClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import utilities.PropertyUtils;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.MatcherAssert.assertThat;

public class BaseTest {

    String baseUrl;
    int statusCode;
    UserClient userClient;


    @BeforeMethod
    public void startTest() {
        userClient = new UserClient();
        baseUrl = userClient.getBaseUrl();
        RestAssured.baseURI = baseUrl;
    }

    @Test
    public void healthCheckTest() {
        Response response = RestAssured.given().get();
        statusCode = response.getStatusCode();
        String statusMessage = response.getBody().asString();
        Assert.assertEquals(statusCode, 200, "Expected status code is not matched.");
        Assert.assertTrue(statusMessage.contains("ok"), "Expected status message 'ok' is not present in the response.");
    }


}
