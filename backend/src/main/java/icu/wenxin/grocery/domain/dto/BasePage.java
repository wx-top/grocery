package icu.wenxin.grocery.domain.dto;

import lombok.Data;

@Data
public class BasePage {
    private Long current = 1L;
    private Long size = 10L;
}
