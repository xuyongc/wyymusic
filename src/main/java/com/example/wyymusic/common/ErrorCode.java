package com.example.wyymusic.common;

/**
 * @author xyc
 * @CreteDate 2023/4/26 9:35
 **/
public enum ErrorCode {

    /**
     * 全局异常处理
     */
    SYSTEM_ERROR("系统异常", 50000),

    ERROR("请求异常错误",40000),

    PARAMS_ERROR("请求参数异常", 40001),

    PARAMS_NULL_ERROR("请求参数为空", 40002),

    REQUEST_ERROR("请求错误", 40003),

    NULL_ERROR("请求数据为空", 40004),

    NOT_LOGIN_ERROR("未登录", 30200),

    NO_AUTH_ERROR("没权限", 70002),

    USER_ALREADY_REGISTERED("用户已经注册", 40017),

    USER_NOT_REGISTER("用户未注册", 40103),

    USER_DATA_ERROR("用户数据不存在", 40105),
    ;


    String message;

    int errorCode;

    String description;

    ErrorCode(String message, int errorCode, String description) {
        this.message = message;
        this.errorCode = errorCode;
        this.description = description;
    }

    ErrorCode(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }
}
