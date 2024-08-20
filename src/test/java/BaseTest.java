import io.restassured.RestAssured;
import io.restassured.response.Response;
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
    public static void baseTest() {
        RestAssured.baseURI = baseUrl;
        Response response = RestAssured.given().get();
        statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
    }
}
