package com.example.wyymusic.service;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wyymusic.model.request.UpdateUserRequest;
import com.example.wyymusic.model.vo.UserVo;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author xyc
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-04-26 09:12:50
*/
public interface UserService extends IService<User> {

    /**
     * 密码登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    String userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 发送密码
     * @param phone
     * @return
     */
    BaseResponse<String> sendCode(String phone);

    /**
     * 用户登录通过验证码
     * @param phone
     * @param code
     * @return
     */
    BaseResponse<String> userLogin(String phone,String code);


    /**
     * 推出登录
     * @param request
     * @return
     */
    BaseResponse<Boolean> userLogout(HttpServletRequest request);

    /**
     * 注销
     * @param request
     * @return
     */
    BaseResponse<Boolean> removeByMe(HttpServletRequest request);

    /**
     * 管理员注销
     * @param userId
     * @return
     */
    BaseResponse<Boolean> removeByAdmin(Long userId);

    /**
     * 修改用户
     * @param request
     * @return
     */
    BaseResponse<Boolean> updateUser(UpdateUserRequest request);

    /**
     * 获取用户列表
     * @param pageNumber
     * @param pageSize
     * @return
     */
    BaseResponse<List<UserVo>> getList(int pageNumber, int pageSize);

    /**
     * 创建一个用户通过手机号
     * @param phone
     * @return
     */
    User createUserByPhone(String phone);


    /**
     * 获取当前用户
     * @return
     */
    UserVo getCurrentUser();
}
