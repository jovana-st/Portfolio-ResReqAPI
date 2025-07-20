import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GetUserTests {

    private UserAPI userAPI;

    @BeforeClass
    public void setup(){
        userAPI = new UserAPI();
    }

    @Test //Positive scenario - happy flow
    public void getUser() {

        Response response = userAPI.getUser(1);
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("data.email"), "george.bluth@reqres.in");
        Assert.assertEquals(response.jsonPath().getString("data.first_name"), "George");
    }

    @Test //Negative scenario - nonexistent user
    public void getNonExistentUser() {
        
        Response response = userAPI.getUser(9999);
        Assert.assertEquals(response.statusCode(), 404);
        //The response is blank, so no error message can be asserted, but if id could, I would do it something like:
        //Assert.assertEquals(response.jsonPath().getString("error"), "User does not exist.");
    }

}