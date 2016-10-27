package org.pg6100.quiz.businesslayer;

import org.pg6100.quiz.datalayer.Quiz;
import org.pg6100.quiz.datalayer.SubSubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class QuizBean {

    @PersistenceContext
    protected EntityManager em;

    public QuizBean(){}

    public Quiz registerQuiz(SubSubCategory category, String question, List<String> answerList, int correctAnswer) {
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
}
