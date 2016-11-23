package org.pg6100.restApi.dto;

import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.quiz.datalayer.SubSubCategory;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryConverter {

    private CategoryConverter() {}

    public static RootCategoryDTO transform(RootCategory category) {
        Objects.requireNonNull(category);
        RootCategoryDTO dto = new RootCategoryDTO(category.getName());
        return dto;
    }

    public static SubCategoryDTO transform(SubCategory category) {
        SubCategoryDTO dto = new SubCategoryDTO(category.getRootCategory().getName(), category.getName());
        return dto;
    }

    public static SubSubCategoryDTO transform(SubSubCategory category) {
        SubSubCategoryDTO dto = new SubSubCategoryDTO(category.getSubCategory().getName(), category.getName());
        return dto;
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
