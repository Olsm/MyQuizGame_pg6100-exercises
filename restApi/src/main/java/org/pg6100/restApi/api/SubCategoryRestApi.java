package org.pg6100.restApi.api;

import io.swagger.annotations.*;
import org.pg6100.restApi.dto.RootCategoryDTO;
import org.pg6100.restApi.dto.SubCategoryDTO;
import org.pg6100.restApi.dto.SubSubCategoryDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Api(value = "/subcategories", description = "Handling of creating and retrieving sub categories")
@Path("/subcategories")
@Produces(MediaType.APPLICATION_JSON)
public interface SubCategoryRestApi {

    String ID_PARAM = "The numeric id of the category";

    @ApiOperation("Get all the sub categories")
    @GET
    Set<SubCategoryDTO> get();

    @ApiOperation("Create a subcategory")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new subcategory")
    String createSubCategory(
            @ApiParam("Category name and root category")
                    SubCategoryDTO dto);

    @ApiOperation("Get subcategory by id (name)")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetSubCategoryById(
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

    @ApiOperation("GET all subcategories with the given parent specified by id (name)")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/parent/{id}")
    @Deprecated
    Response deprecatedGetSubWithGivenParentByCategory(
            @ApiParam("The root category name")
            @PathParam("id")
                    String name);
}
