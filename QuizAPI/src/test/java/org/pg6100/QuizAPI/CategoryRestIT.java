package org.pg6100.QuizAPI;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.pg6100.QuizAPI.dto.RootCategoryDTO;
import org.pg6100.QuizAPI.dto.SubCategoryDTO;
import org.pg6100.QuizAPI.dto.SubSubCategoryDTO;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;

/*
    Unless otherwise specified, you will have to write tests
    for REST APIs using RestAssured
 */
public class CategoryRestIT extends CategoryRestTestBase {

    @Test
    public void testCleanDB() {

        get("/categories").then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testCreateAndGetRootCategory() {
        get("/categories").then().statusCode(200).body("size()", is(0));

        RootCategoryDTO dto = createRootCategoryDTO("name");
        get("/categories").then().statusCode(200).body("size()", is(1));

        testGetCategory("/categories", dto.id);
    }

    @Test
    public void testCreateAndGetSubCategory() {
        RootCategoryDTO rootDTO = createRootCategoryDTO("name");

        get("/subcategories").then().statusCode(200).body("size()", is(0));

        SubCategoryDTO dto = createSubCategoryDTO(rootDTO.id, "name");
        get("/subcategories").then().statusCode(200).body("size()", is(1));

        testGetCategory("/subcategories", dto.id);
    }

    @Test
    public void testCreateAndGetSubSubCategory() {
        RootCategoryDTO rootDTO = createRootCategoryDTO("name");
        SubCategoryDTO subDTO = createSubCategoryDTO(rootDTO.id, "name");
        get("/subsubcategories").then().statusCode(200).body("size()", is(0));

        SubSubCategoryDTO dto = createSubSubCategoryDTO(subDTO.id, "name");
        get("/subsubcategories").then().statusCode(200).body("size()", is(1));

        testGetCategory("/subsubcategories", dto.id);
    }

    @Test
    public void testGetRootCategories() throws Exception {
        RootCategoryDTO dto1 = createRootCategoryDTO("name1");
        RootCategoryDTO dto2 = createRootCategoryDTO("name2");
        get("/categories").then().statusCode(200).body("size()", is(2))
            .body("name", hasItems("name1", "name2"));
    }

    /* TODO
    @Test
    public void testUpdateRootCategory() throws Exception {
        RootCategoryDTO dto = createRootCategoryDTO("name1");
        dto.name = "name2";
        updateCategory(dto, "name1", "/categories");
        testGetCategory("/categories", "name2");
    }
    */

    @Test
    public void testDeleteRootCategory() throws Exception {
        RootCategoryDTO dto = createRootCategoryDTO("name");
        deleteCategory(dto.id, "/categories");
        get("/categories").then().statusCode(200).body("size()", is(0));
    }

    /* TODO
    @Test
    public void testGetWithQuizes() throws Exception {

    }
    */


    @Test
    public void testGetSubCategories() throws Exception {
        RootCategoryDTO dto = createRootCategoryDTO("name");
        SubCategoryDTO dto1 = createSubCategoryDTO(dto.id, "name1");
        SubCategoryDTO dto2 = createSubCategoryDTO(dto.id, "name2");
        get("/subcategories").then().statusCode(200).body("size()", is(2))
                .body("name", hasItems("name1", "name2"));
    }

    /* TODO
    @Test
    public void testUpdateSubCategory() throws Exception {

    }
    */

    @Test
    public void testDeleteSubCategory() throws Exception {
        RootCategoryDTO dto = createRootCategoryDTO("name");
        SubCategoryDTO subDTO = createSubCategoryDTO(dto.id, "name");
        deleteCategory(subDTO.id, "/subcategories");
        get("/subcategories").then().statusCode(200).body("size()", is(0));
    }

    @Test
    public void testGetSubCategoriesByRootCategory() throws Exception {
        RootCategoryDTO rootDTO = createRootCategoryDTO("root");
        RootCategoryDTO rootDTO2 = createRootCategoryDTO("root2");
        SubCategoryDTO subDTO = createSubCategoryDTO(rootDTO.id, "sub1");
        SubCategoryDTO subDTO2 = createSubCategoryDTO(rootDTO.id, "sub2");
        SubCategoryDTO subDTO3 = createSubCategoryDTO(rootDTO2.id, "sub3");

        given().pathParam("id", rootDTO.id)
                .get("/categories/id/{id}/subcategories")
                .then()
                .statusCode(200)
                .body("name", hasItems("sub1", "sub2"));
    }

    @Test
    public void testGetSubWithGivenParentByCategory() throws Exception {
        RootCategoryDTO rootDTO = createRootCategoryDTO("root");
        createSubCategoryDTO(rootDTO.id, "sub1");
        createSubCategoryDTO(rootDTO.id, "sub2");
        RootCategoryDTO rootDTO2 = createRootCategoryDTO("root2");
        createSubCategoryDTO(rootDTO2.id, "sub3");

        given().pathParam("id", rootDTO.id)
                .get("/subcategories/parent/{id}")
                .then()
                .statusCode(200)
                .body("name", hasItems("sub1", "sub2"));
    }


    /* TODO
    @Test
    public void testUpdateSubSubCategory() throws Exception {

    }
    */

    @Test
    public void testDeleteSubSubCategory() throws Exception {
        RootCategoryDTO rootDTO = createRootCategoryDTO("root");
        SubCategoryDTO subDTO = createSubCategoryDTO(rootDTO.id, "sub");
        SubSubCategoryDTO subSubDTO = createSubSubCategoryDTO(subDTO.id, "name");
        deleteCategory(subSubDTO.id, "/subsubcategories");
        get("/subsubcategories").then().statusCode(200).body("size()", is(0));
    }

    /* TODO
    @Test
    public void testGetSubSubWithQuizes() throws Exception {
        RootCategoryDTO rootDTO = createRootCategoryDTO("root");
        SubCategoryDTO subDTO = createSubCategoryDTO(rootDTO.id, "sub");
        SubSubCategoryDTO subSubDTO = createSubSubCategoryDTO(subDTO.id, "subsub");

        get("/categories/withQuizzes/subsubcategories").then().statusCode(200).body("size()", is(1));
    }
    */

    @Test
    public void testGetSubSubBySubCategory() throws Exception {
        RootCategoryDTO rootDTO = createRootCategoryDTO("root");
        SubCategoryDTO subDTO = createSubCategoryDTO(rootDTO.id, "sub");
        createSubSubCategoryDTO(subDTO.id, "subsub1");
        createSubSubCategoryDTO(subDTO.id, "subsub2");

        given().pathParam("id", subDTO.id)
                .get("/subsubcategories/id/{id}/subsubcategories")
                .then()
                .statusCode(200)
                .body("name", hasItems("subsub1", "subsub2"));
    }

    @Test
    public void testGetSubSubWithGivenSubParentByCategory() throws Exception {
        RootCategoryDTO rootDTO = createRootCategoryDTO("root");
        SubCategoryDTO subDTO = createSubCategoryDTO(rootDTO.id, "sub");
        SubSubCategoryDTO subSubDTO1 = createSubSubCategoryDTO(subDTO.id, "subsub1");
        createSubSubCategoryDTO(subDTO.id, "subsub2");

        given().pathParam("id", subSubDTO1.id)
                .get("/subsubcategories/parent/{id}")
                .then()
                .statusCode(200)
                .body("name", hasItems("subsub1", "subsub2"));
    }


    private RootCategoryDTO createRootCategoryDTO(String name) {
        RootCategoryDTO dto = new RootCategoryDTO(name);
        dto.id = registerCategory(dto, "/categories");
        return dto;
    }

    private SubCategoryDTO createSubCategoryDTO(String rootCategoryId, String name) {
        SubCategoryDTO dto = new SubCategoryDTO(rootCategoryId, name);
        dto.id = registerCategory(dto, "/subcategories");
        return dto;
    }

    private SubSubCategoryDTO createSubSubCategoryDTO(String subCategoryId, String name) {
        SubSubCategoryDTO dto = new SubSubCategoryDTO(subCategoryId, name);
        dto.id = registerCategory(dto, "/subsubcategories");
        return dto;
    }

    private void testGetCategory(String path, String id) {
        given().pathParam("id", id)
                .get(path + "/id/{id}")
                .then()
                .statusCode(200)
                .body("id", hasItem(id));
    }


    private String registerCategory(Object dto, String path) {
        return given().contentType(ContentType.JSON)
                .body(dto)
                .post(path)
                .then()
                .statusCode(200)
                .extract().asString();
    }

    public void updateCategory(Object dto, String id, String path) {
        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(dto)
                .put(path + "/id/{id}")
                .then()
                .statusCode(204);
    }

    private void deleteCategory(String id, String path) {
        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .delete(path + "/id/{id}")
                .then()
                .statusCode(204);
    }
}