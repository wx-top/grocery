package icu.wenxin.grocery.business.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminVO {
    private Integer id;
    private String nickname;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
