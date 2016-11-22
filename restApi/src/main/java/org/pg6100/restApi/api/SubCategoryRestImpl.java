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

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class SubCategoryRestImpl implements SubCategoryRestApi {

    @EJB
    private CategoryEJB cEJB;
    @EJB
    private QuizEJB qEJB;

    @Override
    public List<SubCategoryDTO> get() {
        return CategoryConverter.transformSubCategories(cEJB.getAllSubCategories());
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
    public SubCategoryDTO getSubCategoryById(String name) {
        return CategoryConverter.transform(cEJB.getSubCategory(name));
    }

    @Override
    public void updateSubCategory(String name, SubCategoryDTO dto) {
        if (! cEJB.subCatExists(name))
            throw new WebApplicationException("Cannot find category with name: " + name, 404);
        else if (! cEJB.rootCatExists(dto.rootCategoryName))
            throw new WebApplicationException("Cannot find root category with name: " + name, 404);

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

    @Override
    public List<SubCategoryDTO> getSubCategoriesByRootCategory(String name) {
        return CategoryConverter.transformSubCategories(cEJB.getRootCategory(name).getSubCategoryList());
    }

    @Override
    public List<SubCategoryDTO> getSubWithGivenParentByCategory(String name) {
        return CategoryConverter.transformSubCategories(cEJB.getSubCategory(name).getRootCategory().getSubCategoryList());
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
