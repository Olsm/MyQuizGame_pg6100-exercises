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
    public RootCategoryDTO getRootCategoryById(Long id) {
        requireRootCategory(id);
        return CategoryConverter.transform(cEJB.getRootCategory(id));
    }

    @Override
    public Set<SubCategoryDTO> getSubCategoriesByRootCategory(Long id) {
        requireRootCategory(id);
        return CategoryConverter.transformSubCategories(cEJB.getRootCategory(id).getSubCategoryList());
    }

    @Override
    public Long createRootCategory(RootCategoryDTO dto) {
        if (dto.name == null)
            throw new WebApplicationException("Category name must be specified when creating root category");

        RootCategory rootCategory;
        try {
            rootCategory = cEJB.registerRootCategory(dto.name);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return rootCategory.getId();
    }

    @Override
    public void updateRootCategory(Long id, RootCategoryDTO dto) {
        if (! cEJB.rootCatExists(id))
            throw new WebApplicationException("Cannot find category with id " + id, 404);

        try {
            cEJB.updateRootCategory(id, dto.name);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void deleteRootCategory(Long id) {
        cEJB.deleteRootCategory(id);
    }

    //----------------------------------------------------------

    private void requireRootCategory(Long id) throws WebApplicationException {
        if (!cEJB.rootCatExists(id)) {
            throw new WebApplicationException("Cannot find root category with id " + id, 404);
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
    public Response deprecatedGetRootCategoryById(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories")
                        .queryParam("id", id).build())
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
    public Response deprecatedGetSubCategoriesByRootCategory(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories/" + id + "/subcategories").build())
                .build();
    }
}
