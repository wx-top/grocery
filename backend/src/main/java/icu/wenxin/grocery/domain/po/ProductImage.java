package icu.wenxin.grocery.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("product_images")
public class ProductImage {
    @TableId(type = IdType.AUTO)
    private Integer id;  // 图片ID
    private Integer productId; // 商品ID
    private String url; // 图片URL
    private String name; // 文件名
    private String fileName;
    @Builder.Default
    private Integer sortOrder = 0; // 图片排序
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt; // 更新时间
}