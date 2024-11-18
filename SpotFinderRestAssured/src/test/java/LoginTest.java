import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import testClasses.LoginMethod;

import static org.hamcrest.Matchers.equalTo;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest extends LoginMethod {

    String category = "Logowanie_użytkownika";
    public String getTokenByLogin(ExtentTest test){
        ExtentTest node = test.createNode("Pobranie tokenu");
        Response loginUser = loginNormalUser("username",true,false);
        String token = "Bearer "+getValueFromReponse(loginUser.getBody(),"token");
        fillReport(loginUser,200,node,category);
        loginUser.then()
                .statusCode(200)
                .assertThat()
                .body("user.username", equalTo(getProperties("username")));
        return token;
    }

    @Test
    @Order(1)
    @DisplayName("USER.PT.004 Logowanie użytkownika - przypadek pozytywny")
    public void LoginTest(){
        ExtentTest test = extentReports.createTest("USER.PT.004 Logowanie użytkownika - przypadek pozytywny");
        try{
            Response loginNormalUser = loginNormalUser("username", true,false);
            ExtentTest node = test.createNode("Logowanie za pomocą username");
            fillReport(loginNormalUser,200, node,category);
            loginNormalUser.then()
                    .statusCode(200)
                    .assertThat()
                    .body("user.username", equalTo(getProperties("username")))
                    .body("user.isAdmin", equalTo(false));

            loginNormalUser = loginNormalUser("email", true,false);
            node = test.createNode("Logowanie za pomocą email");
            fillReport(loginNormalUser,200, node,category);
            loginNormalUser.then()
                    .statusCode(200)
                    .assertThat()
                    .body("user.username", equalTo(getProperties("username")))
                    .body("user.isAdmin", equalTo(false));
        }catch (Exception err){
            fillReport(test,err,category);
        }
    }

    @Test
    @Order(2)
    @DisplayName("USER.PT.005 Logowanie użytkownika - przypadek negatywny - nieaktywne konto")
    public void LoginInactiveUserTest(){
        ExtentTest test = extentReports.createTest("USER.PT.005 Logowanie użytkownika - przypadek negatywny - nieaktywne konto");
        try {
            Response loginNormalUser = loginNormalUser("username", false,false);
            fillReport(loginNormalUser,401,test,category);
            loginNormalUser.then()
                    .statusCode(401)
                    .assertThat()
                    .body("error.message", equalTo("Invalid credentials"));
        }catch (Exception err){
            fillReport(test,err,category);
        }
    }

    @Test
    @Order(3)
    @DisplayName("USER.PT.006 Logowanie użytkownika - przypadek negatywny - błędne dane")
    public void LoginUserFakeDataTest(){
        ExtentTest test = extentReports.createTest("USER.PT.006 Logowanie użytkownika - przypadek negatywny - błędne dane");
        try {
            ExtentTest node = test.createNode("Logowanie za pomocą nieprawidłowej nazwy użytkownika");
            Response loginNormalUser = loginNormalUser("username", true,true);
            fillReport(loginNormalUser,401,node,category);
            loginNormalUser.then()
                    .statusCode(401)
                    .assertThat()
                    .body("error.message", equalTo("Invalid credentials"));

            node = test.createNode("Logowanie za pomocą nieprawidłowego hasła");
            loginNormalUser = loginNormalUser("password", true,true);
            fillReport(loginNormalUser,401,node,category);
            loginNormalUser.then()
                    .statusCode(401)
                    .assertThat()
                    .body("error.message", equalTo("Invalid credentials"));
        }catch (Exception err){
            fillReport(test,err,category);
        }
    }

    @Test
    @Order(4)
    @DisplayName("USER.PT.007 Reset hasła - przypadek pozytywny")
    public void setUpNewPasswordTest(){
        ExtentTest test = extentReports.createTest("USER.PT.007 Reset hasła - przypadek pozytywny");
        try {
            Response newPassword = setUpPassword(false);
            fillReport(newPassword,200,test,category);
            newPassword.then()
                    .statusCode(200)
                    .assertThat()
                    .body("message", equalTo("Email sended"));
        }catch (Exception err){
            fillReport(test,err,category);
        }
    }

    @Test
    @Order(5)
    @DisplayName("USER.PT.008 Reset hasła - przypadek negatywny - nieistniejący email")
    public void setUpNewPasswordBadDataTest(){
        ExtentTest test = extentReports.createTest("USER.PT.008 Reset hasła - przypadek negatywny - nieistniejący email");
        try {
            Response newPassword = setUpPassword(true);
            fillReport(newPassword,400,test,category);
            newPassword.then()
                    .statusCode(400)
                    .assertThat()
                    .body("error.message", equalTo("Bad Request"));
        }catch (Exception err){
            fillReport(test,err,category);
        }
    }

    @Test
    @Order(6)
    @DisplayName("USER.PT.011 Zmiana danych użytkownika - przypadek pozytywny")
    public void changeUserDataTest(){
        category = "Zmiana_danych_i_ustawień_użytkownika";
        ExtentTest test = extentReports.createTest("USER.PT.011 Zmiana danych użytkownika - przypadek pozytywny");
        try {
            //Pobranie tokenu v
            String token = getTokenByLogin(test);
            //////////

            //Zmiana username i logowanie za pomocą nowego username
            Response newData = updateData("username",token);
            ExtentTest node = test.createNode("Zmiana nazwy użytkownika");
            fillReport(newData,200,node,category);
            newData.then()
                    .statusCode(200)
                    .assertThat()
                    .body("username", equalTo(getProperties("nowaNazwaUzytkownika")))
                    .body("email", equalTo(getProperties("email")));

            Response loginUser = loginNormalUser("newUsername",true,false);
            node = test.createNode("Logowanie za pomoca nowej nazwy użytkownika");
            fillReport(loginUser,200,node,category);
            loginUser.then()
                    .statusCode(200)
                    .assertThat()
                    .body("user.username", equalTo(getProperties("nowaNazwaUzytkownika")));
            token = "Bearer "+getValueFromReponse(loginUser.getBody(),"token");
            /////////

            //zmiana email i logowanie za pomocą nowego email
            newData = updateData("email",token);
            node = test.createNode("Zmiana email");
            fillReport(newData,200,node,category);
            newData.then()
                    .statusCode(200)
                    .assertThat()
                    .body("username", equalTo(getProperties("username")))
                    .body("email", equalTo(getProperties("nowyEmail")));

            loginUser = loginNormalUser("username",true,false);
            node = test.createNode("Logowanie za pomoca nowego email");
            fillReport(loginUser,200,node,category);
            loginUser.then()
                    .statusCode(200)
                    .assertThat()
                    .body("user.email", equalTo(getProperties("nowyEmail")));
            ////////////

            //powrót do starych danych
            newData = updateData("",token);
            node = test.createNode("Zmiana na dane bazowe");
            fillReport(newData,200,node,category);
            newData.then()
                    .statusCode(200)
                    .assertThat()
                    .body("username", equalTo(getProperties("username")))
                    .body("email", equalTo(getProperties("email")));

        }catch (Exception err){
            fillReport(test,err,category);
        }
    }

    @Test
    @Order(7)
    @DisplayName("USER.PT.012 Zmiana ustawień użytkownika - przypadek pozytywny")
    public void  changeUserSettingsTest(){
        ExtentTest test = extentReports.createTest("USER.PT.012 Zmiana ustawień użytkownika - przypadek pozytywny");
        //Pobranie tokenu
        try {
            String token = getTokenByLogin(test);
            Response newSetting = updateUserSettings("theme",token);
            ExtentTest node = test.createNode("Zmiana stylu strony na jasny");
            fillReport(newSetting,200,node,category);
            newSetting.then()
                    .statusCode(200)
                    .assertThat()
                    .body("message",equalTo("Configuration saved"));

            newSetting = updateUserSettings("language",token);
            node = test.createNode("Zmiana języka na angielski");
            fillReport(newSetting,200,node,category);
            newSetting.then()
                    .statusCode(200)
                    .assertThat()
                    .body("message",equalTo("Configuration saved"));
        }catch (Exception err){
            fillReport(test,err,category);
        }
    }
}
