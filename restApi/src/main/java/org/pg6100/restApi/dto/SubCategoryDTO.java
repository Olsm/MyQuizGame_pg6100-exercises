package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.datalayer.RootCategory;

@ApiModel("A category")
public class SubCategoryDTO {

    @ApiModelProperty("The category name (id)")
    public String name;

    @ApiModelProperty("The root category")
    public RootCategory rootCategory;

    public SubCategoryDTO(){}

    public SubCategoryDTO(RootCategory rootCategory, String name) {
        this.rootCategory = rootCategory;
        this.name = name;
    }
}
