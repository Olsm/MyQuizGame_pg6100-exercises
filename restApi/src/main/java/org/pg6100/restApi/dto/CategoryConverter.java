package org.pg6100.restApi.dto;

import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.quiz.datalayer.SubSubCategory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CategoryConverter {

    private CategoryConverter() {}

    public static CategoryDTO transform(RootCategory category) {
        Objects.requireNonNull(category);
        CategoryDTO dto = new CategoryDTO();
        dto.category = String.valueOf(category.getCategory());
        return dto;
    }

    public static CategoryDTO transform(SubCategory category) {
        CategoryDTO dto = transform(category.getRootCategory());
        dto.rootCategory = category.getRootCategory();
        return dto;
    }

    public static CategoryDTO transform(SubSubCategory category) {
        CategoryDTO dto = transform(category.getSubCategory());
        dto.subCategory = category.getSubCategory();
        return dto;
    }

    public static List<CategoryDTO> transformList(List<?>categories) {
        Objects.requireNonNull(categories);
        return categories.stream()
                .map(CategoryConverter::transform)
                .collect(Collectors.toList());
    }




    public static CategoryDTO transform(Object category) {
        return transform((category.getClass().cast(category)));
    }


    /*
    public static CategoryDTO transform(CategoryInterface category) {
        if (category.getClass() == RootCategory.class)
            return transform((RootCategory) category);
        else if (category.getClass() == SubCategory.class)
            return transform((SubCategory) category);
        else if (category.getClass() == SubSubCategory.class)
            return transform((SubSubCategory) category);
        else
            return null;
    }
    */
}
