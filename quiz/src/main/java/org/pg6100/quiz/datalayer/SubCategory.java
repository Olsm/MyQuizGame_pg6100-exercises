package org.pg6100.quiz.datalayer;

import javax.persistence.*;
import java.util.List;

@Entity
public class SubCategory {

    @Id
    private String name;
    @ManyToOne
    RootCategory rootCategory;
    @OneToMany
    private List<SubSubCategory> subSubCategoryList;

    public SubCategory() {}

    public SubCategory(RootCategory rootCategory, String name) {
        this.rootCategory = rootCategory;
        this.name = name;
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

    public List<SubSubCategory> getSubSubCategoryList() {
        return subSubCategoryList;
    }

    public void setSubSubCategoryList(List<SubSubCategory> subSubCategoryList) {
        this.subSubCategoryList = subSubCategoryList;
    }

    public void addSubSubCategory(SubSubCategory category) {
        getSubSubCategoryList().add(category);
    }
}
