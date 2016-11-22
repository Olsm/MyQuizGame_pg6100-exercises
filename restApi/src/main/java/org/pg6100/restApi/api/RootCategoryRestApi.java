package org.pg6100.restApi.api;

import io.swagger.annotations.*;
import org.pg6100.restApi.dto.RootCategoryDTO;
import org.pg6100.restApi.dto.SubCategoryDTO;
import org.pg6100.restApi.dto.SubSubCategoryDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/categories", description = "Handling of creating and retrieving categories")
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public interface RootCategoryRestApi {

    String ID_PARAM = "The numeric id of the category";

    @ApiOperation("Get all the categories")
    @GET
    List<RootCategoryDTO> get();

    @ApiOperation("Create a category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new category")
    String createRootCategory(
            @ApiParam("Category name")
                    RootCategoryDTO dto);

    @ApiOperation("Get category by id (name)")
    @GET
    @Path("/id/{id}")
    RootCategoryDTO getRootCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("Update category by id (name)")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateRootCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category,
            @ApiParam("The category that will replace the old one")
                    RootCategoryDTO dto);

    /* TODO: Patch
    @ApiOperation("")
    @PATCH
    @Path("/subcategories/id/{id}");
    */

    @ApiOperation("Delete a category with given id (name)")
    @DELETE
    @Path("/id/{id}")
    void deleteRootCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String name);

    @ApiOperation("all categories that have at least one subcategory with at least one subsubcategory with at least one quiz.")
    @GET
    @Path("/categories/withQuizzes")
    List<RootCategoryDTO> getWithQuizes();
}
