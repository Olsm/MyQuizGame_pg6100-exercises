package org.pg6100.restApi.api;

import com.google.common.base.Throwables;
import org.pg6100.quiz.businesslayer.CategoryEJB;
import org.pg6100.quiz.businesslayer.QuizEJB;
import org.pg6100.quiz.datalayer.Quiz;
import org.pg6100.quiz.datalayer.SubSubCategory;
import org.pg6100.restApi.dto.QuizConverter;
import org.pg6100.restApi.dto.QuizDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/*
    The actual implementation could be a EJB, eg if we want to handle
    transactions and dependency injections with @EJB.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class QuizRestImpl implements QuizRestApi {

    @EJB
    private QuizEJB QEJB;
    @EJB
    private CategoryEJB CEJB;

    @Override
    public List<QuizDTO> get() {
        return QuizConverter.transform(QEJB.getAll());
    }

    @Override
    public List<QuizDTO> getByCategory(String id) {
        return QuizConverter.transform(QEJB.getAllFromCategory(CEJB.getSubSubCategory(id)));
    }

    @Override
    public Long createQuiz(QuizDTO dto) {

        /*
            Error code 400:
            the user had done something wrong, eg sent invalid input configurations
         */

        if(dto.id != null){
            throw new WebApplicationException("Cannot specify id for a newly generated quiz", 400);
        }
        /* if(dto.creationTime != null){
            throw new WebApplicationException("Cannot specify creationTime for a newly generated quiz", 400);
        }*/

        Quiz quiz;
        try{
            SubSubCategory subCat = CEJB.getSubSubCategory(dto.category.name);
            quiz = QEJB.registerQuiz(subCat, dto.question, dto.answerList, dto.answerList.indexOf(dto.correctAnswer));
        }catch (Exception e){
            /*
                note: this work just because NOT_SUPPORTED,
                otherwise a rolledback transaction would propagate to the
                caller of this method
             */
            throw wrapException(e);
        }

        return quiz.getId();
    }

    @Override
    public QuizDTO getById(Long id) {
        return QuizConverter.transform(QEJB.getQuiz(id));
    }

    @Override
    public void update(Long pathId, QuizDTO dto) {
        long id;
        try{
            id = Long.parseLong(dto.id);
        } catch (Exception e){
            throw new WebApplicationException("Invalid id: "+dto.id, 400);
        }

        if(id != pathId){
            // in this case, 409 (Conflict) sounds more appropriate than the generic 400
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        }

        if(! QEJB.isPresent(id)){
            throw new WebApplicationException("Not allowed to create a quiz with PUT, and cannot find quiz with id: "+id, 404);
        }

        try {
            QEJB.update(id, dto.question, dto.answerList, dto.answerList.indexOf(dto.correctAnswer));
        } catch (Exception e){
            throw wrapException(e);
        }
    }

    @Override
    public void updateQuestion(Long id, String question){
        if(! QEJB.isPresent(id)){
            throw new WebApplicationException("Cannot find quiz with id: "+id, 404);
        }

        try {
            QEJB.updateQuizQuestion(id, question);
        } catch (Exception e){
            throw wrapException(e);
        }
    }

    @Override
    public void delete(Long id) {
        QEJB.deleteQuiz(id);
    }


    //----------------------------------------------------------

    private WebApplicationException wrapException(Exception e) throws WebApplicationException{

        /*
            Errors:
            4xx: the user has done something wrong, eg asking for something that does not exist (404)
            5xx: internal server error (eg, could be a bug in the code)
         */

        Throwable cause = Throwables.getRootCause(e);
        if(cause instanceof ConstraintViolationException){
            return new WebApplicationException("Invalid constraints on input: "+cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
