package org.pg6100.quiz.datalayer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RootCategory {

    @Id
    private String name;
    @OneToMany
    private List<SubCategory> subCategoryList;

    public RootCategory() {}

    public RootCategory(String name) {
        this.name = name;
        this.subCategoryList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String category) {
        this.name = category;
    }

    public List<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public void addSubCategory(SubCategory category) {
        getSubCategoryList().add(category);
    }
}
