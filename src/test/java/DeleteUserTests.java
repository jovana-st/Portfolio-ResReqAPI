import io.restassured.response.Response;
import models.CreateUserBody;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DeleteUserTests {

    private UserAPI userAPI;

    @BeforeClass
    public void setup(){
        userAPI = new UserAPI();
    }

    @Test //Negative scenario - no access to API
    public void deleteUserNoAccess() {

        Response response = userAPI.deleteUser(1, false);
        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("error"), "Missing API key.");
    }

    @Test //Positive scenario - the User is successfully deleted
    public void deleteUser() {

        Response response = userAPI.deleteUser(1, true);
        Assert.assertEquals(response.statusCode(), 204);
    }

    @Test //Negative scenario - Missing User with the specified ID
    public void deleteUserNoUserId() {

        Response response = userAPI.deleteUser(999999999, true);
        Assert.assertEquals(response.statusCode(), 204);
    }

}