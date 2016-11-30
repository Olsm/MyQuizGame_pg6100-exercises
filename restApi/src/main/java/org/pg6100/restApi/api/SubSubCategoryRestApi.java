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

@Api(value = "/subsubcategories", description = "Handling of creating and retrieving sub sub categories")
@Path("/subsubcategories")
@Produces(MediaType.APPLICATION_JSON)
public interface SubSubCategoryRestApi {

    String ID_PARAM = "The numeric id of the category";

    @ApiOperation("Get all the sub sub categories")
    @GET
    Set<SubSubCategoryDTO> get();

    @ApiOperation("Create a subsub category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the new subsub category")
    String createSubSubCategory(
            @ApiParam("Category name and sub category")
                    SubSubCategoryDTO dto);

    @ApiOperation("Get subsub category by id (name)")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetSubSubCategoryById(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String name);

    @ApiOperation("Update subsub category by id (name)")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateSubSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    String name,
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
                    String name);

    @ApiOperation("Get all subsubcategories with at least one quiz")
    @GET
    @Path("/withQuizzes/subsubcategories")
    Set<SubSubCategoryDTO> getSubSubWithQuizes();

    @ApiOperation("GET all subsubcategories of the subcategory specified by id (name)")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/id/{id}/subsubcategories")
    @Deprecated
    Response deprecatedGetSubSubBySubCategory(
            @ApiParam("The sub category name")
            @PathParam("id")
                    String name);

    @ApiOperation("GET all subsubcategories with the given subcategory parent specified by id (name)")
    @ApiResponses({@ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")})
    @GET
    @Path("/parent/{id}")
    @Deprecated
    Response deprecatedGetSubSubWithGivenSubParentByCategory(
            @ApiParam("The subsub category name")
            @PathParam("id")
                    String name);
}
