package com.example.wyymusic.utils;

import com.example.wyymusic.model.domain.User;
import com.example.wyymusic.model.vo.UserVo;

/**
 * @author xyc
 * @CreteDate 2023/4/28 15:43
 **/
public class UserHolder {
    private static final ThreadLocal<UserVo> userVoTL = new ThreadLocal<>();

    public static void saveUser(UserVo userVo){
        userVoTL.set(userVo);
    }

    public static UserVo getUser(){
        return userVoTL.get();
    }

    public static void removeUserVo(){
        userVoTL.remove();
    }
}
