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

    public void updateQuizCategory(Long quizId, SubSubCategory category) {
        getQuiz(quizId).setSubSubCategory(category);
    }

    public void updateQuizQuestion(Long quizId, String question) {
        getQuiz(quizId).setQuestion(question);
    }

    public void updateQuizAnswer(Long quizId, List<String> answerList) {
        getQuiz(quizId).setAnswers(answerList);
    }

    public void updateQuizCorrectAnswer(Long quizId, int correctAnswer) {
        getQuiz(quizId).setCorrectAnswer(correctAnswer);
    }

    public void deleteQuiz(Long quizId) {
        em.remove(getQuiz(quizId));
    }
}
