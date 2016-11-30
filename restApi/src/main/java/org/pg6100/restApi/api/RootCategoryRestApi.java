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

@Api(value = "/categories", description = "Handling of creating and retrieving categories")
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public interface RootCategoryRestApi {

    String ID_PARAM = "The numeric id of the category";

    @ApiOperation("Get all the categories")
    @GET
    Set<RootCategoryDTO> get(
            @ApiParam("Root categories with quizes")
            @QueryParam("withQuizes")
                    boolean withQuizes
    );

    @ApiOperation("Get category by id (name)")
    @GET
    @Path("/{id}")
    RootCategoryDTO getRootCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("Create a category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new category")
    String createRootCategory(
            @ApiParam("Category name")
                    RootCategoryDTO dto);

    @ApiOperation("Update category by id (name)")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateRootCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String name,
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

    @ApiOperation("GET all subcategories of the category specified by id (name)")
    @GET
    @Path("/{id}/subcategories")
    Set<SubCategoryDTO> getSubCategoriesByRootCategory(
            @ApiParam("The root category name")
            @PathParam("id")
                    String name);


    /* Deprecated methods */

    @ApiOperation("Get category by id (name)")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetRootCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String category);

    @ApiOperation("all categories that have at least one subcategory with at least one subsubcategory with at least one quiz.")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/withQuizzes")
    @Deprecated
    Response deprecatedGetWithQuizes();

    @ApiOperation("GET all subcategories of the category specified by id (name)")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/id/{id}/subcategories")
    @Deprecated
    Response deprecatedGetSubCategoriesByRootCategory(
            @ApiParam("The root category name")
            @PathParam("id")
                    String name);
}
