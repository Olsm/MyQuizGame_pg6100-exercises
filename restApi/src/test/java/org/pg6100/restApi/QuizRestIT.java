package org.pg6100.restApi;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pg6100.restApi.dto.QuizDTO;
import org.pg6100.restApi.dto.RootCategoryDTO;
import org.pg6100.restApi.dto.SubCategoryDTO;
import org.pg6100.restApi.dto.SubSubCategoryDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;

/*
    Unless otherwise specified, you will have to write tests
    for REST APIs using RestAssured
 */
public class QuizRestIT extends QuizRestTestBase {

    private RootCategoryDTO rootCategory;
    private SubCategoryDTO subCategory;
    private SubSubCategoryDTO category;

    @Before
    public void setup() {
        rootCategory = new RootCategoryDTO("rootCategory");
        subCategory = new SubCategoryDTO(rootCategory.name, "subCategory");
        category = new SubSubCategoryDTO(subCategory.name, "category");
        registerCategory(rootCategory, "/categories");
        registerCategory(subCategory, "/subcategories");
        registerCategory(category, "/subsubcategories");
    }

    @Test
    public void testCleanDB() {
        testGet().body("size()", is(0));
    }

    @Test
    public void testCreateAndGet() {
        String question = "Such Question";
        List<String> answerList = new ArrayList<>();
        answerList.add("ans1");
        answerList.add("ans2");
        answerList.add("ans3");
        answerList.add("ans4");
        String correctAnswer = answerList.get(3);
        testGet().body("size()", is(0));

        QuizDTO dto = createQuiz(null, category, question, answerList, correctAnswer);
        String id = testRegisterQuiz(dto).extract().asString();
        testGet().body("size()", is(1));
        testGet("/id/{id}", id)
                .body("id", is(id))
                .body("category.name", is(category.name))
                .body("category.subCategoryName", is(category.subCategoryName))
                .body("question", is(question))
                .body("answerList", is(answerList))
                .body("correctAnswer", is(correctAnswer));
    }

    @Test
    public void testDelete() {
        String id = testRegisterQuiz(createQuiz()).extract().asString();
        testGet().body("id", contains(id));
        
        delete("/id/" + id);
        get().then().body("id", not(contains(id)));
    }


    @Test
    public void testUpdate() throws Exception {

        QuizDTO quizDTO = createQuiz();

        //first create with a POST
        String id = given().contentType(ContentType.JSON)
                .body(quizDTO)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        //check if POST was fine
        get("/id/" + id).then().body("question", is(quizDTO.question));

        String updatedQuestion = "new question";

        //now change text with PUT
        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(createQuiz(null, quizDTO.category, updatedQuestion, quizDTO.answerList, quizDTO.correctAnswer))
                .put("/id/{id}")
                .then()
                .statusCode(204);

        //was the PUT fine?
        get("/id/" + id).then().body("question", is(updatedQuestion));


        //now rechange, but just the text
        String anotherQuestion = "yet another question";

        given().contentType(ContentType.TEXT)
                .body(anotherQuestion)
                .pathParam("id", id)
                .put("/id/{id}/question")
                .then()
                .statusCode(204);

        get("/id/" + id).then().body("question", is(anotherQuestion));
    }

