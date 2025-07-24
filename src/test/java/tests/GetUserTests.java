package tests;

import api.UserAPI;
import io.restassured.response.Response;
import models.User;
import models.UserData;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ApiAssertions;
import utils.RegexHelper;

import java.util.List;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

public class GetUserTests {

    private UserAPI userAPI;

    @BeforeClass
    public void setup(){
        userAPI = new UserAPI();
    }

    @Test //Positive scenario - return all users
    public void getAllUsers() {

        Response response = userAPI.getAllUsers();
        ApiAssertions.assertStatusCode(response, 200);
    }

    @Test //Positive scenario - happy flow
    public void getUser() {

        Response response = userAPI.getUser(1);
        ApiAssertions.assertStatusCode(response, 200);
        ApiAssertions.assertResponseFieldEquals(response, "data.email", "george.bluth@reqres.in");
        ApiAssertions.assertResponseFieldEquals(response, "data.first_name", "George");
        //Assertions using the Regex Helper methods
        Assert.assertTrue(RegexHelper.isValidEmail(response.jsonPath().getString("data.email")));
        Assert.assertTrue(RegexHelper.isValidUrl(response.jsonPath().getString("support.url")));
    }

    @Test //Negative scenario - nonexistent user
    public void getNonExistentUser() {
        
        Response response = userAPI.getUser(9999);
        ApiAssertions.assertStatusCode(response, 404);
        //The response is blank, so I will assert it with that in mind
        ApiAssertions.assertEmptyBody(response);
    }

    @Test //An example of deserializing the response and asserting
    public void getUserListValidation() {

        Response response = userAPI.getAllUsers();
        User userResponse = response.as(User.class);
        List<UserData> users = userResponse.getData();

        //Asserting that the list is not empty
        assertFalse(users.isEmpty());

        //Asserting that all users have required fields
        users.forEach(user -> {
            assertNotNull(user.getId());
            assertNotNull(user.getFirst_name());
            assertNotNull(user.getLast_name());
            assertNotNull(user.getEmail());
        });

        //Asserting that a specific user (with the id 1 and name George) exists in the list of all users
        boolean found = users.stream().anyMatch(u -> u.getId() == 1 && "George".equals(u.getFirst_name()));
        Assert.assertTrue(found);
    }

}