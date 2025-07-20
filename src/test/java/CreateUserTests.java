import io.restassured.response.Response;
import models.CreateUserBody;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("error"), "Missing API key.");
    }

    @Test //Positive scenario - the user is created with all the necessary information
    public void createUserHappyFlow() {
        CreateUserBody userBody = new CreateUserBody("Jovana", "QA Engineer");

        Response response = userAPI.createUser(userBody, true);
        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test //Positive scenario - the user is created without optional fields
    public void createUserNoOptionalFields() {
        CreateUserBody userBody = new CreateUserBody("Jovana", null);

        Response response = userAPI.createUser(userBody, true);
        Assert.assertEquals(response.statusCode(), 201);
        Assert.assertEquals(response.jsonPath().getString("job"), null);
    }

    @Test //Positive scenario - special characters in the name/job
    public void createUserSpecialChars() {
        CreateUserBody userBody = new CreateUserBody("Jovana!@#$%^&*()_+=/><., ", "QA Engineer!@#$%^&*()_+=/><., ");

        Response response = userAPI.createUser(userBody, true);
        Assert.assertEquals(201, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("name"), "Jovana!@#$%^&*()_+=/><., ");
        Assert.assertEquals(response.jsonPath().getString("job"), "QA Engineer!@#$%^&*()_+=/><., ");
    }

    @Test //Positive scenario - name/job are very long
    public void createUserCharLong() {
        CreateUserBody userBody = new CreateUserBody("Jovana has a long name Jovana has a long name Jovana has a long name Jovana has a long name Jovana .",
                "Job has a long name Job has a long name Job has a long name Job has a long name Job has a long name ");

        Response response = userAPI.createUser(userBody, true);
        Assert.assertEquals(201, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("name"),
                "Jovana has a long name Jovana has a long name Jovana has a long name Jovana has a long name Jovana .");
        Assert.assertEquals(response.jsonPath().getString("job"),
                "Job has a long name Job has a long name Job has a long name Job has a long name Job has a long name ");
    }

    @Test //Negative scenario - missing required field (name)
    //Due to this being a mocked API this test fails - the user can indeed be created without a name
    public void createUserNoName() {
        CreateUserBody userBody = new CreateUserBody(null, "QA Engineer");

        Response response = userAPI.createUser(userBody, true);
        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("error"), "Name is required");
    }

    @Test //Negative scenario - Name invalid data type
    //Due to this being a mocked API this test fails
    public void createUserInvalidName() {
        CreateUserBody userBody = new CreateUserBody("12345", "QA Engineer");

        Response response = userAPI.createUser(userBody, true);
        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("error"), "Invalid type for 'name'");
    }

    @Test //Negative scenario - invalid additional fields
    //Due to this being a mocked API this test fails
    public void createUserInvalidExtraFields() {
        CreateUserBody userBody = new CreateUserBody("Jovana", "QA Engineer", "Invalid Field");

        Response response = userAPI.createUser(userBody, true);
        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("error"), "Invalid field for 'extraField'");
    }

    @Test //Negative scenario - Name invalid data type
    //Due to this being a mocked API this test fails
    public void createUserEmptyName() {
        CreateUserBody userBody = new CreateUserBody("", null);

        Response response = userAPI.createUser(userBody, true);
        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("error"), "Name cannot be empty");
    }

}