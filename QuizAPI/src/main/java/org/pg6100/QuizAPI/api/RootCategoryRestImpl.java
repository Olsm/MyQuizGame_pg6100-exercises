package org.pg6100.QuizAPI.api;

import com.google.common.base.Throwables;
import org.pg6100.quiz.businesslayer.CategoryEJB;
import org.pg6100.quiz.businesslayer.QuizEJB;
import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.QuizAPI.dto.CategoryConverter;
import org.pg6100.QuizAPI.dto.RootCategoryDTO;
import org.pg6100.QuizAPI.dto.SubCategoryDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Set;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class RootCategoryRestImpl implements RootCategoryRestApi {

    @EJB
    private CategoryEJB cEJB;
    @EJB
    private QuizEJB qEJB;

    @Override
    public Set<RootCategoryDTO> get(boolean withQuizes) {
        if (withQuizes)
            return CategoryConverter.transformCategories(cEJB.getRootCategoriesWithQuizes());
        else
            return CategoryConverter.transformCategories(cEJB.getAllRootCategories());
    }

    @Override
    public RootCategoryDTO getRootCategoryById(String name) {
        requireRootCategory(name);
        return CategoryConverter.transform(cEJB.getRootCategory(name));
    }

    @Override
    public Set<SubCategoryDTO> getSubCategoriesByRootCategory(String name) {
        requireRootCategory(name);
        return CategoryConverter.transformSubCategories(cEJB.getRootCategory(name).getSubCategoryList());
    }

    @Override
    public String createRootCategory(RootCategoryDTO dto) {
        if (dto.name == null)
            throw new WebApplicationException("Category name must be specified when creating root category");

        RootCategory rootCategory;
        try {
            rootCategory = cEJB.registerRootCategory(dto.name);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return rootCategory.getName();
    }

    @Override
    public void updateRootCategory(String name, RootCategoryDTO dto) {
        if (! cEJB.rootCatExists(name))
            throw new WebApplicationException("Cannot find category with name: " + name, 404);

        try {
            cEJB.updateRootCategory(name, dto.name);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void deleteRootCategory(String name) {
        cEJB.deleteRootCategory(name);
    }

    //----------------------------------------------------------

    private void requireRootCategory(String name) throws WebApplicationException {
        if (!cEJB.rootCatExists(name)) {
            throw new WebApplicationException("Cannot find root category: " + name, 404);
        }
    }

    private WebApplicationException wrapException(Exception e) throws WebApplicationException{

        /*
            Errors:
            4xx: the user has done something wrong, eg asking for something that does not exist (404)
            5xx: internal server error (eg, could be a bug in the code)
         */

        Throwable cause = Throwables.getRootCause(e);
        if(cause instanceof ConstraintViolationException){
            return new WebApplicationException("Invalid constraints on input: "+cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }


    /* Deprecated methods */

    @Override
    public Response deprecatedGetRootCategoryById(String name) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories")
                        .queryParam("id", name).build())
                .build();
    }

    @Override
    public Response deprecatedGetWithQuizes() {
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories")
                        .queryParam("withQuizes").build())
                .build();
    }

    @Override
    public Response deprecatedGetSubCategoriesByRootCategory(String name) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories/" + name + "/subcategories").build())
                .build();
    }
}
