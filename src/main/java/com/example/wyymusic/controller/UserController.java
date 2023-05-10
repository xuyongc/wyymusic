package com.example.wyymusic.controller;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.ErrorCode;
import com.example.wyymusic.common.Results;
import com.example.wyymusic.common.exception.BusinessException;
import com.example.wyymusic.model.request.*;
import com.example.wyymusic.model.vo.UserVo;
import com.example.wyymusic.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/4/26 9:24
 **/
@Controller
@RequestMapping("/user")
@ResponseBody
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<String> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();


        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String uuid = userService.userLogin(userAccount, userPassword,request);

        return Results.success(uuid);
    }


    /**
     * 发送验证码
     * @param request
     * @return
     */
    @PostMapping("/send/code")
    public BaseResponse<String> sendCode(@RequestBody SendCodeRequest request){
        return userService.sendCode(request.getPhone());
    }

    /**
     * 验证码登录
     * @param request
     * @return
     */
    @PostMapping("/login/code")
    public BaseResponse<String> userLogin(@RequestBody UserLoginByCodeRequest request){
        return userService.userLogin(request.getPhone(),request.getCode());
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        return userService.userLogout(request);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("/remove/")
    public BaseResponse<Boolean> removeByMe(HttpServletRequest request){
        return userService.removeByMe(request);
    }

    /**
     * 管理员删除
     * @param userId
     * @return
     */
    @GetMapping("/admin/remove/")
    public BaseResponse<Boolean> removeByAdmin(Long userId){
        return userService.removeByAdmin(userId);
    }

    /**
     * 用户修改
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UpdateUserRequest request){
        return userService.updateUser(request);
    }

    /**
     * 管理员修改用户列表
     * @param request
     * @return
     */
    @PostMapping("/admin/get/list")
    public BaseResponse<List<UserVo>> getUserList(@RequestBody GetUserListRequest request){
        return userService.getList(request.getPageNumber(),request.getPageSize());
    }

    /**
     * 获取当前用户
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<UserVo> getCurrentUser() {
        return Results.success(userService.getCurrentUser());
    }

}
