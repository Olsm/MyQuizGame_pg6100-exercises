package org.pg6100.QuizAPI.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A sub sub category")
public class SubSubCategoryDTO {

    @ApiModelProperty("The sub sub category id")
    public String id;

    @ApiModelProperty("The category name (id)")
    public String name;

    @ApiModelProperty("The sub category")
    public String subCategoryId;

    public SubSubCategoryDTO(){}

    public SubSubCategoryDTO(String subCategoryId, String name) {
        this.subCategoryId = subCategoryId;
        this.name = name;
    }

    public SubSubCategoryDTO(String subCategoryId, String id, String name) {
        this(subCategoryId, name);
        this.id = id;
    }
}
