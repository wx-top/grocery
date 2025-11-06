package icu.wenxin.grocery.business.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryUpdateDTO {
    @NotNull(message = "分类ID不能为空")
    private Integer id;
    @NotBlank(message = "分类名称不能为空")
    private String name;
    private String description;
}