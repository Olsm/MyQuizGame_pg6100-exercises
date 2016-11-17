package org.pg6100.restApi.api;

import com.google.common.base.Throwables;
import org.pg6100.quiz.businesslayer.CategoryEJB;
import org.pg6100.quiz.businesslayer.QuizEJB;
import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.quiz.datalayer.SubSubCategory;
import org.pg6100.restApi.dto.CategoryConverter;
import org.pg6100.restApi.dto.CategoryDTO;

import javax.ejb.EJB;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

public class CategoryRestImpl implements CategoryRestApi {

    @EJB
    private CategoryEJB cEJB;
    @EJB
    private QuizEJB qEJB;

    @Override
    public List<CategoryDTO> get() {
        return CategoryConverter.transformList(cEJB.getAllRootCategories());
    }

    @Override
    public String createRootCategory(CategoryDTO dto) {
        if (dto.subCategory != null)
            throw new WebApplicationException("Cannot specify sub category when creating root category");
        else if (dto.subSubCategory != null)
            throw new WebApplicationException("Cannot specify sub category when creating root category");
        else if (dto.category == null)
            throw new WebApplicationException("Category name must be specified when creating root category");

        RootCategory rootCategory;
        try {
            rootCategory = cEJB.registerRootCategory(dto.category);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return rootCategory.getCategory();
    }

    @Override
    public CategoryDTO getRootCategoryById(String category) {
        return CategoryConverter.transform(cEJB.getRootCategory(category));
    }

    @Override
    public void updateRootCategory(String category, CategoryDTO dto) {
        if (! cEJB.rootCatExists(category))
            throw new WebApplicationException("Cannot find category with name: " + category, 404);

        try {
            cEJB.updateRootCategory(category, dto.category);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void deleteRootCategory(String category) {
        cEJB.deleteRootCategory(category);
    }

    @Override
    public String createSubSubCategory(CategoryDTO dto) {
        if (dto.rootCategory != null)
            throw new WebApplicationException("Cannot specify root category when creating subsub category");
        else if (dto.subSubCategory != null)
            throw new WebApplicationException("Cannot specify subsub category when creating subsub category");
        else if (dto.subCategory == null)
            throw new WebApplicationException("Sub category must be specified when creating subsub category");
        else if (dto.category == null)
            throw new WebApplicationException("Category name must be specified when creating subsub category");

        SubSubCategory subSubCategory;
        try {
            subSubCategory = cEJB.registerSubSubCategory(dto.subCategory, dto.category);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return subSubCategory.getCategory();
    }

    @Override
    public CategoryDTO getSubSubCategoryById(String category) {
        return CategoryConverter.transform(cEJB.getSubSubCategory(category));
    }

    @Override
    public void updateSubSubCategory(String category, CategoryDTO dto) {
        if (!cEJB.subSubCatExists(category))
            throw new WebApplicationException("Cannot find category with name: " + category, 404);
        else if (!cEJB.subCatExists(dto.subCategory.getCategory()))
            throw new WebApplicationException("Cannot find sub category with name: " + category, 404);

        try {
            cEJB.updateSubSubCategory(category, dto.category, dto.subCategory.getCategory());
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void deleteSubSubCategory(String category) {
        cEJB.deleteSubSubCategory(category);
    }

    @Override
    public String createSubCategory(CategoryDTO dto) {
        if (dto.subCategory != null)
            throw new WebApplicationException("Cannot specify sub category when creating sub category");
        else if (dto.subSubCategory != null)
            throw new WebApplicationException("Cannot specify subsub category when creating sub category");
        else if (dto.rootCategory == null)
            throw new WebApplicationException("Root category must be specified when creating sub category");
        else if (dto.category == null)
            throw new WebApplicationException("Category name must be specified when creating sub category");

        SubCategory subCategory;
        try {
            subCategory = cEJB.registerSubCategory(dto.rootCategory, dto.category);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return subCategory.getCategory();
    }

    @Override
    public CategoryDTO getSubCategoryById(String category) {
        return CategoryConverter.transform(cEJB.getSubCategory(category));
    }

    @Override
    public void updateSubCategory(String category, CategoryDTO dto) {
        if (! cEJB.subCatExists(category))
            throw new WebApplicationException("Cannot find category with name: " + category, 404);
        else if (! cEJB.rootCatExists(dto.rootCategory.getCategory()))
            throw new WebApplicationException("Cannot find root category with name: " + category, 404);

        try {
            cEJB.updateSubCategory(category, dto.category, dto.rootCategory.getCategory());
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void deleteSubCategory(String category) {
        cEJB.deleteSubCategory(category);
    }

    @Override
    public List<CategoryDTO> getWithQuizes() {
        return CategoryConverter.transformList(cEJB.getRootCategoriesWithQuizes());
    }

    @Override
    public List<CategoryDTO> getSubSubWithQuizes() {
        return CategoryConverter.transformList(cEJB.getSubSubCategoriesWithQuizes());
    }

    @Override
    public List<CategoryDTO> getSubCategoriesByRootCategory(String category) {
        return CategoryConverter.transformList(cEJB.getRootCategory(category).getSubCategoryList());
    }

    @Override
    public List<CategoryDTO> getSubWithGivenParentByCategory(String category) {
        return CategoryConverter.transformList(cEJB.getSubCategory(category).getRootCategory().getSubCategoryList());
    }

    @Override
    public List<CategoryDTO> getSubSubBySubCategory(String category) {
        return CategoryConverter.transformList(cEJB.getSubCategory(category).getSubSubCategoryList());
    }

    @Override
    public List<CategoryDTO> getSubSubWithGivenSubParentByCategory(String category) {
        return CategoryConverter.transformList(cEJB.getSubSubCategory(category).getSubCategory().getSubSubCategoryList());
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
