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

@Api(value = "/subcategories", description = "Handling of creating and retrieving sub categories")
@Path("/subcategories")
@Produces(MediaType.APPLICATION_JSON)
public interface SubCategoryRestApi {

    String ID_PARAM = "The numeric id of the category";

    @ApiOperation("Get all the sub categories")
    @GET
    List<SubCategoryDTO> get();

    @ApiOperation("Create a subcategory")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new subcategory")
    String createSubCategory(
            @ApiParam("Category name and root category")
                    SubCategoryDTO dto);

    @ApiOperation("Get subcategory by id (name)")
    @GET
    @Path("/id/{id}")
    SubCategoryDTO getSubCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("Update sub category by id (name)")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category,
            @ApiParam("The sub category that will replace the old one")
                    SubCategoryDTO dto);

    /* TODO: Patch
    @ApiOperation("")
    @PATCH
    @Path("/categories/id/{id}");
    */

    @ApiOperation("Delete a sub category with given id (name)")
    @DELETE
    @Path("/id/{id}")
    void deleteSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("GET all subcategories of the category specified by id (name)")
    @GET
    @Path("/categories/id/{category}/subcategories")
    List<SubCategoryDTO> getSubCategoriesByRootCategory(
            @ApiParam("The root category name")
            @PathParam("category")
                    String category);

    @ApiOperation("GET all subcategories with the given parent specified by id (name)")
    @GET
    @Path("/parent/{category}")
    List<SubCategoryDTO> getSubWithGivenParentByCategory(
            @ApiParam("The sub category name")
            @PathParam("category")
                    String category);
}
