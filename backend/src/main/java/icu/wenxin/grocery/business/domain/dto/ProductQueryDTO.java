package icu.wenxin.grocery.business.domain.dto;

import icu.wenxin.grocery.common.query.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductQueryDTO extends BaseQuery {
    private Integer categoryId;
    private String name;
    private String code;
}
