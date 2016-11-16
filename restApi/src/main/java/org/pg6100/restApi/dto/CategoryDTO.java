package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;
import org.pg6100.quiz.datalayer.SubSubCategory;

@ApiModel("A category")
public class CategoryDTO {

    @ApiModelProperty("The category name (id)")
    public String category;

    @ApiModelProperty("The root category")
    public RootCategory rootCategory;

    @ApiModelProperty("The sub category")
    public SubCategory subCategory;

    @ApiModelProperty("The sub sub category")
    public SubSubCategory subSubCategory;

    public CategoryDTO(){}

    public CategoryDTO(String category, RootCategory rootCategory, SubCategory subCategory, SubSubCategory subSubCategory) {
        this.category = category;
        this.rootCategory = rootCategory;
        this.subCategory = subCategory;
        this.subSubCategory = subSubCategory;
    }
}
