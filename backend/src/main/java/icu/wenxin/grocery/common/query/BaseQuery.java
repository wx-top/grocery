package icu.wenxin.grocery.common.query;

import lombok.Data;

@Data
public class BaseQuery {
    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 每页数量
     */
    private Integer size = 15;

    /**
     * 排序字段
     */
    private String sortBy;

    /**
     * 排序方向: ASC, DESC
     */
    private String sortDirection = "DESC";

    /**
     * 搜索关键词
     */
    private String keyword;


    public Integer getCurrent() {
        if (current == null) {
            return 1;
        }
        return current;
    }

    public Integer getSize() {
        if (size == null) {
            return 15;
        }
        return size;
    }
}
