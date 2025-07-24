import io.restassured.response.Response;
import models.CreateUserBody;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("error"), "Missing API key.");
    }

    @Test //Positive scenario - the user is updated with all the necessary information
    public void updateUserHappyFlow() {
        CreateUserBody userBody = new CreateUserBody("Jovana Updated", "QA Engineer Updated");

        Response response = userAPI.updateUser(1, userBody, true);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test //Positive scenario - the user is updated without optional fields
    public void updateUserNoOptionalFields() {
        CreateUserBody userBody = new CreateUserBody("Jovana Updated", null);

        Response response = userAPI.updateUser(1, userBody, true);
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("job"), null);
        Assert.assertEquals(response.jsonPath().getString("name"), "Jovana Updated");
    }

    @Test //Positive scenario - special characters in the name/job
    public void updateUserSpecialChars() {
        CreateUserBody userBody = new CreateUserBody("Jovana!@#$%^&*()_+=/><., ", "QA Engineer!@#$%^&*()_+=/><., ");

        Response response = userAPI.updateUser(1, userBody, true);
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("name"), "Jovana!@#$%^&*()_+=/><., ");
        Assert.assertEquals(response.jsonPath().getString("job"), "QA Engineer!@#$%^&*()_+=/><., ");
    }

    @Test //Positive scenario - name/job are very long
    public void updateUserCharLong() {
        CreateUserBody userBody = new CreateUserBody("Jovana has a long name Jovana has a long name Jovana has a long name Jovana has a long name Jovana .",
                "Job has a long name Job has a long name Job has a long name Job has a long name Job has a long name ");

        Response response = userAPI.updateUser(1, userBody, true);
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("name"),
                "Jovana has a long name Jovana has a long name Jovana has a long name Jovana has a long name Jovana .");
        Assert.assertEquals(response.jsonPath().getString("job"),
                "Job has a long name Job has a long name Job has a long name Job has a long name Job has a long name ");
    }

    @Test //Negative scenario - missing required field (name)
    //Due to this being a mocked API this test fails - the user can indeed be created without a name
    public void updateUserNoName() {
        CreateUserBody userBody = new CreateUserBody(null, "QA Engineer");

        Response response = userAPI.updateUser(1, userBody, true);
        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("error"), "Name is required");
    }

    @Test //Negative scenario - Name invalid data type
    //Due to this being a mocked API this test fails
    public void updateUserInvalidName() {
        CreateUserBody userBody = new CreateUserBody("12345", "QA Engineer");

        Response response = userAPI.updateUser(1, userBody, true);
        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("error"), "Invalid type for 'name'");
    }

    @Test //Negative scenario - invalid additional fields
    //Due to this being a mocked API this test fails
    public void updateUserInvalidExtraFields() {
        CreateUserBody userBody = new CreateUserBody("Jovana", "QA Engineer", "Invalid Field");

        Response response = userAPI.updateUser(1, userBody, true);
        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("error"), "Invalid field for 'extraField'");
    }

    @Test //Negative scenario - Name invalid data type
    //Due to this being a mocked API this test fails
    public void updateUserEmptyName() {
        CreateUserBody userBody = new CreateUserBody("", null);

        Response response = userAPI.updateUser(1, userBody, true);
        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("error"), "Name cannot be empty");
    }

    @Test //Negative scenario - Missing User with the specified ID
    //Due to this being a mocked API this test fails
    public void updateUserNoUserId() {
        CreateUserBody userBody = new CreateUserBody("", null);

        Response response = userAPI.updateUser(99999999, userBody, true);
        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("error"), "The user does not exist.");
    }

}
