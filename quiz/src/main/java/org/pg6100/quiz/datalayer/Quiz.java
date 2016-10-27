package org.pg6100.quiz.datalayer;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@NamedQueries({
        @NamedQuery(name = Quiz.SUM_QUIZES, query = "select count(q) from Quiz q"),
})

@Entity
public class Quiz {

    public static final String SUM_QUIZES = "SUM_QUIZS";

    @Id @GeneratedValue
    private Long id;
    @NotNull @ManyToOne
    private SubSubCategory subSubCategory;
    @NotEmpty
    private String question;
    @NotEmpty
    private List<String> answerList;
    @NotEmpty
    private int correctAnswer;

    public Quiz() {}

    public Quiz(SubSubCategory category, String question, List<String> answerList, int correctAnswer) {
        this.subSubCategory = category;
        this.question = question;
        this.answerList = answerList;
        this.correctAnswer = correctAnswer;
    }

    public Long getId() {
        return id;
    }

    public SubSubCategory getSubSubCategory() {
        return subSubCategory;
    }

    public void setSubSubCategory(SubSubCategory subSubCategory) {
        this.subSubCategory = subSubCategory;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Quiz.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Quiz other = (Quiz) obj;
        return (this.getId() == null) ? other.getId() == null : this.getId().equals(other.getId());
    }
}
