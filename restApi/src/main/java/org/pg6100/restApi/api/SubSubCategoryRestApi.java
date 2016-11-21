package org.pg6100.restApi.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.pg6100.restApi.dto.RootCategoryDTO;
import org.pg6100.restApi.dto.SubCategoryDTO;
import org.pg6100.restApi.dto.SubSubCategoryDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/subsubcategories", description = "Handling of creating and retrieving sub sub categories")
@Path("/subsubcategories")
@Produces(MediaType.APPLICATION_JSON)
public interface SubSubCategoryRestApi {

    String ID_PARAM = "The numeric id of the category";

    @ApiOperation("Create a subsub category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new subsub category")
    String createSubSubCategory(
            @ApiParam("Category name and sub category")
                    SubSubCategoryDTO dto);

    @ApiOperation("Get subsub category by id (name)")
    @GET
    @Path("/id/{id}")
    SubSubCategoryDTO getSubSubCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("Update subsub category by id (name)")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateSubSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category,
            @ApiParam("The subsub category that will replace the old one")
                    SubSubCategoryDTO dto);

    /* TODO: Patch
    @ApiOperation("")
    @PATCH
    @Path("/id/{id}");
    */

    @ApiOperation("Delete a subsub category with given id (name)")
    @DELETE
    @Path("/id/{id}")
    void deleteSubSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("Get all subsubcategories with at least one quiz")
    @GET
    @Path("/categories/withQuizzes/subsubcategories")
    List<SubSubCategoryDTO> getSubSubWithQuizes();

    @ApiOperation("GET all subsubcategories of the subcategory specified by id (name)")
    @GET
    @Path("/subcategories/id/{category}/subsubcategories")
    List<SubSubCategoryDTO> getSubSubBySubCategory(
            @ApiParam("The sub category name")
            @PathParam("category")
                    String category);

    @ApiOperation("GET all subsubcategories with the given subcategory parent specified by id (name)")
    @GET
    @Path("/parent/{category}")
    List<SubSubCategoryDTO> getSubSubWithGivenSubParentByCategory(
            @ApiParam("The subsub category name")
            @PathParam("category")
                    String category);
}