    @Test
    public void testMissingForUpdate() {

        given().contentType(ContentType.JSON)
                .body("{\"id\":-333}")
                .pathParam("id", "-333")
                .put("/id/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdateNonMatchingId() {

        QuizDTO quizDTO = createQuiz();

        given().contentType(ContentType.JSON)
                .body(createQuiz("222", quizDTO.category, quizDTO.question, quizDTO.answerList, quizDTO.correctAnswer))
                .pathParam("id", "-333")
                .put("/id/{id}")
                .then()
                .statusCode(409);
    }


    @Test
    public void testInvalidUpdate() {

        QuizDTO quizDTO = createQuiz();

        String id = given().contentType(ContentType.JSON)
                .body(quizDTO)
                .post()
                .then()
                .extract().asString();

        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(createQuiz(null, null, "", null, null))
                .put("/id/{id}")
                .then()
                .statusCode(400);
    }

    private void createSomeQuizes() {
        List<String> answerList = new ArrayList<>();
        answerList.add("ans1");
        answerList.add("ans2");
        answerList.add("ans3");
        answerList.add("ans4");
        createQuiz(null, category, "q1", answerList, answerList.get(1));
        createQuiz(null, category, "q2", answerList, answerList.get(1));
        createQuiz(null, category, "q3", answerList, answerList.get(1));
        createQuiz(null, category, "q4", answerList, answerList.get(1));
        createQuiz(null, category, "q5", answerList, answerList.get(1));
        createQuiz(null, category, "q6", answerList, answerList.get(1));
    }

    private QuizDTO createQuiz(String id, SubSubCategoryDTO category, String question, List<String> answerList, String correctAnswer) {
        QuizDTO quizDTO = new QuizDTO(id, category, question, answerList, correctAnswer);
        return quizDTO;
    }

    private QuizDTO createQuiz() {
        String question = "Such Question";
        List<String> answerList = new ArrayList<>();
        answerList.add("ans1");
        answerList.add("ans2");
        answerList.add("ans3");
        answerList.add("ans4");
        String correctAnswer = answerList.get(3);
        return createQuiz(null, category, question, answerList, correctAnswer);
    }

    @Test
    public void testGetAll() {

        get().then().body("size()", is(0));
        createSomeQuizes();

        get().then().body("size()", is(6));
    }

    @Test
    public void testGetAllByCategory() {

        get().then().body("size()", is(0));
        createSomeQuizes();

        get("/categories/Norway").then().body("size()", is(3));
        get("/countries/Sweden").then().body("size()", is(1));
        get("/countries/Iceland").then().body("size()", is(2));
    }

    @Test
    public void testInvalidGetByCategory() {

        get("/countries/foo").then().statusCode(400);
    }

    @Test
    public void testInvalidCategory() {

        QuizDTO quizDTO = createQuiz();

        given().contentType(ContentType.JSON)
                .body(createQuiz(quizDTO.id, null, quizDTO.question, new ArrayList<>(), "test"))
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void testPostWithId() {

        QuizDTO quizDTO = createQuiz();

        given().contentType(ContentType.JSON)
                .body(createQuiz("1", quizDTO.category, quizDTO.question, quizDTO.answerList, quizDTO.correctAnswer))
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void testPostWithWrongType() {

        /*
            HTTP Error 415: "Unsupported media type"
            The REST API is set to return data in JSon, ie
            @Produces(MediaType.APPLICATION_JSON)
            so, if ask for XML, we should get a 415 error.
            Note: a server might provide the same resource (on same URL)
            with different formats!
            Although nowadays most just deal with Json.
         */

        given().contentType(ContentType.XML)
                .body("<foo></foo>")
                .post()
                .then()
                .statusCode(415);
    }


    @Test
    public void testGetByInvalidId() {

        /*
            In this particular case, "foo" might be a valid id.
            however, as it is not in the database, and there is no mapping
            for a String id, the server will say "Not Found", ie 404.
         */

        get("/id/foo")
                .then()
                .statusCode(404);
    }

    private void registerCategory(Object dto, String path) {
        given().contentType(ContentType.JSON)
                .body(dto)
                .post(path)
                .then()
                .statusCode(200);
    }

    private ValidatableResponse testGet() {
        return testGet("");
    }

    private ValidatableResponse testGet(String path) {
        return get("/quiz" + path).then()
                .statusCode(200);
    }

    private ValidatableResponse testGet(String path, String id) {
        return given().pathParam("id", id)
                .get("/quiz" + path).then()
                .statusCode(200);
    }

    private ValidatableResponse testRegisterQuiz(Object dto) {
        return given().contentType(ContentType.JSON)
                .body(dto)
                .post("/quiz")
                .then()
                .statusCode(200);
    }
}