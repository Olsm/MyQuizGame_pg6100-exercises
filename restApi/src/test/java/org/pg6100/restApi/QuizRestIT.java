package org.pg6100.restApi;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.pg6100.restApi.dto.QuizDTO;
import org.pg6100.restApi.dto.RootCategoryDTO;
import org.pg6100.restApi.dto.SubCategoryDTO;
import org.pg6100.restApi.dto.SubSubCategoryDTO;

import java.util.ArrayList;
import java.util.Arrays;
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

        QuizDTO dto = createQuizDTO(null, category, question, answerList, correctAnswer);
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
        String id = testRegisterQuiz(createQuizDTO()).extract().asString();
        testGet().body("id", is(Arrays.asList(id)));

        delete("/quiz/id/" + id);
        testGet().body("id", not(Arrays.asList(id)));
    }


    @Test
    public void testUpdate() throws Exception {
        QuizDTO quizDTO = createQuizDTO();
        String id = testRegisterQuiz(quizDTO).extract().asString();
        testGet().body("question", contains(quizDTO.question));

        String updatedQuestion = "new question";

        //now change text with PUT
        QuizDTO dto = createQuizDTO(id, quizDTO.category, updatedQuestion, quizDTO.answerList, quizDTO.correctAnswer);
        testUpdateQuiz(dto, dto.id);

        //was the PUT fine?
        testGet().body("question", contains(updatedQuestion));

        //now rechange, but just the text
        String anotherQuestion = "yet another question";

        given().contentType(ContentType.TEXT)
                .body(anotherQuestion)
                .pathParam("id", id)
                .put("/quiz/id/{id}/question")
                .then()
                .statusCode(204);

        testGet().body("question", contains(anotherQuestion));
    }

    @Test
    public void testMissingForUpdate() {
        QuizDTO quizDTO = createQuizDTO();
        quizDTO.id = "-333";
        testUpdateQuiz(quizDTO, "-333", 404);
    }

    @Test
    public void testUpdateNonMatchingId() {
        QuizDTO quizDTO = createQuizDTO();
        quizDTO.id = "1";
        testUpdateQuiz(quizDTO, "-333", 409);
    }


    @Test
    public void testInvalidUpdate() {
        QuizDTO dto = createQuizDTO();
        dto.id = testRegisterQuiz(dto).extract().asString();
        QuizDTO quizDTO = createQuizDTO(dto.id, null, "", null, null);
        testUpdateQuiz(quizDTO, dto.id, 400);
    }

    private void createSomeQuizes() {
        RootCategoryDTO rootCategory2 = new RootCategoryDTO("root2");
        SubCategoryDTO subCategory2 = new SubCategoryDTO(rootCategory2.name, "sub2");
        SubSubCategoryDTO category2 = new SubSubCategoryDTO(subCategory2.name, "subsub2");
        SubSubCategoryDTO category3 = new SubSubCategoryDTO(subCategory2.name, "subsub3");
        registerCategory(rootCategory2, "/categories");
        registerCategory(subCategory2, "/subcategories");
        registerCategory(category2, "/subsubcategories");
        registerCategory(category3, "/subsubcategories");
        List<String> answerList = new ArrayList<>();
        answerList.add("ans1");
        answerList.add("ans2");
        answerList.add("ans3");
        answerList.add("ans4");
        testRegisterQuiz(createQuizDTO(null, category, "q1", answerList, answerList.get(1)));
        testRegisterQuiz(createQuizDTO(null, category, "q2", answerList, answerList.get(1)));
        testRegisterQuiz(createQuizDTO(null, category, "q3", answerList, answerList.get(1)));
        testRegisterQuiz(createQuizDTO(null, category, "q4", answerList, answerList.get(1)));
        testRegisterQuiz(createQuizDTO(null, category2, "q5", answerList, answerList.get(1)));
        testRegisterQuiz(createQuizDTO(null, category2, "q6", answerList, answerList.get(1)));
    }

    private QuizDTO createQuizDTO(String id, SubSubCategoryDTO category, String question, List<String> answerList, String correctAnswer) {
        QuizDTO quizDTO = new QuizDTO(id, category, question, answerList, correctAnswer);
        return quizDTO;
    }

    private QuizDTO createQuizDTO() {
        String question = "Such Question";
        List<String> answerList = new ArrayList<>();
        answerList.add("ans1");
        answerList.add("ans2");
        answerList.add("ans3");
        answerList.add("ans4");
        String correctAnswer = answerList.get(3);
        return createQuizDTO(null, category, question, answerList, correctAnswer);
    }

    @Test
    public void testGetAll() {
        createSomeQuizes();
        testGet().body("size()", is(6));
    }

    @Test
    public void testGetAllByCategory() {
        createSomeQuizes();
        testGet("/categories/{id}", category.name).body("size()", is(4));
        testGet("/categories/{id}", "subsub2").body("size()", is(2));
        testGet("/categories/{id}", "subsub3").body("size()", is(0));
    }

    @Test
    public void testInvalidGetByCategory() {
        testGet("/categories/{id}", "foo", 400);
    }

    @Test
    public void testInvalidCategory() {
        QuizDTO quizDTO = createQuizDTO();
        quizDTO.category = new SubSubCategoryDTO("null", "null");
        testRegisterQuiz(quizDTO, 400);
    }

    @Test
    public void testPostWithId() {
        QuizDTO quizDTO = createQuizDTO();
        testRegisterQuiz(createQuizDTO("1", quizDTO.category, quizDTO.question, quizDTO.answerList, quizDTO.correctAnswer), 400);
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
                .post("/quiz")
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

        testGet("/{id}", "foo", 404);
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
        return  testGet(path, id, 200);
    }

    private ValidatableResponse testGet(String path, String id, int statusCode) {
        return given().pathParam("id", id)
                .get("/quiz" + path).then()
                .statusCode(statusCode);
    }

    private ValidatableResponse testRegisterQuiz(Object dto) {
        return  testRegisterQuiz(dto, 200);
    }

    private ValidatableResponse testRegisterQuiz(Object dto, int statusCode) {
        return given().contentType(ContentType.JSON)
                .body(dto)
                .post("/quiz")
                .then()
                .statusCode(statusCode);
    }

    private ValidatableResponse testUpdateQuiz(Object dto, String id) {
        return testUpdateQuiz(dto, id, 204);
    }

    private ValidatableResponse testUpdateQuiz(Object dto, String id, int statusCode) {
        return given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(dto)
                .put("/quiz/id/{id}")
                .then()
                .statusCode(statusCode);
    }
}