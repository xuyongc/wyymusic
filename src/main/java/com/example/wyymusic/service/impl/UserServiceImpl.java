package com.example.wyymusic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.ErrorCode;
import com.example.wyymusic.common.Results;
import com.example.wyymusic.common.exception.BusinessException;
import com.example.wyymusic.model.domain.User;
import com.example.wyymusic.model.request.UpdateUserRequest;
import com.example.wyymusic.model.vo.UserVo;
import com.example.wyymusic.service.UserService;
import com.example.wyymusic.mapper.UserMapper;
import com.example.wyymusic.utils.RegexUtil;
import com.example.wyymusic.utils.UserHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.example.wyymusic.constant.CommonConstant.*;
import static com.example.wyymusic.constant.TimeConstant.LOGIN_CODE_TL;
import static com.example.wyymusic.constant.TimeConstant.LOGIN_USER_TL;

/**
 * @author xyc
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-04-26 09:12:50
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //判断是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //判断账号是否大于六
        if (userAccount.length() <= 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //判断密码是否大于八
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验账户特殊字符
        if (RegexUtil.isHaveSpecialCharacter(userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //判断是否存在
        String newPassword = DigestUtils.md5DigestAsHex((PASSWORD_PREFIX + userPassword + PASSWORD_SUFFIX).getBytes());
        User user = query().eq("userAccount", userAccount).eq("userPassword", newPassword).one();

        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_REGISTER);
        }

        return saveToRedis(user);
    }

    @Override
    public BaseResponse<String> sendCode(String phone) {
        if (RegexUtil.isPhone(phone)) {
            throw new BusinessException(ErrorCode.ERROR, "手机号有误");
        }

        String code = RandomUtil.randomNumbers(6);

        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TL, TimeUnit.MINUTES);

        return Results.success(code);
    }

    @Override
    public BaseResponse<String> userLogin(String phone, String code) {
        //        判断手机号是否合法
        if (RegexUtil.isPhone(phone)) {
            throw new BusinessException(ErrorCode.ERROR, "手机号有误");
        }
        //        查缓存里面是否存在
        String loginCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);

        if (!code.equals(loginCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //        查询数据库
        User user = query().eq("phone", phone).one();
        //        不存在 注册用户
        if (user == null) {
            user = this.createUserByPhone(phone);
        }
        String uuId = saveToRedis(user);

        return Results.success(uuId);
    }


    @Override
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        return Results.success(removeUserRedis(request));
    }

    @Override
    public BaseResponse<Boolean> removeByMe(HttpServletRequest request) {
        Long userId = UserHolder.getUser().getUserId();
        return Results.success(this.removeUser(userId) && this.removeUserRedis(request));
    }

    @Override
    public BaseResponse<Boolean> removeByAdmin(Long userId) {
        return Results.success(removeUser(userId));
    }

    @Override
    public BaseResponse<Boolean> updateUser(UpdateUserRequest request) {
        Long userId = UserHolder.getUser().getUserId();
        User oldUser = this.getById(userId);

        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        User user = BeanUtil.toBean(request, User.class);
        boolean isSuccess = this.updateById(user);

        return Results.success(isSuccess);
    }

    @Override
    public BaseResponse<List<UserVo>> getList(int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<User> users = this.page(new Page<>(pageNumber, pageSize)).getRecords();
        List<UserVo> userVos = BeanUtil.copyToList(users, UserVo.class);

        return Results.success(userVos);
    }


    public Boolean removeUser(Long userId) {
        Long sum = this.query().eq("userId", userId).count();
        if (sum == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return this.removeById(userId);
    }

    public Boolean removeUserRedis(HttpServletRequest request) {
        String token = request.getHeader(TOKEN);
        return stringRedisTemplate.delete(LOGIN_USER_KEY+token);
    }

    public String saveToRedis(User user) {
        //        做用户脱敏
        UserVo userVo = toUserVo(user);
        //        生成id，返回给前台
        String uuId = IdUtil.simpleUUID();
        String userVoJson;
        try {
            userVoJson = MAPPER.writeValueAsString(userVo);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        //        以json格式存到缓存里面
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY + uuId, userVoJson, LOGIN_USER_TL, TimeUnit.MINUTES);

        return uuId;
    }

    @Override
    public User createUserByPhone(String phone) {
        User user = new User();
        String account = WYY_USER + RandomUtil.randomNumbers(8);
        user.setNickName(account);
        user.setUserAccount(account);
        user.setPhone(phone);
        String password = DigestUtils.md5DigestAsHex((PASSWORD_PREFIX + account + PASSWORD_SUFFIX).getBytes());
        user.setUserPassword(password);
        save(user);

        return user;
    }

    @Override
    public UserVo getCurrentUser() {
        return UserHolder.getUser();
    }

    /**
     * 用户脱敏
     * @param user
     * @return
     */
    public UserVo toUserVo(User user){
        UserVo userVo = BeanUtil.toBean(user, UserVo.class);
        String tag = user.getTag();
        Gson gson = new Gson();
        java.lang.reflect.Type setResult = new TypeToken<HashMap<Integer, Set<String>>>() {}.getType();
        Set<String> tags = gson.fromJson(tag,setResult);
        userVo.setTag(tags);

        return userVo;
    }
}




