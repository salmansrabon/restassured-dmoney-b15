package controller;

import config.UserModel;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;

import java.util.Properties;

import static io.restassured.RestAssured.given;

public class UserController {
    Properties prop;
    public UserController(Properties prop){
        this.prop=prop;
    }
    public Response doLogin(UserModel userModel) throws ConfigurationException {
        RestAssured.baseURI=prop.getProperty("baseUrl");
        Response res= given().contentType("application/json")
                .body(userModel)
                .when().post("/user/login");
        return res;

    }
    public Response createUser(UserModel userModel){
        RestAssured.baseURI=prop.getProperty("baseUrl");
        return given().contentType("application/json")
                .header("Authorization","bearer "+ prop.getProperty("token"))
                .body(userModel)
                .header("X-AUTH-SECRET-KEY",prop.getProperty("partnerKey"))
                .when().post("/user/create");
    }
    public Response searchUser(String userId){
        RestAssured.baseURI=prop.getProperty("baseUrl");
        return given().contentType("application/json")
                .header("Authorization", "bearer "+ prop.getProperty("token"))
                .when().get("user/search/id/"+userId);
    }
    public Response deleteUser(String userId){
        RestAssured.baseURI=prop.getProperty("baseUrl");
        return given().contentType("application/json")
                .header("Authorization", "bearer "+ prop.getProperty("token"))
                .header("X-AUTH-SECRET-KEY",prop.getProperty("partnerKey"))
                .when().delete("user/delete/"+userId);

    }
}
