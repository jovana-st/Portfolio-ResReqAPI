
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseAPI {

    //Setting up general/common request specification
    static final String BASE_URL = "https://reqres.in/api";
    static final String XAPI_KEY = "reqres-free-v1";

    public RequestSpecification baseSpec = new RequestSpecBuilder().setBaseUri(BASE_URL).addHeader("x-api-key",XAPI_KEY)
            .setContentType("application/json").build();

    public RequestSpecification baseSpecNoPermission = new RequestSpecBuilder().setBaseUri(BASE_URL)
            .setContentType("application/json").build();

}
