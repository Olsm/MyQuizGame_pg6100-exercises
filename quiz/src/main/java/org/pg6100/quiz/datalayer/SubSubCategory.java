package org.pg6100.quiz.datalayer;

import javax.persistence.*;
import java.util.List;

@Entity
public class SubSubCategory {

    @Id
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public List<Quiz> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
    }
}
