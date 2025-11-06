package icu.wenxin.grocery.business.domain.dto;

import icu.wenxin.grocery.common.query.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryQueryDTO extends BaseQuery {
    private String name;
}