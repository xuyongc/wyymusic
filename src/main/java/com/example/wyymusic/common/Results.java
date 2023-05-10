package com.example.wyymusic.common;

/**
 * @author xyc
 * @CreteDate 2023/4/26 9:31
 **/
public class Results {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>("成功", 200, null, data);
    }

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>("成功", 200, null, null);
    }

    public static <T> BaseResponse<T> success(String description,T data) {
        return new BaseResponse<>("成功", 200, description, data);
    }

    public static <T> BaseResponse<T> error(String message,int code,String  description) {
        return new BaseResponse<>(message, code, description, null);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode,String  description) {
        return new BaseResponse<>(errorCode.getMessage(), errorCode.getErrorCode(), description, null);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getMessage(), errorCode.getErrorCode(), errorCode.getDescription(), null);
    }

}
