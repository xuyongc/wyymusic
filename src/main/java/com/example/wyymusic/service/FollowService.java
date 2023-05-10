package com.example.wyymusic.service;

import com.example.wyymusic.model.domain.Follow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wyymusic.model.vo.FollowVo;

import java.util.List;

/**
* @author xyc
* @description 针对表【follow(关注表)】的数据库操作Service
* @createDate 2023-04-26 09:12:50
*/
public interface FollowService extends IService<Follow> {

    /**
     * 我关注的
     * @param userId
     * @return
     */
    List<FollowVo> getFriendsList(long userId);

    /**
     * 获取我的粉丝的id
     * @return
     */
    List<Long> getFansId();

    /**
     * 获取我的粉丝
     * @param userId
     * @return
     */
    List<FollowVo> getMyFansList(long userId);
}
