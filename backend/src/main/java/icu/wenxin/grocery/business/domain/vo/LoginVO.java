package icu.wenxin.grocery.business.domain.vo;

import lombok.Data;

@Data
public class LoginVO {
    private AdminVO admin;
    private String token;

}
