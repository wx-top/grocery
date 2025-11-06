package icu.wenxin.grocery.business.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

import icu.wenxin.grocery.business.domain.dto.CategoryCreateDTO;
import icu.wenxin.grocery.business.domain.dto.CategoryUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.CategoryVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("categories")
@AutoMappers({
    @AutoMapper(target = CategoryCreateDTO.class),
    @AutoMapper(target = CategoryUpdateDTO.class),
    @AutoMapper(target = CategoryVO.class)
})
public class Category {
    @TableId(type = IdType.AUTO)
    private Integer id; // 分类id
    private String name; // 分类名称
    private String description; // 分类描述
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt; // 更新时间
}