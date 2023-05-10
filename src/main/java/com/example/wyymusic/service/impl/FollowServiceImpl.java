package com.example.wyymusic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wyymusic.common.ErrorCode;
import com.example.wyymusic.common.exception.BusinessException;
import com.example.wyymusic.model.domain.Follow;
import com.example.wyymusic.model.vo.FollowVo;
import com.example.wyymusic.service.FollowService;
import com.example.wyymusic.mapper.FollowMapper;
import com.example.wyymusic.utils.UserHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author xyc
* @description 针对表【follow(关注表)】的数据库操作Service实现
* @createDate 2023-04-26 09:12:49
*/
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService{

    @Resource
    private FollowMapper followMapper;


    @Override
    public List<FollowVo> getFriendsList(long userId) {
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return followMapper.selectFollowVoList(userId);
    }

    @Override
    public List<Long> getFansId(){
        Long userId = UserHolder.getUser().getUserId();
        return followMapper.selectMyFansId(userId);
    }


    @Override
    public List<FollowVo> getMyFansList(long userId) {
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // todo sql 语句优化
        List<FollowVo> followVoList = followMapper.selectMyFanList(userId);
        followVoList.forEach(followVo -> {
            QueryWrapper<Follow> followQueryWrapper = new QueryWrapper<>();
            followQueryWrapper.eq("userId", userId).eq("fansId", followVo.getUserId());

            Follow follow = this.getOne(followQueryWrapper);
            if (follow == null) {
                followVo.setIsFollowed(1);
            } else {
                followVo.setIsFollowed(0);
            }
        });
        return followVoList;
    }
}




