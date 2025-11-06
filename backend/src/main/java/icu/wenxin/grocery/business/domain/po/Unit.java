package icu.wenxin.grocery.business.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import icu.wenxin.grocery.business.domain.dto.UnitCreateDTO;
import icu.wenxin.grocery.business.domain.dto.UnitUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.UnitVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("units")
@AutoMappers({
    @AutoMapper(target = UnitCreateDTO.class),
    @AutoMapper(target = UnitUpdateDTO.class),
    @AutoMapper(target = UnitVO.class)
})
public class Unit {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    // 新增字段：单位介绍
    private String description;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt;
}
