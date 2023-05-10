package com.example.wyymusic.common.exception;

import com.example.wyymusic.common.ErrorCode;
import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/4/26 9:46
 **/
@Data
public class BusinessException extends RuntimeException{

    int code;

    String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getErrorCode();
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getErrorCode();
        this.description = errorCode.getDescription();
    }

}
