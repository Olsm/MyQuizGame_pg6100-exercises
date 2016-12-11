package org.pg6100.quiz;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pg6100.quiz.businesslayer.CategoryEJB;
import org.pg6100.quiz.businesslayer.QuizEJB;
import org.pg6100.quiz.datalayer.Quiz;
import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.quiz.datalayer.SubSubCategory;
import org.pg6100.quiz.util.DeleterEJB;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class QuizEJBTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.pg6100.quiz.businesslayer","org.pg6100.quiz.datalayer",
                        "org.apache.commons.codec")
                .addClass(DeleterEJB.class)
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private QuizEJB quizEJB;
    @EJB
    private CategoryEJB categoryEJB;
    @EJB
    private DeleterEJB deleterEJB;

    private Quiz quiz;
    private RootCategory rootCategory;
    private SubCategory subCategory;
    private SubSubCategory subSubCategory;
    private List<String> answerList;
    private int correctAnswer;
    private int count = 1;

    @Before
    public void setup() {
        rootCategory = categoryEJB.registerRootCategory("Science" + count);
        subCategory = categoryEJB.registerSubCategory(rootCategory, "Computer Science" + count);
        subSubCategory = categoryEJB.registerSubSubCategory(subCategory, "JEE" + count);
        answerList = new ArrayList<>();
        answerList.add("answer1");
        answerList.add("answer2");
        answerList.add("answer3");
        answerList.add("answer4");
        correctAnswer = 1;
        quiz = quizEJB.registerQuiz(subSubCategory, "question", answerList, correctAnswer);
        count++;
    }

    @After
    public void tearDown() {
        deleterEJB.deleteEntities(Quiz.class);
        for (SubSubCategory cat : categoryEJB.getAllSubSubCategories()) {
            categoryEJB.deleteSubSubCategory(cat.getId());
        }
        for (SubCategory cat : categoryEJB.getAllSubCategories()) {
            categoryEJB.deleteSubCategory(cat.getId());
        }
        deleterEJB.deleteEntities(RootCategory.class);
    }

    @Test
    public void testGetRootCategory() {
        assertEquals(rootCategory.getId(), quiz.getRootCategory().getId());
    }

    @Test
    public void testGetSubCategory() {
        assertEquals(subCategory.getId(), quiz.getSubCategory().getId());
    }

    @Test
    public void testGetSubSubCategory() {
        assertEquals(subSubCategory, quiz.getSubSubCategory());
    }

    @Test
    public void testGetAnswerList() {
        assertEquals(answerList, quiz.getAnswers());
    }

    @Test
    public void testGetCorrectAnswer() {
        assertEquals(answerList.get(correctAnswer), quiz.getCorrectAnswer());
    }

    @Test
    public void getQuiz() {
        assertEquals(quiz, quizEJB.getQuiz(quiz.getId()));
    }

    @Test
    public void getNumberOfQuizes() {
        int quizs = quizEJB.getNumberOfQuizes();
        quizEJB.registerQuiz(subSubCategory, "question", answerList, correctAnswer);
        assertEquals(quizs + 1, quizEJB.getNumberOfQuizes());
    }

    @Test(expected = EJBException.class)
    public void testQuestionCannotBeEmpty() {
        quizEJB.registerQuiz(subSubCategory, "", answerList, correctAnswer);
    }

    @Test(expected = EJBException.class)
    public void testAnswerCannotBeEmpty() {
        quizEJB.registerQuiz(subSubCategory, "question", new ArrayList<>(), correctAnswer);
    }

    @Test(expected = EJBException.class)
    public void testCorrectAnswerCannotBeMoreThan4() {
        quizEJB.registerQuiz(subSubCategory, "question", answerList, 5);
    }

    @Test(expected = EJBException.class)
    public void testCorrectAnswerCannotBeLessThan1() {
        quizEJB.registerQuiz(subSubCategory, "question", answerList, 0);
    }

    @Test
    public void testUpdateQuizCategory() {
        SubSubCategory category = categoryEJB.registerSubSubCategory(subCategory, "subsubcat");
        quizEJB.updateQuizCategory(quiz.getId(), category);
        assertEquals(category.getId(), quizEJB.getQuiz(quiz.getId()).getSubSubCategory().getId());
    }

    @Test
    public void testUpdateQuizQuestion() {
        quizEJB.updateQuizQuestion(quiz.getId(), "suchQ");
        assertEquals("suchQ", quizEJB.getQuiz(quiz.getId()).getQuestion());
    }

    @Test
    public void testUpdateQuizAnswer() {
        ArrayList<String> answerList = new ArrayList<>();
        answerList.add("A");
        answerList.add("B");
        answerList.add("C");
        answerList.add("D");
        quizEJB.updateQuizAnswer(quiz.getId(), answerList);
        assertEquals(answerList, quizEJB.getQuiz(quiz.getId()).getAnswers());
    }

    @Test
    public void testUpdateQuizCorrectAnswer() {
        quizEJB.updateQuizCorrectAnswer(quiz.getId(), 2);
        assertEquals("answer2", quizEJB.getQuiz(quiz.getId()).getCorrectAnswer());
    }

    @Test
    public void testDeleteQuiz() {
        quizEJB.deleteQuiz(quiz.getId());
        assertFalse(quizEJB.isPresent(quiz.getId()));
    }

}