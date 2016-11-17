package org.pg6100.restApi.api;

import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;
import org.pg6100.restApi.dto.CategoryDTO;

import javax.persistence.GeneratedValue;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/categories", description = "Handling of creating and retrieving categories")
@Produces(MediaType.APPLICATION_JSON)
public interface CategoryRestApi {

    String ID_PARAM = "The numeric id of the category";

    @ApiOperation("Get all the categories")
    @GET
    List<CategoryDTO> get();

    @ApiOperation("Create a category")
    @POST
    @Path("/categories")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new category")
    String createRootCategory(
            @ApiParam("Category name")
                    CategoryDTO dto);

    @ApiOperation("Get category by id (name)")
    @GET
    @Path("/categories/id/{id}")
    CategoryDTO getRootCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("Update category by id (name)")
    @PUT
    @Path("/categories/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateRootCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category,
            @ApiParam("The category that will replace the old one")
                    CategoryDTO dto);

    /* TODO: Patch
    @ApiOperation("")
    @PATCH
    @Path("/subcategories/id/{id}");
    */

    @ApiOperation("Delete a category with given id (name)")
    @DELETE
    @Path("/categories/id/{id}")
    void deleteRootCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("Create a subsub category")
    @POST
    @Path("/subsub categories")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new subsub category")
    String createSubSubCategory(
            @ApiParam("Category name and sub category")
                    CategoryDTO dto);

    @ApiOperation("Get subsub category by id (name)")
    @GET
    @Path("/subsubcategories/id/{id}")
    CategoryDTO getSubSubCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("Update subsub category by id (name)")
    @PUT
    @Path("/subsubcategories/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateSubSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category,
            @ApiParam("The subsub category that will replace the old one")
                    CategoryDTO dto);

    /* TODO: Patch
    @ApiOperation("")
    @PATCH
    @Path("/subsubcategories/id/{id}");
    */

    @ApiOperation("Delete a subsub category with given id (name)")
    @DELETE
    @Path("/subsubcategories/id/{id}")
    void deleteSubSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("Create a subcategory")
    @POST
    @Path("/subcategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new subcategory")
    String createSubCategory(
            @ApiParam("Category name and root category")
                    CategoryDTO dto);

    @ApiOperation("Get subcategory by id (name)")
    @GET
    @Path("/subcategories/id/{id}")
    CategoryDTO getSubCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("Update sub category by id (name)")
    @PUT
    @Path("/subcategories/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category,
            @ApiParam("The sub category that will replace the old one")
                    CategoryDTO dto);

    /* TODO: Patch
    @ApiOperation("")
    @PATCH
    @Path("/categories/id/{id}");
    */

    @ApiOperation("Delete a sub category with given id (name)")
    @DELETE
    @Path("/subcategories/id/{id}")
    void deleteSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("all categories that have at least one subcategory with at least one subsubcategory with at least one quiz.")
    @GET
    @Path("/categories/withQuizzes")
    List<CategoryDTO> getWithQuizes();

    @ApiOperation("Get all subsubcategories with at least one quiz")
    @GET
    @Path("/categories/withQuizzes/subsubcategories")
    List<CategoryDTO> getSubSubWithQuizes();

    @ApiOperation("GET all subcategories of the category specified by id (name)")
    @GET
    @Path("/categories/id/{category}/subcategories")
    List<CategoryDTO> getSubCategoriesByRootCategory(
            @ApiParam("The root category name")
            @PathParam("category")
                String category);

    @ApiOperation("GET all subcategories with the given parent specified by id (name)")
    @GET
    @Path("/subcategories/parent/{category}")
    List<CategoryDTO> getSubWithGivenParentByCategory(
            @ApiParam("The sub category name")
            @PathParam("category")
                String category);

    @ApiOperation("GET all subsubcategories of the subcategory specified by id (name)")
    @GET
    @Path("/subcategories/id/{category}/subsubcategories")
    List<CategoryDTO> getSubSubBySubCategory(
            @ApiParam ("The sub category name")
            @PathParam("category")
                    String category);

    @ApiOperation("GET all subsubcategories with the given subcategory parent specified by id (name)")
    @GET
    @Path("/subsubcategories/parent/{category}")
    List<CategoryDTO> getSubSubWithGivenSubParentByCategory(
            @ApiParam("The subsub category name")
            @PathParam("category")
                    String category);
}
