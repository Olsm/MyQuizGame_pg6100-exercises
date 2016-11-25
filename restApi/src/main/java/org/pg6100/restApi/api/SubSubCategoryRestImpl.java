package org.pg6100.restApi.api;

import com.google.common.base.Throwables;
import org.pg6100.quiz.businesslayer.CategoryEJB;
import org.pg6100.quiz.businesslayer.QuizEJB;
import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.quiz.datalayer.SubSubCategory;
import org.pg6100.restApi.dto.CategoryConverter;
import org.pg6100.restApi.dto.RootCategoryDTO;
import org.pg6100.restApi.dto.SubCategoryDTO;
import org.pg6100.restApi.dto.SubSubCategoryDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.Set;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class SubSubCategoryRestImpl implements SubSubCategoryRestApi {

    @EJB
    private CategoryEJB cEJB;
    @EJB
    private QuizEJB qEJB;

    @Override
    public Set<SubSubCategoryDTO> get() {
        return CategoryConverter.transformSubSubCategories(cEJB.getAllSubSubCategories());
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
    public SubSubCategoryDTO getSubSubCategoryById(String name) {
        return CategoryConverter.transform(cEJB.getSubSubCategory(name));
    }

    @Override
    public void updateSubSubCategory(String name, SubSubCategoryDTO dto) {
        if (!cEJB.subSubCatExists(name))
            throw new WebApplicationException("Cannot find category with name: " + name, 404);
        else if (!cEJB.subCatExists(dto.subCategoryName))
            throw new WebApplicationException("Cannot find sub category with name: " + name, 404);

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
    public Set<SubSubCategoryDTO> getSubSubWithQuizes() {
        return CategoryConverter.transformSubSubCategories(cEJB.getSubSubCategoriesWithQuizes());
    }

    @Override
    public Set<SubSubCategoryDTO> getSubSubBySubCategory(String name) {
        return CategoryConverter.transformSubSubCategories(cEJB.getSubCategory(name).getSubSubCategoryList());
    }

    @Override
    public Set<SubSubCategoryDTO> getSubSubWithGivenSubParentByCategory(String name) {
        if (!cEJB.subSubCatExists(name))
            throw new WebApplicationException("Cannot find category with name: " + name, 404);
        return CategoryConverter.transformSubSubCategories(cEJB.getSubSubCategory(name).getSubCategory().getSubSubCategoryList());
    }

    //----------------------------------------------------------

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
}
