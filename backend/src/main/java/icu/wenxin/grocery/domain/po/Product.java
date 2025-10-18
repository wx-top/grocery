package icu.wenxin.grocery.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("products")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer id; // 商品ID
    private String name; // 商品名称
    private String code; // 商品编码
    private BigDecimal price; // 商品价格
    private Integer categoryId; // 分类ID
    private Integer unitId; // 单位ID
    private String description; // 商品描述
    @TableField(exist = false)
    private List<ProductImage> imageList;
    @Builder.Default
    private Boolean isActive = true; // 是否上架
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt; // 更新时间
}