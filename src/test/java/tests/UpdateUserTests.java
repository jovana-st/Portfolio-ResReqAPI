package tests;

import api.UserAPI;
import io.restassured.response.Response;
import models.CreateUserBody;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ApiAssertions;

public class UpdateUserTests {

    private UserAPI userAPI;

    @BeforeClass
    public void setup(){
        userAPI = new UserAPI();
    }

    @Test //Negative scenario - no access to API
    public void updateUserNoAccess() {
        CreateUserBody userBody = new CreateUserBody("Jovana Updated", "QA Engineer Updated");

        Response response = userAPI.updateUser(1, userBody, false);
        ApiAssertions.assertStatusCode(response, 401);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Missing API key.");
    }

    @Test //Positive scenario - the user is updated with all the necessary information
    public void updateUserHappyFlow() {
        CreateUserBody userBody = new CreateUserBody("Jovana Updated", "QA Engineer Updated");

        Response response = userAPI.updateUser(1, userBody, true);
        ApiAssertions.assertStatusCode(response, 200);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
    }

    @Test //Positive scenario - the user is updated without optional fields
    public void updateUserNoOptionalFields() {
        CreateUserBody userBody = new CreateUserBody("Jovana Updated", null);

        Response response = userAPI.updateUser(1, userBody, true);
        ApiAssertions.assertStatusCode(response, 200);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "job", null);
        ApiAssertions.assertResponseFieldEquals(response, "name", "Jovana Updated");
    }

    @Test //Positive scenario - special characters in the name/job
    public void updateUserSpecialChars() {
        String specialCharsName = "Jovana!@#$%^&*()_+=/><., ";
        String specialCharsJob = "QA Engineer!@#$%^&*()_+=/><., ";
        CreateUserBody userBody = new CreateUserBody(specialCharsName, specialCharsJob);

        Response response = userAPI.updateUser(1, userBody, true);
        ApiAssertions.assertStatusCode(response, 200);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "name", specialCharsName);
        ApiAssertions.assertResponseFieldEquals(response, "job", specialCharsJob);
    }

    @Test //Positive scenario - name/job are very long
    public void updateUserCharLong() {
        String longName = "Jovana has a long name Jovana has a long name Jovana has a long name Jovana has a long name Jovana .";
        String longJob = "Job has a long name Job has a long name Job has a long name Job has a long name Job has a long name ";
        CreateUserBody userBody = new CreateUserBody(longName,longJob);

        Response response = userAPI.updateUser(1, userBody, true);
        ApiAssertions.assertStatusCode(response, 200);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "name", longName);
        ApiAssertions.assertResponseFieldEquals(response, "job", longJob);
    }

    @Test(enabled = false) //Negative scenario - missing required field (name)
    //Due to this being a mocked API this test fails - the user can indeed be created without a name
    public void updateUserNoName() {
        CreateUserBody userBody = new CreateUserBody(null, "QA Engineer");

        Response response = userAPI.updateUser(1, userBody, true);
        ApiAssertions.assertStatusCode(response, 400);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Name is required");
    }

    @Test(enabled = false) //Negative scenario - Name invalid data type
    //Due to this being a mocked API this test fails
    public void updateUserInvalidName() {
        CreateUserBody userBody = new CreateUserBody("12345", "QA Engineer");

        Response response = userAPI.updateUser(1, userBody, true);
        ApiAssertions.assertStatusCode(response, 400);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Invalid type for 'name'");
    }

    @Test(enabled = false) //Negative scenario - invalid additional fields
    //Due to this being a mocked API this test fails
    public void updateUserInvalidExtraFields() {
        CreateUserBody userBody = new CreateUserBody("Jovana", "QA Engineer", "Invalid Field");

        Response response = userAPI.updateUser(1, userBody, true);
        ApiAssertions.assertStatusCode(response, 400);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Invalid field for 'extraField'");
    }

    @Test(enabled = false) //Negative scenario - Name invalid data type
    //Due to this being a mocked API this test fails
    public void updateUserEmptyName() {
        CreateUserBody userBody = new CreateUserBody("", null);

        Response response = userAPI.updateUser(1, userBody, true);
        ApiAssertions.assertStatusCode(response, 400);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Name is required");
    }

    @Test(enabled = false) //Negative scenario - Missing User with the specified ID
    //Due to this being a mocked API this test fails
    public void updateUserNoUserId() {
        CreateUserBody userBody = new CreateUserBody("", null);

        Response response = userAPI.updateUser(99999999, userBody, true);
        ApiAssertions.assertStatusCode(response, 400);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "The user does not exist.");
    }

}