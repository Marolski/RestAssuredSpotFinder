package baseClasses;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BaseApi {
    String ctType = "application/json";

    public Response postWithBody(String url, String uri, String json, String token, String contentType){
        return
                given().header("authorization", token).
                        contentType(contentType).
                        body(json).
                        post(url+uri);
    }
    public Response postWithBody(String url, String uri, String json, String contentType){
        return
                given().contentType(contentType).
                        body(json).
                        post(url+uri);
    }
    public Response getNewPassword(String url, String uri, String value){
        return
                given()
                        .contentType(ctType)
                        .get(url+uri+value);
    }
    public Response patchData(String url, String uri, String json, String contentType, String token){
        return
                given()
                        .headers("Authorization",token)
                        .contentType(contentType)
                        .body(json)
                        .patch(url+uri);
    }
}
