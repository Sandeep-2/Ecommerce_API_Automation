import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.PropertyUtils;

public class BaseTest {

    static String baseUrl ;
    static int statusCode;

    @BeforeMethod
    public static void startTest(){
        baseUrl = PropertyUtils.getProperty("base.url");
    }

    @Test
    public static void healthCheckTest() {
        RestAssured.baseURI = baseUrl;
        Response response = RestAssured.given().get();
        statusCode = response.getStatusCode();
        String statusMessage = response.getBody().asString();
        Assert.assertEquals(statusCode, 200, "Expected status code is not matched.");
        Assert.assertTrue(statusMessage.contains("ok"), "Expected status message 'ok' is not present in the response.");
    }
}
