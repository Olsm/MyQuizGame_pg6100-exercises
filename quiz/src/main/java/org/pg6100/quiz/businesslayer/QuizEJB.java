package org.pg6100.quiz.businesslayer;

import org.pg6100.quiz.datalayer.Quiz;
import org.pg6100.quiz.datalayer.SubSubCategory;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class QuizEJB {

    @PersistenceContext
    protected EntityManager em;

    public QuizEJB(){}

    public Quiz registerQuiz(SubSubCategory category, String question, List<String> answerList, int correctAnswer){
        Quiz quiz = new Quiz(category, question, answerList, correctAnswer);
        em.persist(quiz);
        return quiz;
    }

    public Quiz getQuiz(Long id) {
        Query query = em.createQuery("SELECT q FROM Quiz q where q.id = :id");
        query.setParameter("id", id);
        return (Quiz) query.getSingleResult();
    }

    public int getNumberOfQuizes(){
        Query query = em.createNamedQuery(Quiz.SUM_QUIZES);
        return ((Number)query.getSingleResult()).intValue();
    }

    public List<Quiz> getAll() {
        Query query = em.createQuery("SELECT q FROM Quiz q");
        return (List<Quiz>) query.getResultList();
    }

    public List<Quiz> getAllFromCategory(SubSubCategory category) {
        Query query = em.createQuery("SELECT q FROM Quiz q where q.subSubCategory = :category");
        query.setParameter("category", category);
        return (List<Quiz>) query.getResultList();
    }

    public void updateQuizCategory(Quiz quiz, SubSubCategory category) {
        quiz.setSubSubCategory(category);
    }

    public void updateQuizQuestion(Quiz quiz, String question) {
        quiz.setQuestion(question);
    }

    public void updateQuizAnswer(Quiz quiz, List<String> answerList) {
        quiz.setAnswers(answerList);
    }

    public void updateQuizCorrectAnswer(Quiz quiz, int correctAnswer) {
        quiz.setCorrectAnswer(correctAnswer);
    }

    public void deleteQuiz(Quiz quiz) {
        em.remove(quiz);
    }
}
