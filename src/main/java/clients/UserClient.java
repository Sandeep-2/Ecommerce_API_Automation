package clients;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import io.restassured.path.json.JsonPath;

public class UserClient {
    private static String baseUrl;
    private static String randomEmail;

    public UserClient() {
        baseUrl = PropertyUtils.getProperty("base.url");
        randomEmail = RandomEmailGenerator.generateRandomEmail();
        RestAssured.baseURI = baseUrl;
    }

    public Response createUser() {
        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        Response response = RestAssured.given().contentType(ContentType.JSON).body(requestBody)
                .post("/api/auth/signup");

        return response;
    }

    public Response authenticateUser() {
        String loginRequestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        Response response = RestAssured.given().contentType(ContentType.JSON).body(loginRequestBody)
                .post("/api/auth/login");

        return response;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getRandomEmail() {
        return randomEmail;
    }
}
