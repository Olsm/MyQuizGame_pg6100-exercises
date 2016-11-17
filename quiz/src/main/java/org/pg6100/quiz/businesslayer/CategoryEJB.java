package org.pg6100.quiz.businesslayer;

import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.quiz.datalayer.SubSubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Stateless
public class CategoryEJB {

    @PersistenceContext
    protected EntityManager em;

    public CategoryEJB(){}

    public RootCategory registerRootCategory(String category) {
        RootCategory rootCategory = new RootCategory(category);
        em.persist(rootCategory);
        return rootCategory;
    }

    public SubCategory registerSubCategory(RootCategory rootCategory, String category) {
        SubCategory subCategory = new SubCategory(rootCategory, category);
        em.persist(subCategory);
        return subCategory;
    }

    public SubSubCategory registerSubSubCategory(SubCategory subCategory, String category) {
        SubSubCategory subSubCategory = new SubSubCategory(subCategory, category);
        em.persist(subSubCategory);
        return subSubCategory;
    }

    public RootCategory getRootCategory(String category) {
        Query query = em.createQuery("SELECT c FROM RootCategory c where c.category = :id");
        query.setParameter("id", category);
        return (RootCategory) query.getSingleResult();
    }

    public SubCategory getSubCategory(String category) {
        Query query = em.createQuery("SELECT c FROM SubCategory c where c.category = :id");
        query.setParameter("id", category);
        return (SubCategory) query.getSingleResult();
    }
    public SubSubCategory getSubSubCategory(String category) {
        Query query = em.createQuery("SELECT c FROM SubSubCategory c where c.category = :id");
        query.setParameter("id", category);
        return (SubSubCategory) query.getSingleResult();
    }

    public boolean rootCatExists(String category) {
        return em.find(RootCategory.class, category) != null;
    }
    public boolean subCatExists(String category) {
        return em.find(SubCategory.class, category) != null;
    }
    public boolean subSubCatExists(String category) {
        return em.find(SubSubCategory.class, category) != null;
    }


    public boolean updateRootCategory(@NotNull String category, @NotNull String newCategory) {
            RootCategory rootCategory = getRootCategory(category);
            if (rootCategory == null) {
                return false;
            }
            rootCategory.setCategory(newCategory);
            return true;
    }

    public boolean updateSubCategory(@NotNull String category, @NotNull String newCategory, @NotNull String rootCategory) {
        RootCategory rootCat = getRootCategory(rootCategory);
        SubCategory subCat = getSubCategory(category);
        if (rootCat == null || subCat == null) {
            return false;
        }
        subCat.setCategory(newCategory);
        subCat.setRootCategory(rootCat);
        return true;
    }

    public boolean updateSubSubCategory(@NotNull String category, @NotNull String newCategory, @NotNull String subCategory) {
        SubCategory subCat = getSubCategory(subCategory);
        SubSubCategory subSubCat = getSubSubCategory(category);
        if (subCat == null || subSubCat == null) {
            return false;
        }
        subSubCat.setCategory(newCategory);
        subSubCat.setSubCategory(subCat);
        return true;
    }

    public void deleteRootCategory(@NotNull String category) {
        em.remove(getRootCategory(category));
    }

    public void deleteSubCategory(@NotNull String category) {
        em.remove(getSubCategory(category));
    }

    public void deleteSubSubCategory(@NotNull String category) {
        em.remove(getSubSubCategory(category));
    }

    public List<RootCategory> getAllRootCategories() {
        Query query = em.createQuery("Select c FROM RootCategory c");
        return (List<RootCategory>) query.getResultList();
    }

    public List<SubCategory> getAllSubCategories() {
        Query query = em.createQuery("Select c FROM SubCategory c");
        return (List<SubCategory>) query.getResultList();
    }

    public List<SubSubCategory> getAllSubSubCategories() {
        Query query = em.createQuery("Select c FROM SubSubCategory c");
        return (List<SubSubCategory>) query.getResultList();
    }

    public List<RootCategory> getRootCategoriesWithQuizes() {
        List<RootCategory> categories = getAllRootCategories();
        List<RootCategory> categoriesWithQuizes = new ArrayList<>();

        for (RootCategory rootCategory : categories) {
            for (SubCategory subCategory : rootCategory.getSubCategoryList()) {
                for (SubSubCategory subSubCategory : subCategory.getSubSubCategoryList()) {
                    if (subSubCategory.getQuizList().size() > 0) {
                        categoriesWithQuizes.add(rootCategory);
                    }
                }
            }
        }

        return categoriesWithQuizes;
    }

    public List<SubSubCategory> getSubSubCategoriesWithQuizes() {
        List<SubSubCategory> subSubCategories = getAllSubSubCategories();
        List<SubSubCategory> subSubCategoriesWithQuizes = new ArrayList<>();

        for (SubSubCategory category : subSubCategories) {
            if (category.getQuizList().size() > 0) {
                subSubCategoriesWithQuizes.add(category);
            }
        }

        return subSubCategoriesWithQuizes;
    }
}
