package org.pg6100.quiz.datalayer;

import javax.persistence.*;
import java.util.List;

@Entity
public class SubCategory {

    @Id
    private String category;
    @ManyToOne
    RootCategory rootCategory;
    @OneToMany
    private List<SubSubCategory> subSubCategoryList;

    public SubCategory() {}

    public SubCategory(RootCategory rootCategory, String category) {
        this.rootCategory = rootCategory;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
