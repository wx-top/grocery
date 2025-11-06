package icu.wenxin.grocery.business.domain.dto;

import icu.wenxin.grocery.business.domain.po.ProductImage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateDTO {
    @NotBlank(message = "商品名称不能为空")
    private String name; // 商品名称
    private String code; // 商品编码
    private BigDecimal price; // 商品价格
    @NotBlank(message = "商品分类不能为空")
    private Integer categoryId; // 分类ID
    @NotBlank(message = "商品单位不能为空")
    private Integer unitId; // 单位ID
    private String description; // 商品描述
    private List<ProductImage> imageList;
}
