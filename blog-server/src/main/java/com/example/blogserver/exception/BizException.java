package com.example.blogserver.exception;

import com.zlc.blogcommon.enums.StatusCodeEnum;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import static com.zlc.blogcommon.enums.StatusCodeEnum.FAIL;


/**
 * 处理业务逻辑异常
 *
 */
@Getter
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code = FAIL.getCode();

    /**
     * 错误信息
     */
    private final String message;

    public BizException(String message) {
        this.message = message;

    }

    public BizException(String message, StatusCodeEnum statusCodeEnum) {
        this.message = message;
        this.code = statusCodeEnum.getCode();
    }

    public BizException(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getDesc();
    }
}
