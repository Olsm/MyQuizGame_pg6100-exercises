package org.pg6100.QuizAPI.api;

import io.swagger.annotations.*;
import org.pg6100.QuizAPI.dto.RootCategoryDTO;
import org.pg6100.QuizAPI.dto.SubCategoryDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @ApiOperation("Get category by id)")
    @GET
    @Path("/{id}")
    RootCategoryDTO getRootCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id);

    @ApiOperation("Create a category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new category")
    Long createRootCategory(
            @ApiParam("Category name")
                    RootCategoryDTO dto);

    @ApiOperation("Update category by id")
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateRootCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,
            @ApiParam("The category that will replace the old one")
                    RootCategoryDTO dto);

    /* TODO: Patch
    @ApiOperation("")
    @PATCH
    @Path("/subcategories/id/{id}");
    */

    @ApiOperation("Delete a category with given id (name)")
    @DELETE
    @Path("/{id}")
    void deleteRootCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id);

    @ApiOperation("GET all subcategories of the category specified by id")
    @GET
    @Path("/{id}/subcategories")
    Set<SubCategoryDTO> getSubCategoriesByRootCategory(
            @ApiParam("The root category id")
            @PathParam("id")
                    Long id);


    /* Deprecated methods */

    @ApiOperation("Get category by id")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetRootCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id);

    @ApiOperation("all categories that have at least one subcategory with at least one subsubcategory with at least one quiz.")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/withQuizzes")
    @Deprecated
    Response deprecatedGetWithQuizes();

    @ApiOperation("GET all subcategories of the category specified by id")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/id/{id}/subcategories")
    @Deprecated
    Response deprecatedGetSubCategoriesByRootCategory(
            @ApiParam("The root category id")
            @PathParam("id")
                    Long id);
}
