package org.pg6100.restApi;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.pg6100.quiz.datalayer.SubSubCategory;
import org.pg6100.restApi.dto.RootCategoryDTO;
import org.pg6100.restApi.dto.SubCategoryDTO;
import org.pg6100.restApi.dto.SubSubCategoryDTO;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
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

        given().pathParam("id", dto.name)
                .get("/categories/id/{id}")
                .then()
                .statusCode(200)
                .body("name", is(dto.name));
    }

    @Test
    public void testCreateAndGetSubCategory() {
        RootCategoryDTO rootDTO = createRootCategoryDTO("name");

        get("/subcategories").then().statusCode(200).body("size()", is(0));

        SubCategoryDTO dto = createSubCategoryDTO(rootDTO.name, "name");
        get("/subcategories").then().statusCode(200).body("size()", is(1));

        given().pathParam("id", dto.name)
                .get("/subcategories/id/{id}")
                .then()
                .statusCode(200)
                .body("name", is(dto.name));
    }

    @Test
    public void testCreateAndGetSubSubCategory() {
        RootCategoryDTO rootDTO = createRootCategoryDTO("name");
        SubCategoryDTO subDTO = createSubCategoryDTO(rootDTO.name, "name");
        get("/subsubcategories").then().statusCode(200).body("size()", is(0));

        SubSubCategoryDTO dto = createSubSubCategoryDTO(subDTO.name, "name");
        get("/subsubcategories").then().statusCode(200).body("size()", is(1));

        given().pathParam("id", dto.name)
                .get("/subsubcategories/id/{id}")
                .then()
                .statusCode(200)
                .body("name", is(dto.name));
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

    public void registerCategory(Object dto, String path) {
        given().contentType(ContentType.JSON)
                .body(dto)
                .post(path)
                .then()
                .statusCode(200);
    }
}