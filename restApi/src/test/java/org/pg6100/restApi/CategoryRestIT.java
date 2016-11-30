package org.pg6100.restApi;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.pg6100.restApi.dto.RootCategoryDTO;
import org.pg6100.restApi.dto.SubCategoryDTO;
import org.pg6100.restApi.dto.SubSubCategoryDTO;

import java.util.Arrays;

import static com.sun.javafx.fxml.expression.Expression.equalTo;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
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

        testGetCategory("/categories", "name");
    }

    @Test
    public void testCreateAndGetSubCategory() {
        RootCategoryDTO rootDTO = createRootCategoryDTO("name");

        get("/subcategories").then().statusCode(200).body("size()", is(0));

        SubCategoryDTO dto = createSubCategoryDTO(rootDTO.name, "name");
        get("/subcategories").then().statusCode(200).body("size()", is(1));

        testGetCategory("/subcategories", "name");
    }

    @Test
    public void testCreateAndGetSubSubCategory() {
        RootCategoryDTO rootDTO = createRootCategoryDTO("name");
        SubCategoryDTO subDTO = createSubCategoryDTO(rootDTO.name, "name");
        get("/subsubcategories").then().statusCode(200).body("size()", is(0));

        SubSubCategoryDTO dto = createSubSubCategoryDTO(subDTO.name, "name");
        get("/subsubcategories").then().statusCode(200).body("size()", is(1));

        testGetCategory("/subsubcategories", "name");
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
        deleteCategory(createRootCategoryDTO("name"), "name", "/categories");
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
        SubCategoryDTO dto1 = createSubCategoryDTO(dto.name, "name1");
        SubCategoryDTO dto2 = createSubCategoryDTO(dto.name, "name2");
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
        deleteCategory(createSubCategoryDTO(dto.name, "name"), "name", "/subcategories");
        get("/subcategories").then().statusCode(200).body("size()", is(0));
    }

    @Test
    public void testGetSubCategoriesByRootCategory() throws Exception {
        createRootCategoryDTO("root");
        createSubCategoryDTO("root", "sub1");
        createSubCategoryDTO("root", "sub2");
        createRootCategoryDTO("root2");
        createSubCategoryDTO("root2", "sub3");

        given().pathParam("id", "root")
                .get("/categories/id/{id}/subcategories")
                .then()
                .statusCode(200)
                .body("name", hasItems("sub1", "sub2"));
    }

    @Test
    public void testGetSubWithGivenParentByCategory() throws Exception {
        createRootCategoryDTO("root");
        createSubCategoryDTO("root", "sub1");
        createSubCategoryDTO("root", "sub2");
        createRootCategoryDTO("root2");
        createSubCategoryDTO("root2", "sub3");

        given().pathParam("id", "root")
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
        SubCategoryDTO subDTO = createSubCategoryDTO(rootDTO.name, "sub");
        deleteCategory(createSubSubCategoryDTO(subDTO.name, "name"), "name", "/subsubcategories");
        get("/subsubcategories").then().statusCode(200).body("size()", is(0));
    }

    /* TODO
    @Test
    public void testGetSubSubWithQuizes() throws Exception {
        RootCategoryDTO rootDTO = createRootCategoryDTO("root");
        SubCategoryDTO subDTO = createSubCategoryDTO(rootDTO.name, "sub");
        SubSubCategoryDTO subSubDTO = createSubSubCategoryDTO(subDTO.name, "subsub");

        get("/categories/withQuizzes/subsubcategories").then().statusCode(200).body("size()", is(1));
    }
    */

    @Test
    public void testGetSubSubBySubCategory() throws Exception {
        createRootCategoryDTO("root");
        createSubCategoryDTO("root", "sub");
        createSubSubCategoryDTO("sub", "subsub1");
        createSubSubCategoryDTO("sub", "subsub2");

        given().pathParam("id", "sub")
                .get("/subsubcategories/id/{id}/subsubcategories")
                .then()
                .statusCode(200)
                .body("name", hasItems("subsub1", "subsub2"));
    }

    @Test
    public void testGetSubSubWithGivenSubParentByCategory() throws Exception {
        createRootCategoryDTO("root");
        createSubCategoryDTO("root", "sub");
        createSubSubCategoryDTO("sub", "subsub1");
        createSubSubCategoryDTO("sub", "subsub2");

        given().pathParam("id", "subsub1")
                .get("/subsubcategories/parent/{id}")
                .then()
                .statusCode(200)
                .body("name", hasItems("subsub1", "subsub2"));
    }


    private RootCategoryDTO createRootCategoryDTO(String name) {
        RootCategoryDTO dto = new RootCategoryDTO(name);
        registerCategory(dto, "/categories");
        return dto;
    }

    private SubCategoryDTO createSubCategoryDTO(String rootCategoryName, String name) {
        SubCategoryDTO dto = new SubCategoryDTO(rootCategoryName, name);
        registerCategory(dto, "/subcategories");
        return dto;
    }

    private SubSubCategoryDTO createSubSubCategoryDTO(String subCategoryName, String name) {
        SubSubCategoryDTO dto = new SubSubCategoryDTO(subCategoryName, name);
        registerCategory(dto, "/subsubcategories");
        return dto;
    }

    public void testGetCategory(String path, String name) {
        given().pathParam("id", name)
                .get(path + "/id/{id}")
                .then()
                .statusCode(200)
                .body("name", hasItem(name));
    }


    public void registerCategory(Object dto, String path) {
        given().contentType(ContentType.JSON)
                .body(dto)
                .post(path)
                .then()
                .statusCode(200);
    }

    public void updateCategory(Object dto, String name, String path) {
        given().contentType(ContentType.JSON)
                .pathParam("id", name)
                .body(dto)
                .put(path + "/id/{id}")
                .then()
                .statusCode(204);
    }

    public void deleteCategory(Object dto, String name, String path) {
        given().contentType(ContentType.JSON)
                .pathParam("id", name)
                .body(dto)
                .delete(path + "/id/{id}")
                .then()
                .statusCode(204);
    }
}