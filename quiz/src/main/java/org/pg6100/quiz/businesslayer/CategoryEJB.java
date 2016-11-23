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
        SubCategory subCategory = new SubCategory(rootCategory, name);
        em.persist(subCategory);
        rootCategory.addSubCategory(subCategory);
        return subCategory;
    }

    public SubSubCategory registerSubSubCategory(SubCategory subCategory, String name) {
        SubSubCategory subSubCategory = new SubSubCategory(subCategory, name);
        em.persist(subSubCategory);
        subCategory.addSubSubCategory(subSubCategory);
        return subSubCategory;
    }

    public RootCategory getRootCategory(String name) {
        Query query = em.createQuery("SELECT c FROM RootCategory c where c.name = :id");
        query.setParameter("id", name);
        return (RootCategory) query.getSingleResult();
    }

    public SubCategory getSubCategory(String name) {
        Query query = em.createQuery("SELECT c FROM SubCategory c where c.name = :id");
        query.setParameter("id", name);
        return (SubCategory) query.getSingleResult();
    }
    public SubSubCategory getSubSubCategory(String name) {
        Query query = em.createQuery("SELECT c FROM SubSubCategory c where c.name = :id");
        query.setParameter("id", name);
        return (SubSubCategory) query.getSingleResult();
    }

    public boolean rootCatExists(String name) {
        return em.find(RootCategory.class, name) != null;
    }
    public boolean subCatExists(String name) {
        return em.find(SubCategory.class, name) != null;
    }
    public boolean subSubCatExists(String name) {
        return em.find(SubSubCategory.class, name) != null;
    }


    public boolean updateRootCategory(@NotNull String name, @NotNull String newCategory) {
            RootCategory rootCategory = getRootCategory(name);
            if (rootCategory == null) {
                return false;
            }
            rootCategory.setName(newCategory);
            return true;
    }

    public boolean updateSubCategory(@NotNull String name, @NotNull String newCategory, @NotNull String rootCategory) {
        RootCategory rootCat = getRootCategory(rootCategory);
        SubCategory subCat = getSubCategory(name);
        if (rootCat == null || subCat == null) {
            return false;
        }
        subCat.setName(newCategory);
        subCat.setRootCategory(rootCat);
        return true;
    }

    public boolean updateSubSubCategory(@NotNull String name, @NotNull String newCategory, @NotNull String subCategory) {
        SubCategory subCat = getSubCategory(subCategory);
        SubSubCategory subSubCat = getSubSubCategory(name);
        if (subCat == null || subSubCat == null) {
            return false;
        }
        subSubCat.setName(newCategory);
        subSubCat.setSubCategory(subCat);
        return true;
    }

    public void deleteRootCategory(@NotNull String name) {
        em.remove(getRootCategory(name));
    }

    public void deleteSubCategory(@NotNull String name) {
        em.remove(getSubCategory(name));
    }

    public void deleteSubSubCategory(@NotNull String name) {
        em.remove(getSubSubCategory(name));
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
