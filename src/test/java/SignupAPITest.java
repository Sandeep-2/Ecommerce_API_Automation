import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;

import static org.hamcrest.MatcherAssert.assertThat;

public class SignupAPITest {

    static String baseUrl;
    static int statusCode;

    @BeforeMethod
    public static void startTest() {
        baseUrl = PropertyUtils.getProperty("base.url");
    }

    @Test
    public static void createNewAccount() {
        RestAssured.baseURI = baseUrl;
        String randomEmail = RandomEmailGenerator.generateRandomEmail();
        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        Response response = RestAssured.given().contentType(ContentType.JSON).body(requestBody)
                .post("/api/auth/signup");
        System.out.println(response.toString());
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201, "Expected status code is not matched.");
        assertThat(response.jsonPath().getString("data.user.email"), Matchers.equalTo(randomEmail));
    }
}
