package org.pg6100.restApi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.pg6100.quiz.datalayer.RootCategory;
import org.pg6100.quiz.datalayer.SubCategory;

@ApiModel("A sub sub category")
public class SubSubCategoryDTO {

    @ApiModelProperty("The category name (id)")
    public String name;

    @ApiModelProperty("The sub category")
    public String subCategoryName;

    public SubSubCategoryDTO(){}

    public SubSubCategoryDTO(String subCategoryName, String name) {
        this.subCategoryName = subCategoryName;
        this.name = name;
    }
}
