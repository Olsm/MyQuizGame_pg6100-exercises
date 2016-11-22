package org.pg6100.restApi.dto;

import org.pg6100.quiz.datalayer.Quiz;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/*
    It might be tempting to use the @Entity objects directly as DTOs.
    That would be WRONG!!!
    It might work for small applications developed by one single person,
    but it is a huge problem (eg, maintainability and de-coupling) for
    larger projects.

    So here we need a converter from @Entity to DTO
 */
public class QuizConverter {

    private QuizConverter(){}

    public static QuizDTO transform(Quiz entity){
        Objects.requireNonNull(entity);
        QuizDTO dto = new QuizDTO();
        dto.id = String.valueOf(entity.getId());
        dto.category = new SubSubCategoryDTO(entity.getSubCategory().getName(), entity.getSubSubCategory().getName());
        dto.question = entity.getQuestion();
        dto.answerList = entity.getAnswers();
        dto.correctAnswer = entity.getCorrectAnswer();

        return dto;
    }

    public static List<QuizDTO> transform(List<Quiz> entities){
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(QuizConverter::transform)
                .collect(Collectors.toList());
    }

}
