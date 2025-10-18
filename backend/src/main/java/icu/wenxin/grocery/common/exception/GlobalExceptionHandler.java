package icu.wenxin.grocery.common.exception;

import icu.wenxin.grocery.common.resp.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R<String> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        return R.error("系统异常");
    }
}
