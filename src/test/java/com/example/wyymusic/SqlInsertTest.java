package com.example.wyymusic;

import cn.hutool.json.JSONUtil;
import com.example.wyymusic.model.domain.User;
import com.example.wyymusic.model.vo.UserVo;
import com.example.wyymusic.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;

import static com.example.wyymusic.constant.CommonConstant.PASSWORD_PREFIX;
import static com.example.wyymusic.constant.CommonConstant.PASSWORD_SUFFIX;

/**
 * @author xyc
 * @CreteDate 2023/4/26 8:55
 **/
@SpringBootTest
public class SqlInsertTest {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void insertUser() {
        ArrayList<User> users = new ArrayList<>();
        String password = DigestUtils.md5DigestAsHex((PASSWORD_PREFIX + "123456789" + PASSWORD_SUFFIX).getBytes());
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setUserId((long) i);
            user.setNickName("WYY" + i);
            user.setUserAccount("WYY" + i);
            user.setGender(0);
            user.setUserPassword(password);
            user.setPhone("1718888888");
            user.setEmail("made_by_xu@qq.com");
            ArrayList<String> strings = new ArrayList<>();
            strings.add("在听");
            strings.add("开心");
            strings.add("萝莉控");
            strings.add("原神");
            strings.add("德丽莎世界第一可爱");
            String tagJson = JSONUtil.toJsonStr(strings);
            user.setTag(tagJson);

            users.add(user);
        }

        userService.saveBatch(users);

    }


}
