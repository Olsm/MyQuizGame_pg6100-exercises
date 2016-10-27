package org.pg6100.quiz.datalayer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class SubSubCategory {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String category;
    @ManyToOne
    SubCategory subCategory;
    @OneToMany
    private List<Quiz> quizList;

    public SubSubCategory() {}

    public SubSubCategory(SubCategory subCategory, String category) {
        this.subCategory = subCategory;
        this.category = category;
    }
}
