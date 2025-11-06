package icu.wenxin.grocery.business.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UnitVO {
    private Integer id;
    private String name;
    // 新增：单位介绍
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}