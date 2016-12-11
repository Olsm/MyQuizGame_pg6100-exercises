package org.pg6100.QuizAPI.api;

import io.swagger.annotations.*;
import org.pg6100.QuizAPI.dto.SubSubCategoryDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Api(value = "/subsubcategories", description = "Handling of creating and retrieving sub sub categories")
@Path("/subsubcategories")
@Produces(MediaType.APPLICATION_JSON)
public interface SubSubCategoryRestApi {

    String ID_PARAM = "The numeric id of the category";

    @ApiOperation("Get all the sub sub categories")
    @GET
    Set<SubSubCategoryDTO> get(
            @ApiParam("Root categories with quizes")
            @QueryParam("withQuizes")
                    boolean withQuizes
    );

    @ApiOperation("Get subsub category by id")
    @GET
    @Path("/{id}")
    SubSubCategoryDTO getSubSubCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id);

    @ApiOperation("Create a subsub category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new subsub category")
    Long createSubSubCategory(
            @ApiParam("Category name and sub category")
                    SubSubCategoryDTO dto);

    @ApiOperation("Update subsub category by id")
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateSubSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,
            @ApiParam("The subsub category that will replace the old one")
                    SubSubCategoryDTO dto);

    /* TODO: Patch
    @ApiOperation("")
    @PATCH
    @Path("/id/{id}");
    */

    @ApiOperation("Delete a subsub category with given id")
    @DELETE
    @Path("/id/{id}")
    void deleteSubSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id);

    @ApiOperation("GET all subsubcategories of the subcategory specified by id")
    @GET
    @Path("/{id}/subsubcategories")
    Set<SubSubCategoryDTO> getSubSubBySubCategory(
            @ApiParam("The sub category id")
            @PathParam("id")
                    Long id);


    /* Deprecated methods */

    @ApiOperation("Get subsub category by id")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetSubSubCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id);

    @ApiOperation("GET all subsubcategories of the subcategory specified by id")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/id/{id}/subsubcategories")
    @Deprecated
    Response deprecatedGetSubSubBySubCategory(
            @ApiParam("The sub category id")
            @PathParam("id")
                    Long id);

    @ApiOperation("GET all subsubcategories with the given subcategory parent specified by id")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/parent/{id}")
    @Deprecated
    Response deprecatedGetSubSubWithGivenSubParentByCategory(
            @ApiParam("The subsub category id")
            @PathParam("id")
                    Long id);


    @ApiOperation("Get all subsubcategories with at least one quiz")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Deprecated
    @Path("/withQuizzes/subsubcategories")
    Response deprecatedGetSubSubWithQuizes();
}
