package com.example.wyymusic.model.request;

import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/4/27 9:27
 **/
@Data
public class UserLoginByCodeRequest {
    private String phone;

    private String code;
}
