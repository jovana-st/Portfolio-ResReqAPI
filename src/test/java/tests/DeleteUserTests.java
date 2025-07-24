package tests;

import api.UserAPI;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ApiAssertions;

public class DeleteUserTests {

    private UserAPI userAPI;

    @BeforeClass
    public void setup(){
        userAPI = new UserAPI();
    }

    @Test //Negative scenario - no access to API
    public void deleteUserNoAccess() {

        Response response = userAPI.deleteUser(1, false);
        ApiAssertions.assertStatusCode(response, 401);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Missing API key.");
    }

    @Test //Positive scenario - the User is successfully deleted
    public void deleteUser() {

        Response response = userAPI.deleteUser(1, true);
        ApiAssertions.assertStatusCode(response, 204);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertEmptyBody(response);
    }

    @Test //Negative scenario - Missing User with the specified ID
    public void deleteUserNonExistentUser() {

        Response response = userAPI.deleteUser(999999999, true);
        ApiAssertions.assertStatusCode(response, 204);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertEmptyBody(response);
    }

}