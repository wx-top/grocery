package icu.wenxin.grocery.business.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import icu.wenxin.grocery.business.domain.dto.ProductCreateDTO;
import icu.wenxin.grocery.business.domain.dto.ProductUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.ProductVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("products")
@AutoMappers({
        @AutoMapper(target = ProductCreateDTO.class),
        @AutoMapper(target = ProductUpdateDTO.class),
        @AutoMapper(target = ProductVO.class)
})
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer id; // 商品ID
    private String name; // 商品名称
    private String code; // 商品编码
    private BigDecimal price; // 商品价格
    private Integer categoryId; // 分类ID
    private Integer unitId; // 单位ID
    private String description; // 商品描述
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt; // 更新时间
}