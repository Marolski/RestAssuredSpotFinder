import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import testClasses.RegisterMethod;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterTest extends RegisterMethod {

    String category = "Rejestracja_użytkownika";

    @Test
    @Order(1)
    @DisplayName("USER.PT.001 Rejestracja użytkownika - przypadek pozytywny")
    public void addNewUserTest(){
        ExtentTest test = extentReports.createTest("USER.PT.001 Rejestracja użytkownika - przypadek pozytywny");
        try {
            Response registerNewUser = registerNewUser();
            fillReport(registerNewUser,201,test,category);
            registerNewUser.then().
                    statusCode(201).
                    assertThat().body("message",equalTo("User created"));
        }catch (Exception err) {
            fillReport(test,err,category);
        }
    }
    @Test
    @Order(2)
    @DisplayName("USER.PT.002 Rejestracja użytkownika - przypadek negatywny - użytkownik o podanych danych już istnieje")
    public void addUserWithExistingParamTest(){
        ExtentTest test = extentReports.createTest("USER.PT.002 Rejestracja użytkownika - przypadek negatywny - użytkownik o podanych danych już istnieje");
        try {
            Response registerNewUser = registerExistingUser();
            fillReport(registerNewUser,409,test,category);
            registerNewUser.then().
                    statusCode(409).
                    assertThat().body("error.message",equalTo("User exist"));
        }catch (Exception err) {
            fillReport(test,err,category);
        }
    }
    @Test
    @Order(3)
    @DisplayName("USER.PT.003 Rejestracja użytkownika - przypadek negatywny - błędne dane")
    public void addUserWithBadParamTest(){
        ExtentTest test = extentReports.createTest("USER.PT.003 Rejestracja użytkownika - przypadek negatywny - błędne dane");
        try {
            Response registerNewUser = registerUserWithEmptyParams("EmailFormat");
            ExtentTest node = test.createNode("Błędny format email");
            fillReport(registerNewUser,400,node,category);
            registerNewUser.then().
                    statusCode(400).
                    assertThat().body("errors[0].param", containsString("email"));

            registerNewUser = registerUserWithEmptyParams("EmptyEmail");
            ExtentTest node2 = test.createNode("Brak adresu email");
            fillReport(registerNewUser,400,node2,category);
            registerNewUser.then().
                    statusCode(400).
                    assertThat().body("errors[0].param",equalTo("email"));

            registerNewUser = registerUserWithEmptyParams("EmptyPassword");
            ExtentTest node3 = test.createNode("Brak hasła");
            fillReport(registerNewUser,400,node3,category);
            registerNewUser.then().
                    statusCode(400).
                    assertThat().body("errors[0].param",equalTo("password"));

            registerNewUser = registerUserWithEmptyParams("EmptyUsername");
            ExtentTest node4 = test.createNode("Brak nazwy użytkownika");
            fillReport(registerNewUser,400,node4,category);
            registerNewUser.then().
                    statusCode(400).
                    assertThat().body("errors[0].param",equalTo("username"));
        }catch (Exception err){
            fillReport(test,err,category);
        }
    }
}
