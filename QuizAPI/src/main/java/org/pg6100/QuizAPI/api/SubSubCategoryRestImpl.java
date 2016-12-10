package org.pg6100.QuizAPI.api;

import com.google.common.base.Throwables;
import org.pg6100.quiz.businesslayer.CategoryEJB;
import org.pg6100.quiz.businesslayer.QuizEJB;
import org.pg6100.quiz.datalayer.SubSubCategory;
import org.pg6100.QuizAPI.dto.CategoryConverter;
import org.pg6100.QuizAPI.dto.SubSubCategoryDTO;

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
public class SubSubCategoryRestImpl implements SubSubCategoryRestApi {

    @EJB
    private CategoryEJB cEJB;
    @EJB
    private QuizEJB qEJB;

    @Override
    public Set<SubSubCategoryDTO> get(boolean withQuizes) {
        if (withQuizes)
            return CategoryConverter.transformSubSubCategories(cEJB.getSubSubCategoriesWithQuizes());
        else
            return CategoryConverter.transformSubSubCategories(cEJB.getAllSubSubCategories());
    }

    @Override
    public SubSubCategoryDTO getSubSubCategoryById(String name) {
        requireSubSubCategory(name);
        return CategoryConverter.transform(cEJB.getSubSubCategory(name));
    }

    @Override
    public String createSubSubCategory(SubSubCategoryDTO dto) {
        if (dto.subCategoryName == null)
            throw new WebApplicationException("Sub category must be specified when creating subsub category");
        else if (dto.name == null)
            throw new WebApplicationException("Category name must be specified when creating subsub category");

        SubSubCategory subSubCategory;
        try {
            subSubCategory = cEJB.registerSubSubCategory(cEJB.getSubCategory(dto.subCategoryName), dto.name);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return subSubCategory.getName();
    }

    @Override
    public void updateSubSubCategory(String name, SubSubCategoryDTO dto) {
        requireSubCategory(dto.subCategoryName);
        requireSubSubCategory(name);

        try {
            cEJB.updateSubSubCategory(name, dto.name, dto.subCategoryName);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void deleteSubSubCategory(String name) {
        cEJB.deleteSubSubCategory(name);
    }

    @Override
    public Set<SubSubCategoryDTO> getSubSubBySubCategory(String name) {
        requireSubCategory(name);
        return CategoryConverter.transformSubSubCategories(cEJB.getSubCategory(name).getSubSubCategoryList());
    }

    //----------------------------------------------------------

    private void requireSubCategory(String name) throws WebApplicationException {
        if (!cEJB.subCatExists(name)) {
            throw new WebApplicationException("Cannot find sub category: " + name, 404);
        }
    }

    private void requireSubSubCategory(String name) throws WebApplicationException {
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
    public Response deprecatedGetSubSubCategoryById(String name) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subsubcategories")
                        .queryParam("id", name).build())
                .build();
    }

    @Override
    public Response deprecatedGetSubSubBySubCategory(String name) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories")
                        .queryParam("id", name)
                        .uri("subsubcategories").build())
                .build();
    }

    @Override
    public Response deprecatedGetSubSubWithGivenSubParentByCategory(String name) {
        if (!cEJB.subSubCatExists(name))
            throw new WebApplicationException("Cannot find category with name: " + name, 404);
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories")
                        .queryParam("id", name).uri("subsubcategories").build())
                .build();
    }

    @Override
    public Response deprecatedGetSubSubWithQuizes() {
        return Response.status(301)
                .location(UriBuilder.fromUri("subsubcategories")
                        .queryParam("withQuizes").build())
                .build();
    }
}
