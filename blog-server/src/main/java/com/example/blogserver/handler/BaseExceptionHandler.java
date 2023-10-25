package com.example.blogserver.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.BadPaddingException;

@ResponseBody
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseBody
    public void zuulHandler(BadPaddingException e) {
        System.out.println("出现全局异常");
    }

}
