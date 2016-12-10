package org.pg6100.QuizAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.pg6100.QuizAPI.dto.QuizDTO;
import org.pg6100.QuizAPI.dto.RootCategoryDTO;
import org.pg6100.QuizAPI.dto.SubCategoryDTO;
import org.pg6100.QuizAPI.dto.SubSubCategoryDTO;
import org.pg6100.utils.web.JBossUtil;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

class QuizRestTestBase {

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
        List<QuizDTO> list1 = Arrays.asList(given().accept(ContentType.JSON).get("/quiz")
                .then()
                .statusCode(200)
                .extract().as(QuizDTO[].class));

        list1.forEach(dto ->
                given().pathParam("id", dto.id).delete("/quiz/id/{id}").then().statusCode(204));

        get("/quiz").then().statusCode(200).body("size()", is(0));

        List<SubSubCategoryDTO> list2 = Arrays.asList(given().accept(ContentType.JSON).get("/subsubcategories")
                .then()
                .statusCode(200)
                .extract().as(SubSubCategoryDTO[].class));

        list2.forEach(dto ->
                given().pathParam("id", dto.name).delete("/subsubcategories/id/{id}").then().statusCode(204));

        get("/subsubcategories").then().statusCode(200).body("size()", is(0));

        List<SubCategoryDTO> list3 = Arrays.asList(given().accept(ContentType.JSON).get("/subcategories")
                .then()
                .statusCode(200)
                .extract().as(SubCategoryDTO[].class));

        list3.forEach(dto ->
                given().pathParam("id", dto.name).delete("/subcategories/id/{id}").then().statusCode(204));

        get("/subcategories").then().statusCode(200).body("size()", is(0));

        List<RootCategoryDTO> list4 = Arrays.asList(given().accept(ContentType.JSON).get("/categories")
                .then()
                .statusCode(200)
                .extract().as(RootCategoryDTO[].class));

        list4.forEach(dto ->
                given().pathParam("id", dto.name).delete("/categories/id/{id}").then().statusCode(204));

        get("/categories").then().statusCode(200).body("size()", is(0));
    }
}
