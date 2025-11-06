package icu.wenxin.grocery.business.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UnitUpdateDTO {
    @NotNull(message = "单位ID不能为空")
    private Integer id;
    @NotBlank(message = "单位名称不能为空")
    private String name;
    // 新增：单位介绍（可选）
    private String description;
}