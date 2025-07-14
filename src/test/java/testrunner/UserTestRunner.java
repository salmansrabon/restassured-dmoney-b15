package testrunner;

import com.github.javafaker.Faker;
import config.Setup;
import config.UserModel;
import controller.UserController;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Utils;

public class UserTestRunner extends Setup {
    @Test(priority = 1, description = "User login")
    public void doLogin() throws ConfigurationException {
        UserController userController=new UserController(prop);
        UserModel userModel=new UserModel();
        userModel.setEmail("admin@roadtocareer.net");
        userModel.setPassword("1234");
        Response res= userController.doLogin(userModel);
        JsonPath jsonObj=res.jsonPath();
        String token=jsonObj.get("token");
        Utils.setEnv("token",token);
    }
    @Test(priority = 2, description = "Create new user")
    public void createUser() throws ConfigurationException {
        UserController userController=new UserController(prop);
        UserModel userModel=new UserModel();
        Faker faker=new Faker();
        userModel.setName(faker.name().fullName());
        userModel.setEmail(faker.internet().emailAddress());
        userModel.setPassword("1234");
        String phoneNumber="0130"+Utils.generateRandomId(1000000,9999999);
        userModel.setPhone_number(phoneNumber);
        userModel.setNid("123456789");
        userModel.setRole("Customer");
        Response res= userController.createUser(userModel);
        System.out.println(res.asString());
        JsonPath jsonObject=res.jsonPath();
        int userId= jsonObject.get("user.id");
        System.out.println(userId);
        Utils.setEnv("userId",String.valueOf(userId));
    }
    @Test(priority = 3, description = "Search user")
    public void searchUser(){
        UserController userController=new UserController(prop);
        Response res= userController.searchUser(prop.getProperty("userId"));
        System.out.println(res.asString());

        JsonPath jsonPath=res.jsonPath();
        String messageActual= jsonPath.get("message");
        String messageExpected="User found";
        Assert.assertEquals(messageActual,messageExpected);
    }
    @Test(priority = 4, description = "User delete")
    public void deleteUser(){
        UserController userController=new UserController(prop);
        System.out.println(prop.getProperty("userId"));
        Response res= userController.deleteUser(prop.getProperty("userId"));
        System.out.println(res.asString());
    }
}
