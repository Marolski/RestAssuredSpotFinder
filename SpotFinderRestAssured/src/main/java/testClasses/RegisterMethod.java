package testClasses;

import baseClasses.BaseApi;
import baseClasses.BaseMethod;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import models.UserModel;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.lang.reflect.Field;
import java.util.Locale;

public class RegisterMethod extends BaseMethod{

    UserModel NewUser;
    Faker faker = new Faker(new Locale("pl"));
    BaseApi baseApi = new BaseApi();
    String URL = getProperties("URL");
    String URI = getProperties("addUser");
    String contentType = "application/json";

    @BeforeAll
    public static void setUp(){
        System.out.println("rejestracja");
        readPropertyFile();
    }
    @AfterAll
    public static void tearDown(){
        exitReport();
    }

    public String createUserJson(UserModel fakeUserModelData){
        JSONObject json = new JSONObject();
        Field field = null;
        try {
            field = UserModel.class.getDeclaredField("username");
            String fieldName = field.getName();
            json.put(fieldName,fakeUserModelData.getUsername());

            field = UserModel.class.getDeclaredField("password");
            fieldName = field.getName();
            json.put(fieldName,fakeUserModelData.getPassword());

            field = UserModel.class.getDeclaredField("email");
            fieldName = field.getName();
            json.put(fieldName,fakeUserModelData.getEmail());

            field = UserModel.class.getDeclaredField("confirmed");
            fieldName = field.getName();
            json.put(fieldName,fakeUserModelData.getConfirmed());

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public Response registerNewUser(){
        NewUser = new UserModel(faker.name().username(),getProperties("password"),faker.internet().emailAddress(faker.name().firstName()), true);
        return baseApi.postWithBody(URL,URI, createUserJson(NewUser),contentType);
    }

    public Response registerExistingUser(){
        NewUser = new UserModel(faker.name().username(),getProperties("password"),getProperties("email"), true);
        return baseApi.postWithBody(URL,URI, createUserJson(NewUser),contentType);
    }

    public Response registerUserWithEmptyParams(String emptyParam){
        switch (emptyParam){
            case "EmailFormat":
                NewUser = new UserModel(faker.name().username(),getProperties("password"),faker.internet().domainName(), true);
                break;
            case "EmptyEmail":
                NewUser = new UserModel(faker.name().username(),getProperties("password"),"null", true);
                break;
            case "EmptyPassword":
                NewUser = new UserModel(faker.name().username(),"null",faker.internet().emailAddress(faker.name().firstName()), true);
                NewUser.setPassword("");
                break;
            case "EmptyUsername":
                NewUser = new UserModel("null",getProperties("password"),faker.internet().emailAddress(faker.name().firstName()), true);
                NewUser.setUsername("");
                break;
        }
        return baseApi.postWithBody(URL,URI, createUserJson(NewUser),contentType);
    }
}
