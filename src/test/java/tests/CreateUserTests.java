package tests;

import api.UserAPI;
import io.restassured.response.Response;
import models.CreateUserBody;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ApiAssertions;

public class CreateUserTests {

    private UserAPI userAPI;

    @BeforeClass
    public void setup(){
        userAPI = new UserAPI();
    }


    @Test //Negative scenario - no access to API
    public void createUserNoAccess() {
        CreateUserBody userBody = new CreateUserBody("Jovana", "QA Engineer");

        Response response = userAPI.createUser(userBody, false);
        ApiAssertions.assertStatusCode(response, 401);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Missing API key.");
    }

    @Test //Positive scenario - the user is created with all the necessary information
    public void createUserHappyFlow() {
        CreateUserBody userBody = new CreateUserBody("Jovana", "QA Engineer");

        Response response = userAPI.createUser(userBody, true);
        ApiAssertions.assertStatusCode(response, 201);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseContainsField(response, "name");
        ApiAssertions.assertResponseContainsField(response, "job");
    }

    @Test //Positive scenario - the user is created without optional fields
    public void createUserNoOptionalFields() {
        CreateUserBody userBody = new CreateUserBody("Jovana", null);

        Response response = userAPI.createUser(userBody, true);
        ApiAssertions.assertStatusCode(response, 201);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "job", null);
    }

    @Test //Positive scenario - special characters in the name/job
    public void createUserSpecialChars() {
        String specialCharsName = "Jovana!@#$%^&*()_+=/><., ";
        String specialCharsJob = "QA Engineer!@#$%^&*()_+=/><., ";
        CreateUserBody userBody = new CreateUserBody(specialCharsName, specialCharsJob);

        Response response = userAPI.createUser(userBody, true);
        ApiAssertions.assertStatusCode(response, 201);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "name", specialCharsName);
        ApiAssertions.assertResponseFieldEquals(response, "job", specialCharsJob);
    }

    @Test //Positive scenario - name/job are very long
    public void createUserCharLong() {
        String longName = "Jovana has a long name Jovana has a long name Jovana has a long name Jovana has a long name Jovana .";
        String longJob = "Job has a long name Job has a long name Job has a long name Job has a long name Job has a long name ";
        CreateUserBody userBody = new CreateUserBody(longName, longJob);

        Response response = userAPI.createUser(userBody, true);
        ApiAssertions.assertStatusCode(response, 201);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "name", longName);
        ApiAssertions.assertResponseFieldEquals(response, "job", longJob);
    }

    @Test(enabled = false) //Negative scenario - missing required field (name)
    //Due to this being a mocked API this test fails - the user can indeed be created without a name
    public void createUserNoName() {
        CreateUserBody userBody = new CreateUserBody(null, "QA Engineer");

        Response response = userAPI.createUser(userBody, true);
        ApiAssertions.assertStatusCode(response, 400);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Name is required");
    }

    @Test(enabled = false) //Negative scenario - Name invalid data type
    //Due to this being a mocked API this test fails
    public void createUserInvalidName() {
        CreateUserBody userBody = new CreateUserBody("12345", "QA Engineer");

        Response response = userAPI.createUser(userBody, true);
        ApiAssertions.assertStatusCode(response, 400);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Invalid type for 'name'");
    }

    @Test(enabled = false) //Negative scenario - invalid additional fields
    //Due to this being a mocked API this test fails
    public void createUserInvalidExtraFields() {
        CreateUserBody userBody = new CreateUserBody("Jovana", "QA Engineer", "Invalid Field");

        Response response = userAPI.createUser(userBody, true);
        ApiAssertions.assertStatusCode(response, 400);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Invalid field for 'extraField'");
    }

    @Test(enabled = false) //Negative scenario - Name invalid data type
    //Due to this being a mocked API this test fails
    public void createUserEmptyName() {
        CreateUserBody userBody = new CreateUserBody("", null);

        Response response = userAPI.createUser(userBody, true);
        ApiAssertions.assertStatusCode(response, 400);
        ApiAssertions.assertResponseTimeLessThan(response, 2000);
        ApiAssertions.assertResponseFieldEquals(response, "error", "Name cannot be empty");
    }

}