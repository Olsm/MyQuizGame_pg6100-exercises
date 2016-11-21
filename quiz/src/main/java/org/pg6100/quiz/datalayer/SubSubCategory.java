package org.pg6100.quiz.datalayer;

import javax.persistence.*;
import java.util.List;

@Entity
public class SubSubCategory {

    @Id
    private String name;
    @ManyToOne
    SubCategory subCategory;
    @OneToMany
    private List<Quiz> quizList;

    public SubSubCategory() {}

    public SubSubCategory(SubCategory subCategory, String name) {
        this.subCategory = subCategory;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String category) {
        this.name = category;
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

    public void addQuiz(Quiz quiz) {
        getQuizList().add(quiz);
    }
}
