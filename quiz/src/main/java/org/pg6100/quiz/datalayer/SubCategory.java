package org.pg6100.quiz.datalayer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SubCategory {

    @Id
    private String name;
    @ManyToOne
    RootCategory rootCategory;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<SubSubCategory> subSubCategoryList;

    public SubCategory() {}

    public SubCategory(RootCategory rootCategory, String name) {
        this.rootCategory = rootCategory;
        this.name = name;
        this.subSubCategoryList = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String category) {
        this.name = category;
    }

    public RootCategory getRootCategory() {
        return rootCategory;
    }

    public void setRootCategory(RootCategory rootCategory) {
        this.rootCategory = rootCategory;
    }

    public Set<SubSubCategory> getSubSubCategoryList() {
        return subSubCategoryList;
    }

    public void setSubSubCategoryList(Set<SubSubCategory> subSubCategoryList) {
        this.subSubCategoryList = subSubCategoryList;
    }

    public void addSubSubCategory(SubSubCategory category) {
        getSubSubCategoryList().add(category);
    }

    public void removeSubSubCategory(SubSubCategory subSubCategory) {
        getSubSubCategoryList().remove(subSubCategory);
    }
}
