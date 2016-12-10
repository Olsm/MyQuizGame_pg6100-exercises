package org.pg6100.QuizAPI.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A category")
public class RootCategoryDTO {

    @ApiModelProperty("The category name (id)")
    public String name;

    public RootCategoryDTO(){}

    public RootCategoryDTO(String name) {
        this.name = name;
    }
}
