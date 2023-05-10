package com.example.wyymusic.config;

import cn.hutool.core.util.StrUtil;
import com.example.wyymusic.model.vo.UserVo;
import com.example.wyymusic.utils.UserHolder;
import com.google.gson.Gson;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static com.example.wyymusic.constant.CommonConstant.LOGIN_USER_KEY;
import static com.example.wyymusic.constant.TimeConstant.LOGIN_USER_TL;

/**
 * @author xyc
 * @CreteDate 2023/4/28 15:36
 **/
public class FlushRedisInterceptor implements HandlerInterceptor {

    private StringRedisTemplate redisTemplate;

    public FlushRedisInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


//        if ("OPTIONS".equals(request.getMethod())) {
//            return true;
//        }
        String token = request.getHeader("authorization");
        if (StrUtil.hasEmpty(token)) {
            return true;
        }

        String userJson = redisTemplate.opsForValue().get(LOGIN_USER_KEY + token);

        Gson gson = new Gson();
        UserVo userVo = gson.fromJson(userJson, UserVo.class);

        UserHolder.saveUser(userVo);

        redisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TL, TimeUnit.MINUTES);

        return true;
    }

    /**
     * 为什么在这执行 拦截器执行顺序 pr1 pr2 ps1 ps2 af1 af2
     * https://blog.csdn.net/zxd1435513775/article/details/80556034
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUserVo();
    }
}
