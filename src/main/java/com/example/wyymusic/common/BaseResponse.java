package com.example.wyymusic.common;

import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/4/26 9:27
 **/
@Data
public class BaseResponse<T> {
    private String message;

    private int code;

    private String description;

    private T Data;

    public BaseResponse(String message, int code, String description, T data) {
        this.message = message;
        this.code = code;
        this.description = description;
        Data = data;
    }
}
