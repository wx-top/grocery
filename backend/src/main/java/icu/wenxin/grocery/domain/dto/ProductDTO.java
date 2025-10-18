package icu.wenxin.grocery.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductDTO extends BasePage {
    private Integer categoryId;
    private String name;
    private String code;
}
