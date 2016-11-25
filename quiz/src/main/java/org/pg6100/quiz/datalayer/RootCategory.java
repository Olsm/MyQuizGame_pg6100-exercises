package org.pg6100.quiz.datalayer;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
public class RootCategory {

    @Id
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<SubCategory> subCategoryList;

    public RootCategory() {}

    public RootCategory(String name) {
        this.name = name;
        this.subCategoryList = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String category) {
        this.name = category;
    }

    public Set<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(Set<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public void addSubCategory(SubCategory category) {
        getSubCategoryList().add(category);
    }

    public void removeSubCategory(SubCategory category) {
        getSubCategoryList().remove(category);
    }
}
