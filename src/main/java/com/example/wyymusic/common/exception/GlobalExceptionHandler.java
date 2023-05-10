package com.example.wyymusic.common.exception;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.ErrorCode;
import com.example.wyymusic.common.Results;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xyc
 * @CreteDate 2023/4/26 9:48
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public <T>BaseResponse<T> businessException(BusinessException e){
        return Results.error(e.getMessage(),e.code,e.description);
    }

    @ExceptionHandler(RuntimeException.class)
    public <T>BaseResponse<T> Exception(RuntimeException e){
        return Results.error(ErrorCode.SYSTEM_ERROR);
    }
}
