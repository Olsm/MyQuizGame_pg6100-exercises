package org.pg6100.quiz.datalayer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class RootCategory {

    @Id @GeneratedValue
    private Long id;
    @NotNull
    private String category;
    @OneToMany
    private List<SubCategory> subCategoryList;

    public RootCategory() {}

    public RootCategory(String category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }
}
