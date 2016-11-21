package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;

@ApiModel("A category")
public class SubSubCategoryDTO {

    @ApiModelProperty("The category name (id)")
    public String name;

    @ApiModelProperty("The sub category")
    public SubCategory subCategory;

    public SubSubCategoryDTO(){}

    public SubSubCategoryDTO(SubCategory subCategory, String name) {
        this.subCategory = subCategory;
        this.name = name;
    }
}
