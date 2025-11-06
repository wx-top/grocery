package icu.wenxin.grocery.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String secret = "grocery-wenxin";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // 排除不需要拦截的路径
        if (isExcludePath(request)) {
            return true;
        }

        // 获取 Token
        String token = request.getHeader("Token");
        if (StrUtil.isBlank(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 验证 Token
        try {
            JWT jwt = JWT.of(token);
            boolean validate = jwt.setKey(secret.getBytes()).validate(0);
            if (!validate) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            request.setAttribute("adminId", jwt.getPayload("adminId"));
            return true;
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    private boolean isExcludePath(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/admin/login");
    }
}
