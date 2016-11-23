package org.pg6100.restApi.api;

import com.google.common.base.Throwables;
import org.pg6100.quiz.businesslayer.CategoryEJB;
import org.pg6100.quiz.businesslayer.QuizEJB;
import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.restApi.dto.CategoryConverter;
import org.pg6100.restApi.dto.RootCategoryDTO;
import org.pg6100.restApi.dto.SubCategoryDTO;

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
public class RootCategoryRestImpl implements RootCategoryRestApi {

    @EJB
    private CategoryEJB cEJB;
    @EJB
    private QuizEJB qEJB;

    @Override
    public Set<RootCategoryDTO> get() {
        return CategoryConverter.transformCategories(cEJB.getAllRootCategories());
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
    public RootCategoryDTO getRootCategoryById(String name) {
        return CategoryConverter.transform(cEJB.getRootCategory(name));
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

    @Override
    public Set<RootCategoryDTO> getWithQuizes() {
        return CategoryConverter.transformCategories(cEJB.getRootCategoriesWithQuizes());
    }

    @Override
    public Set<SubCategoryDTO> getSubCategoriesByRootCategory(String name) {
        return CategoryConverter.transformSubCategories(cEJB.getRootCategory(name).getSubCategoryList());
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
