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
    static String randomEmail;

    @BeforeMethod
    public static void startTest() {
        baseUrl = PropertyUtils.getProperty("base.url");
    }

    @Test
    public static void createNewAccount() {
        RestAssured.baseURI = baseUrl;
        randomEmail = RandomEmailGenerator.generateRandomEmail();
        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        Response response = RestAssured.given().contentType(ContentType.JSON).body(requestBody)
                .post("/api/auth/signup");

        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201, "Expected status code is not matched.");
        assertThat(response.jsonPath().getString("data.user.email"), Matchers.equalTo(randomEmail));
    }

    @Test
    public static void userLogin(){
        RestAssured.baseURI = baseUrl;
        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        Response response = RestAssured.given().contentType(ContentType.JSON).body(requestBody)
                .post("/api/auth/login");


        assertThat(response.getStatusCode(), Matchers.is(201));
        assertThat(response.jsonPath().get("data"), Matchers.notNullValue());
        assertThat(response.jsonPath().get("data.user"), Matchers.notNullValue());
        assertThat(response.jsonPath().get("data.session"), Matchers.notNullValue());
        assertThat(response.jsonPath().getString("data.user.email"), Matchers.equalTo(randomEmail));
        assertThat(response.jsonPath().getString("data.session.token_type"), Matchers.equalTo("bearer"));
        assertThat(response.jsonPath().getString("data.session.refresh_token"), Matchers.notNullValue());
        assertThat(response.jsonPath().getString("data.user.id"), Matchers.equalTo(response.jsonPath().getString("data.session.user.id")));
        assertThat(response.jsonPath().getList("data.user.app_metadata.providers"), Matchers.hasItem("email"));
        assertThat(response.jsonPath().getString("data.user.aud"), Matchers.equalTo("authenticated"));
        assertThat(response.jsonPath().getString("data.user.role"), Matchers.equalTo("authenticated"));
        assertThat(response.jsonPath().getString("data.user.created_at"), Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"));
        assertThat(response.jsonPath().getString("data.user.updated_at"), Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"));
    }
}
