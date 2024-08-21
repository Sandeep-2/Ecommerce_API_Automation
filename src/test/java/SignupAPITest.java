import clients.UserClient;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.*;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import static org.hamcrest.MatcherAssert.assertThat;

public class SignupAPITest extends BaseTest{

    @Test
    public void createNewAccount() {

        Response response = userClient.createUser();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201, "Expected status code is not matched.");
        assertThat(response.jsonPath().getString("data.user.email"), Matchers.equalTo(userClient.getRandomEmail()));
    }

    @Test(dependsOnMethods = "createNewAccount")
    public void userLoginWithValidCredentials() {
        Response response = userClient.authenticateUser();

        assertThat(response.getStatusCode(), Matchers.is(200));
        assertThat(response.jsonPath().get("data"), Matchers.notNullValue());
        assertThat(response.jsonPath().get("data.user"), Matchers.notNullValue());
        assertThat(response.jsonPath().get("data.session"), Matchers.notNullValue());
        assertThat(response.jsonPath().getString("data.user.email"), Matchers.equalTo(userClient.getRandomEmail()));
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
