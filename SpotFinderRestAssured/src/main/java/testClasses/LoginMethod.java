package testClasses;

import baseClasses.BaseApi;
import baseClasses.BaseMethod;
import io.restassured.response.Response;
import models.SettingModel;
import models.UserModel;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.lang.reflect.Field;

public class LoginMethod extends BaseMethod{

    UserModel NewUser;
    SettingModel settingModel;
    BaseApi baseApi = new BaseApi();
    String URL = getProperties("URL");
    String URI = getProperties("loginUser");
    String URIResetPassword = getProperties("resetPassword");
    String URIPatchUser = getProperties("updateUser");
    String URIPatchSettings = getProperties("changeUserSettings");
    String contentType = "application/json";

    @BeforeAll
    public static void setUp(){
        System.out.println("Logowanie");
        readPropertyFile();
    }
    @AfterAll
    public static void tearDown(){
        //exitReport();
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

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public String createUserJson(UserModel fakeUserModelData, boolean email){
        JSONObject json = new JSONObject();
        Field field = null;
        try {
            field = UserModel.class.getDeclaredField("username");
            String fieldName = field.getName();
            json.put(fieldName,fakeUserModelData.getUsername());

            field = UserModel.class.getDeclaredField("password");
            fieldName = field.getName();
            json.put(fieldName,fakeUserModelData.getPassword());
            if(email){
                field = UserModel.class.getDeclaredField("email");
                fieldName = field.getName();
                json.put(fieldName,fakeUserModelData.getEmail());
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public String createSettingModel(SettingModel settingModel){
        JSONObject json = new JSONObject();
        Field field = null;
        JSONObject j2 = new JSONObject();
        try {
            field = SettingModel.class.getDeclaredField("theme");
            String fieldName = field.getName();
            json.put(fieldName,settingModel.getTheme());

            field = SettingModel.class.getDeclaredField("language");
            fieldName = field.getName();
            json.put(fieldName,settingModel.getLanguage());
            String body = json.toString();
            String newBody;
            for (int i=0; i<body.length(); i++){
                if(body.charAt(i)=='"'){
                    newBody = body.substring(0,i+1)+'\\'+body.substring(i+1);
                    i=i+1;
                    body = newBody;
                }
            }
            j2.put("configuration", body);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return j2.toString();
    }

    public Response loginNormalUser(String fieldName, boolean active, boolean fakeUser){
        if(fieldName=="newUsername")
            NewUser = new UserModel(getProperties("nowaNazwaUzytkownika"), getProperties("password"),getProperties("email"));
        else if(fieldName=="newEmail")
            NewUser = new UserModel(getProperties("nowyEmail"), getProperties("password"),getProperties("email"));
        else if(fakeUser==true && fieldName=="username")
            NewUser = new UserModel(getProperties("usernameNieistniejacy"), getProperties("password"),getProperties("email"));
        else if(fakeUser==true && fieldName=="password")
            NewUser = new UserModel(getProperties("username"), getProperties("zlehaslo"),getProperties("email"));
        else{
            if(active==false)
                NewUser = new UserModel(getProperties("usernameNieaktywny"), getProperties("password"),getProperties("emailNieaktywny"));
            else{
                if (fieldName=="username")
                    NewUser = new UserModel(getProperties("username"), getProperties("password"),getProperties("email"));
                else
                    NewUser = new UserModel(getProperties("email"), getProperties("password"),getProperties("email"));
            }
        }
        return baseApi.postWithBody(URL, URI, createUserJson(NewUser),contentType);
    }

    public Response setUpPassword(boolean fake){
        if (!fake)
            return baseApi.getNewPassword(URL,URIResetPassword, getProperties("emailNieaktywny"));
        else return baseApi.getNewPassword(URL,URIResetPassword, getProperties("emailNieistniejacy"));
    }

    public Response updateData(String fieldName, String token){
        if(fieldName=="username")
            NewUser = new UserModel(getProperties("nowaNazwaUzytkownika"),getProperties("password"),getProperties("email"));
        else if(fieldName=="email")
            NewUser = new UserModel(getProperties("username"),getProperties("password"),getProperties("nowyEmail"));
        else
            NewUser = new UserModel(getProperties("username"),getProperties("password"),getProperties("email"));
        return baseApi.patchData(URL,URIPatchUser, createUserJson(NewUser,true),contentType, token);
    }

    public Response updateUserSettings(String field, String token){
        if(field=="theme")
            settingModel = new SettingModel(getProperties("themeLight"),getProperties("languagePl"));
        else if(field=="language")
            settingModel = new SettingModel(getProperties("themeDark"),getProperties("languageEn"));
        return baseApi.patchData(URL,URIPatchSettings,createSettingModel(settingModel),contentType,token);
    }
}
