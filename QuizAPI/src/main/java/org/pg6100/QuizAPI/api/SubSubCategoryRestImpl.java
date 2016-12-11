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
    public SubSubCategoryDTO getSubSubCategoryById(Long id) {
        requireSubSubCategory(id);
        return CategoryConverter.transform(cEJB.getSubSubCategory(id));
    }

    @Override
    public Long createSubSubCategory(SubSubCategoryDTO dto) {
        if (dto.subCategoryId == null)
            throw new WebApplicationException("Sub category must be specified when creating subsub category");
        else if (dto.name == null)
            throw new WebApplicationException("Category name must be specified when creating subsub category");

        SubSubCategory subSubCategory;
        try {
            long subCategoryId = parseId(dto.subCategoryId);
            subSubCategory = cEJB.registerSubSubCategory(cEJB.getSubCategory(subCategoryId), dto.name);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return subSubCategory.getId();
    }

    @Override
    public void updateSubSubCategory(Long id, SubSubCategoryDTO dto) {
        long subCategoryId = parseId(dto.subCategoryId);
        requireSubCategory(subCategoryId);
        requireSubSubCategory(id);

        try {
            cEJB.updateSubSubCategory(id, dto.name, subCategoryId);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void deleteSubSubCategory(Long id) {
        cEJB.deleteSubSubCategory(id);
    }

    @Override
    public Set<SubSubCategoryDTO> getSubSubBySubCategory(Long id) {
        requireSubCategory(id);
        return CategoryConverter.transformSubSubCategories(cEJB.getSubCategory(id).getSubSubCategoryList());
    }

    //----------------------------------------------------------

    private void requireSubCategory(Long id) throws WebApplicationException {
        if (!cEJB.subCatExists(id)) {
            throw new WebApplicationException("Cannot find sub category with id " + id, 404);
        }
    }

    private void requireSubSubCategory(Long id) throws WebApplicationException {
        if (!cEJB.subCatExists(id)) {
            throw new WebApplicationException("Cannot find sub category with id " + id, 404);
        }
    }

    private long parseId(String id) {
        try{
            return Long.parseLong(id);
        } catch (Exception e){
            throw new WebApplicationException("Invalid id: " + id, 400);
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
    public Response deprecatedGetSubSubCategoryById(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subsubcategories")
                        .queryParam("id", id).build())
                .build();
    }

    @Override
    public Response deprecatedGetSubSubBySubCategory(Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories")
                        .queryParam("id", id)
                        .uri("subsubcategories").build())
                .build();
    }

    @Override
    public Response deprecatedGetSubSubWithGivenSubParentByCategory(Long id) {
        if (!cEJB.subSubCatExists(id))
            throw new WebApplicationException("Cannot find category with id " + id, 404);
        return Response.status(301)
                .location(UriBuilder.fromUri("subcategories")
                        .queryParam("id", id).uri("subsubcategories").build())
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
