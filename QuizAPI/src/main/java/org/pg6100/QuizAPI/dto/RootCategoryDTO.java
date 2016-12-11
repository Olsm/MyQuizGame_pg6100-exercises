package org.pg6100.QuizAPI.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A category")
public class RootCategoryDTO {

    @ApiModelProperty("The root category id")
    public String id;

    @ApiModelProperty("The category name)")
    public String name;

    public RootCategoryDTO(){}

    public RootCategoryDTO(String name) {
        this.name = name;
    }

    public RootCategoryDTO(String id, String name) {
        this(name);
        this.id = id;
    }
}
