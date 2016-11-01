package org.pg6100.quiz.businesslayer;

import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.quiz.datalayer.SubSubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    public RootCategory getRootCategory(Long id) {
        Query query = em.createQuery("SELECT c FROM RootCategory c where c.id = :id");
        query.setParameter("id", id);
        return (RootCategory) query.getSingleResult();
    }

    public SubCategory getSubCategory(Long id) {
        Query query = em.createQuery("SELECT c FROM SubCategory c where c.id = :id");
        query.setParameter("id", id);
        return (SubCategory) query.getSingleResult();
    }
    public SubSubCategory getSubSubCategory(Long id) {
        Query query = em.createQuery("SELECT c FROM SubSubCategory c where c.id = :id");
        query.setParameter("id", id);
        return (SubSubCategory) query.getSingleResult();
    }

}
