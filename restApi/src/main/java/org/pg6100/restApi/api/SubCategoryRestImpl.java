package org.pg6100.restApi.api;

import com.google.common.base.Throwables;
import org.pg6100.quiz.businesslayer.CategoryEJB;
import org.pg6100.quiz.businesslayer.QuizEJB;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.restApi.dto.CategoryConverter;
import org.pg6100.restApi.dto.SubCategoryDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class SubCategoryRestImpl implements SubCategoryRestApi {

    @EJB
    private CategoryEJB cEJB;
    @EJB
    private QuizEJB qEJB;

    @Override
    public Set<SubCategoryDTO> get() {
        return CategoryConverter.transformSubCategories(cEJB.getAllSubCategories());
    }

    @Override
    public SubCategoryDTO getSubCategoryById(String name) {
        requireSubCategory(name);
        return CategoryConverter.transform(cEJB.getSubCategory(name));
    }

    @Override
    public String createSubCategory(SubCategoryDTO dto) {
        if (dto.rootCategoryName == null)
            throw new WebApplicationException("Root category must be specified when creating sub category");
        else if (dto.name == null)
            throw new WebApplicationException("Category name must be specified when creating sub category");

        SubCategory subCategory;
        try {
            subCategory = cEJB.registerSubCategory(cEJB.getRootCategory(dto.rootCategoryName), dto.name);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return subCategory.getName();
    }

    @Override
    public void updateSubCategory(String name, SubCategoryDTO dto) {
        requireRootCategory(dto.rootCategoryName);
        requireSubCategory(name);

        try {
            cEJB.updateSubCategory(name, dto.name, dto.rootCategoryName);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void deleteSubCategory(String name) {
        cEJB.deleteSubCategory(name);
    }

    //----------------------------------------------------------

    private void requireRootCategory(String name) throws WebApplicationException {
        if (!cEJB.rootCatExists(name)) {
            throw new WebApplicationException("Cannot find root category: " + name, 404);
        }
    }

    private void requireSubCategory(String name) throws WebApplicationException {
        if (!cEJB.subCatExists(name)) {
            throw new WebApplicationException("Cannot find sub category: " + name, 404);
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
    public Response deprecatedGetSubCategoryById(String name) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories")
                        .queryParam("id", name).build())
                .build();
    }

    @Override
    public Response deprecatedGetSubWithGivenParentByCategory(String name) {
        return Response.status(301)
                .location(UriBuilder.fromUri("categories")
                        .queryParam("id", name).uri("subcategories").build())
                .build();
    }
}
