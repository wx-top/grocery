package icu.wenxin.grocery.business.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UnitCreateDTO {
    @NotBlank(message = "单位名称不能为空")
    private String name;
    // 新增：单位介绍（可选）
    private String description;
}