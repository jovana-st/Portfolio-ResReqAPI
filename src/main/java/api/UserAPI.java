import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.CreateUserBody;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;

public class UserAPI extends BaseAPI{

    //Create a user - POST
    public Response createUser (Object requestBody, boolean hasAccess) {

        RequestSpecification spec = hasAccess ? baseSpec : baseSpecNoPermission;

        return given().spec(spec).body(requestBody)
                .when().post("/users")
                .then().log().all().extract().response();
    }


    //Get user details - GET
    public Response getUser (Integer userId) {
            return given().spec(baseSpec).pathParam("userId", userId)
                    .when().get("/users/{userId}")
                    .then().log().all().extract().response();
    }

    //Update an existing user - PUT
    public Response updateUser(Integer userId, CreateUserBody requestBody, boolean hasAccess){

        RequestSpecification spec = hasAccess ? baseSpec : baseSpecNoPermission;

        return given().spec(spec).body(requestBody)
                .pathParam("userId", userId)
                .when().put("/users/{userId}")
                .then().log().all().extract().response();
    }

    //Delete a user - DELETE
    public Response deleteUser(Integer userId, boolean hasAccess){

        RequestSpecification spec = hasAccess ? baseSpec : baseSpecNoPermission;

        return given().spec(spec)
                .pathParam("userId", userId)
                .when().delete("/users/{userId}")
                .then().log().all().extract().response();
    }



}
