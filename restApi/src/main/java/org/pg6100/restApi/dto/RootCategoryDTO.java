package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.quiz.datalayer.SubSubCategory;

@ApiModel("A category")
public class RootCategoryDTO {

    @ApiModelProperty("The category name (id)")
    public String name;

    public RootCategoryDTO(){}

    public RootCategoryDTO(String name) {
        this.name = name;
    }
}
