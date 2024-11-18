package baseClasses;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.response.Response;

public class BaseReport{

    public static ExtentReports extentReports = new ExtentReports();
    public static void createReport(){
        ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark.html");
        extentReports.attachReporter(spark);
    }
    public static void exitReport(){System.out.println("koncze raport---------------"); extentReports.flush();}
    public void fillReport(Response response, int statusCode, ExtentTest node, String category){
        if(response.statusCode()==statusCode){
            node.pass("Test zakończony powodzeniem");
            node.pass("Otrzymano kod statusu: "+response.statusCode());
            node.pass("Otrzymana odpowiedź: "+response.body().prettyPrint());
        }
        else {
            node.fail("Test zakończony błędem");
            node.fail("Oczekiwany kod statusu: "+statusCode);
            node.fail("Otrzymano kod statusu: "+response.statusCode());
            node.fail("Otrzymana odpowiedź: "+response.body().prettyPrint());
        }
        node.assignCategory(category);
    }
    public void fillReport(ExtentTest node, Exception error, String category){
        node.assignCategory(category);
        node.fail(error);
        node.log(Status.FAIL, error);
    }
}
