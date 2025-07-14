import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class MyRestAssured {
    Properties prop;
    public MyRestAssured() throws IOException {
        prop=new Properties();
        FileInputStream fs=new FileInputStream("./src/test/resources/config.properties");
        prop.load(fs);
    }
    @Test
    public void doLogin() throws ConfigurationException {
        RestAssured.baseURI=prop.getProperty("baseUrl");
        Response res= given().contentType("application/json")
                .body("{\n" +
                        "    \"email\":\"admin@roadtocareer.net\",\n" +
                        "    \"password\":\"1234\"\n" +
                        "}")
                .when().post("/user/login");
        System.out.println(res.asString());

        JsonPath jsonObj=res.jsonPath();
        String token= jsonObj.get("token");
        System.out.println(token);
        Utils.setEnv("token",token);
    }
    @Test
    public void searchUser() throws IOException {
        RestAssured.baseURI=prop.getProperty("baseUrl");
        Response res= given().contentType("application/json")
                .header("Authorization", "bearer "+ prop.getProperty("token"))
                .when().get("user/search/id/28590");
        System.out.println(res.asString());
    }
    @Test
    public void createUser() throws IOException {
        RestAssured.baseURI=prop.getProperty("baseUrl");
        Response res= given().contentType("application/json")
                .header("Authorization","bearer "+ prop.getProperty("token"))
                .body("{\n" +
                        "  \"name\": \"RA b15 user 2\",\n" +
                        "  \"email\": \"rab15user2@test.com\",\n" +
                        "  \"password\": \"1234\",\n" +
                        "  \"phone_number\": \"01504478660\",\n" +
                        "  \"nid\": \"123456789\",\n" +
                        "  \"role\": \"Customer\"\n" +
                        "}")
                .header("X-AUTH-SECRET-KEY",prop.getProperty("partnerKey"))
                .when().post("/user/create");
        System.out.println(res.asString());
    }


}
