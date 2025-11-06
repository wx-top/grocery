package icu.wenxin.grocery.business.domain.vo;

import icu.wenxin.grocery.business.domain.po.ProductImage;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductVO {
    private Integer id;
    private String name; // 商品名称
    private String code; // 商品编码
    private BigDecimal price; // 商品价格
    private Integer categoryId; // 分类ID
    private Integer unitId; // 单位ID
    private String description; // 商品描述
    private List<ProductImage> imageList;
    private LocalDateTime createAt; // 创建时间
    private LocalDateTime updateAt; // 更新时间
}
