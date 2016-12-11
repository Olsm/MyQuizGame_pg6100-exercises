package org.pg6100.quiz.datalayer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SubSubCategory {

    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    @ManyToOne
    SubCategory subCategory;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Quiz> quizList;

    public SubSubCategory() {}

    public SubSubCategory(SubCategory subCategory, String name) {
        this.subCategory = subCategory;
        this.name = name;
        this.quizList = new HashSet<>();
    }

    public Long getId() {
        return id;
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

    public Set<Quiz> getQuizList() {
        return quizList;
    }

    public void setQuizList(Set<Quiz> quizList) {
        this.quizList = quizList;
    }

    public void addQuiz(Quiz quiz) {
        getQuizList().add(quiz);
    }
}
