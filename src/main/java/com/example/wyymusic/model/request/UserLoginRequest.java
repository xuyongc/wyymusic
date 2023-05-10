package com.example.wyymusic.model.request;

import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/4/26 22:19
 **/
@Data
public class UserLoginRequest {

    private  String userAccount;

    private  String userPassword;

}
