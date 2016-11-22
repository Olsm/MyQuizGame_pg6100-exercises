package org.pg6100.restApi;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.pg6100.restApi.dto.QuizDTO;
import org.pg6100.restApi.dto.RootCategoryDTO;
import org.pg6100.restApi.dto.SubCategoryDTO;
import org.pg6100.restApi.dto.SubSubCategoryDTO;
import org.pg6100.utils.web.JBossUtil;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class QuizRestTestBase {

    @BeforeClass
    public static void initClass() {
        JBossUtil.waitForJBoss(10);

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/quizrest/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    @Before
    @After
    public void clean() {

        /*
           Recall, as Wildfly is running as a separated process, changed
           in the database will impact all the tests.
           Here, we read each resource (GET), and then delete them
           one by one (DELETE)
         */
        List<QuizDTO> list1 = Arrays.asList(given().accept(ContentType.JSON).get("/quiz")
                .then()
                .statusCode(200)
                .extract().as(QuizDTO[].class));


        /*
            Code 204: "No Content". The server has successfully processed the request,
            but the return HTTP response will have no body.
         */
        list1.stream().forEach(dto ->
                given().pathParam("id", dto.id).delete("/id/{id}").then().statusCode(204));

        get("/quiz").then().statusCode(200).body("size()", is(0));

        List<RootCategoryDTO> list2 = Arrays.asList(given().accept(ContentType.JSON).get("/categories")
                .then()
                .statusCode(200)
                .extract().as(RootCategoryDTO[].class));

        list2.stream().forEach(dto ->
                given().pathParam("id", dto.name).delete("/categories/id/{id}").then().statusCode(204));

        get("/categories").then().statusCode(200).body("size()", is(0));

        List<SubCategoryDTO> list3 = Arrays.asList(given().accept(ContentType.JSON).get("/subcategories")
                .then()
                .statusCode(200)
                .extract().as(SubCategoryDTO[].class));

        list3.stream().forEach(dto ->
                given().pathParam("id", dto.name).delete("/subcategories/id/{id}").then().statusCode(204));

        get("/subcategories").then().statusCode(200).body("size()", is(0));

        List<SubSubCategoryDTO> list4 = Arrays.asList(given().accept(ContentType.JSON).get("/subsubcategories")
                .then()
                .statusCode(200)
                .extract().as(SubSubCategoryDTO[].class));

        list4.stream().forEach(dto ->
                given().pathParam("id", dto.name).delete("/subsubcategories/id/{id}").then().statusCode(204));

        get("/subsubcategories").then().statusCode(200).body("size()", is(0));
    }
}
