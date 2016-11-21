package org.pg6100.restApi;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.pg6100.restApi.dto.RootCategoryDTO;

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
    public void testCreateAndGet() {

        RootCategoryDTO dto = createRootCategory("name");

        get("/categories").then().statusCode(200).body("size()", is(0));

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        get().then().statusCode(200).body("size()", is(1));

        given().pathParam("name", id)
                .get("/name/{id}")
                .then()
                .statusCode(200)
                .body("name", is(id));
    }

    private RootCategoryDTO createRootCategory(String name) {
        RootCategoryDTO dto = new RootCategoryDTO(name);
        given().contentType(ContentType.JSON)
                .body(dto)
                .post("/categories")
                .then()
                .statusCode(200);
        return dto;
    }
}