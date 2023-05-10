package com.example.wyymusic.config;

import com.example.wyymusic.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.wyymusic.constant.CommonConstant.ADMIN_CODE;

/**
 * @author xyc
 * @CreteDate 2023/5/3 23:47
 **/

public class AdminInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (UserHolder.getUser().getUserRole() != ADMIN_CODE){
            return false;
        }
        return true;
    }


}
