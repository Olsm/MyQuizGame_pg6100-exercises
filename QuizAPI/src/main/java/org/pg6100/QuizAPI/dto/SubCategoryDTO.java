package org.pg6100.QuizAPI.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A sub category")
public class SubCategoryDTO {

    @ApiModelProperty("The category name (id)")
    public String name;

    @ApiModelProperty("The root category")
    public String rootCategoryName;

    public SubCategoryDTO(){}

    public SubCategoryDTO(String rootCategoryName, String name) {
        this.rootCategoryName = rootCategoryName;
        this.name = name;
    }
}
