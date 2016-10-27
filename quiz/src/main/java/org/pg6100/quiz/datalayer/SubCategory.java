package org.pg6100.quiz.datalayer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class SubCategory {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
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

}
