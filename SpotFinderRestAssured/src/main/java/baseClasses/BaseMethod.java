package baseClasses;

import io.restassured.response.ResponseBody;
import models.UserModel;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class BaseMethod extends BaseReport{

    final static String propertyFileName = "config.properties";
    static Properties props = new Properties();
    BaseApi baseApi = new BaseApi();
    String URL = getProperties("URL");
    String contentType = "application/json";

    public String getValueFromReponse(ResponseBody responseBody, String value){
        String rp = responseBody.prettyPrint();
        JSONObject json = new JSONObject(rp);
        return json.getString(value);
    }

    public String getToken() throws NoSuchFieldException {

        String login = getProperties("email");
        String password = getProperties("password");
        //generate body to request
        JSONObject json = new JSONObject();
        Field field = UserModel.class.getDeclaredField("username");
        String fieldName = field.getName();
        json.put(fieldName,login);

        field = UserModel.class.getDeclaredField("password");
        fieldName = field.getName();
        json.put(fieldName,password);

        //call request method
        String token = getValueFromReponse
                (baseApi.postWithBody(URL,
                getProperties("loginUser"),
                json.toString(),contentType).getBody(),
                "token");
        return token;
    }

    //read properties from config.properties
    public static void readPropertyFile() {
        try{
            createReport();
            FileInputStream in = new FileInputStream("src\\main\\resources\\" + propertyFileName);
            props.load(in);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getProperties(String propName){
        return props.getProperty(propName);
    }

}
