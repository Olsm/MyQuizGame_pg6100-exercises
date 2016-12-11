package org.pg6100.QuizAPI.dto;

import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.quiz.datalayer.SubSubCategory;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryConverter {

    private CategoryConverter() {}

    public static RootCategoryDTO transform(RootCategory category) {
        Objects.requireNonNull(category);
        return new RootCategoryDTO(category.getId().toString(), category.getName());
    }

    public static SubCategoryDTO transform(SubCategory category) {
        return new SubCategoryDTO(category.getRootCategory().getId().toString(), category.getId().toString(), category.getName());
    }

    public static SubSubCategoryDTO transform(SubSubCategory category) {
        return new SubSubCategoryDTO(category.getSubCategory().getId().toString(), category.getId().toString(), category.getName());
    }

    public static Set<RootCategoryDTO> transformCategories(Set<RootCategory> categories) {
        Objects.requireNonNull(categories);
        return categories.stream()
                .map(CategoryConverter::transform)
                .collect(Collectors.toSet());
    }

    public static Set<SubCategoryDTO> transformSubCategories(Set<SubCategory> categories) {
        Objects.requireNonNull(categories);
        return categories.stream()
                .map(CategoryConverter::transform)
                .collect(Collectors.toSet());
    }

    public static Set<SubSubCategoryDTO> transformSubSubCategories(Set<SubSubCategory> categories) {
        Objects.requireNonNull(categories);
        return categories.stream()
                .map(CategoryConverter::transform)
                .collect(Collectors.toSet());
    }
}
