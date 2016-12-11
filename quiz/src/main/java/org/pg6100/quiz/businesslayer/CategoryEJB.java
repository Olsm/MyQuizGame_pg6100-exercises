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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class CategoryEJB {

    @PersistenceContext
    protected EntityManager em;

    public CategoryEJB(){}

    public RootCategory registerRootCategory(String name) {
        RootCategory rootCategory = new RootCategory(name);
        em.persist(rootCategory);
        return rootCategory;
    }

    public SubCategory registerSubCategory(RootCategory rootCategory, String name) {
        rootCategory = getRootCategory(rootCategory.getId());
        SubCategory subCategory = new SubCategory(rootCategory, name);
        em.persist(subCategory);
        rootCategory.addSubCategory(subCategory);
        return subCategory;
    }

    public SubSubCategory registerSubSubCategory(SubCategory subCategory, String name) {
        subCategory = getSubCategory(subCategory.getId());
        SubSubCategory subSubCategory = new SubSubCategory(subCategory, name);
        em.persist(subSubCategory);
        subCategory.addSubSubCategory(subSubCategory);
        return subSubCategory;
    }

    public RootCategory getRootCategory(Long id) {
        return em.find(RootCategory.class, id);
    }

    public SubCategory getSubCategory(Long id) {
        return em.find(SubCategory.class, id);
    }
    public SubSubCategory getSubSubCategory(Long id) {
        return em.find(SubSubCategory.class, id);
    }

    public boolean rootCatExists(Long id) {
        return getRootCategory(id) != null;
    }
    public boolean subCatExists(Long id) {
        return getSubCategory(id) != null;
    }
    public boolean subSubCatExists(Long id) {
        return getSubSubCategory(id) != null;
    }


    public boolean updateRootCategory(@NotNull Long id, @NotNull String newCategory) {
            RootCategory rootCategory = getRootCategory(id);
            if (rootCategory == null) {
                return false;
            }
            rootCategory.setName(newCategory);
            return true;
    }

    public boolean updateSubCategory(@NotNull Long id, @NotNull String newCategory, @NotNull Long rootCategoryId) {
        RootCategory rootCat = getRootCategory(rootCategoryId);
        SubCategory subCat = getSubCategory(id);
        if (rootCat == null || subCat == null) {
            return false;
        }
        subCat.setName(newCategory);
        subCat.setRootCategory(rootCat);
        return true;
    }

    public boolean updateSubSubCategory(@NotNull Long id, @NotNull String newName, @NotNull Long newSubCategoryId) {
        SubCategory subCat = getSubCategory(newSubCategoryId);
        SubSubCategory subSubCat = getSubSubCategory(id);
        if (subCat == null || subSubCat == null) {
            return false;
        }
        subSubCat.setName(newName);
        subSubCat.setSubCategory(subCat);
        return true;
    }

    public void deleteRootCategory(@NotNull Long id) {
        em.remove(getRootCategory(id));
    }

    public void deleteSubCategory(@NotNull Long id) {
        SubCategory subCategory = getSubCategory(id);
        getRootCategory(subCategory.getRootCategory().getId()).removeSubCategory(subCategory);
        em.remove(subCategory);
    }

    public void deleteSubSubCategory(@NotNull Long id) {
        SubSubCategory subSubCategory = getSubSubCategory(id);
        getSubCategory(subSubCategory.getSubCategory().getId()).removeSubSubCategory(subSubCategory);
        em.remove(subSubCategory);
    }

    public Set<RootCategory> getAllRootCategories() {
        Query query = em.createQuery("Select c FROM RootCategory c");
        return new HashSet<>(query.getResultList());
    }

    public Set<SubCategory> getAllSubCategories() {
        Query query = em.createQuery("Select c FROM SubCategory c");
        return new HashSet<>(query.getResultList());
    }

    public Set<SubSubCategory> getAllSubSubCategories() {
        Query query = em.createQuery("Select c FROM SubSubCategory c");
        return new HashSet<>(query.getResultList());
    }

    public Set<RootCategory> getRootCategoriesWithQuizes() {
        Set<RootCategory> categories = getAllRootCategories();
        Set<RootCategory> categoriesWithQuizes = new HashSet<>();

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

    public Set<SubSubCategory> getSubSubCategoriesWithQuizes() {
        Set<SubSubCategory> subSubCategories = getAllSubSubCategories();
        Set<SubSubCategory> subSubCategoriesWithQuizes = new HashSet<>();

        for (SubSubCategory category : subSubCategories) {
            if (category.getQuizList().size() > 0) {
                subSubCategoriesWithQuizes.add(category);
            }
        }

        return subSubCategoriesWithQuizes;
    }
}
