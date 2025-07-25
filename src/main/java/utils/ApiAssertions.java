package utils;

import models.User;
import org.testng.Assert;
import io.restassured.response.Response;

import java.util.List;

public class ApiAssertions {

    //Assert that the status code is a specific value
    public static void assertStatusCode(Response response, int expectedStatusCode){
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode);
    }

    //Assert that the response contains a specific field
    public static void assertResponseContainsField(Response response, String fieldName){
        Assert.assertTrue(response.jsonPath().get(fieldName) != null);
    }

    //Assert that a field in the response equals a specific value
    public static void assertResponseFieldEquals(Response response, String fieldName, String expectedValue){
        Assert.assertEquals(response.jsonPath().get(fieldName), expectedValue);
    }

    //Assert that the response time is less than a specific amount
    public static void assertResponseTimeLessThan(Response response, long maxTime){
        long responseTime = response.getTime();
        Assert.assertTrue(responseTime < maxTime);
    }

    //Assert that the response body is empty
    public static void assertEmptyBody(Response response){
        Assert.assertTrue(response.getBody().asString().isEmpty());
    }

}