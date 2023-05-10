package com.example.wyymusic.mapper;

import com.example.wyymusic.model.domain.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wyymusic.model.vo.FollowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author xyc
* @description 针对表【follow(关注表)】的数据库操作Mapper
* @createDate 2023-04-26 09:12:49
* @Entity com.example.wyymusic.model.domain.Follow
*/
public interface FollowMapper extends BaseMapper<Follow> {

    /**
     * 查询我关注的
     * @param userId
     * @return
     */
    List<FollowVo> selectFollowVoList(@Param("userId") long userId);

    /**
     * 查询我的粉丝
     * @param userId
     * @return
     */
    List<FollowVo> selectMyFanList(@Param("userId")long userId);

    /**
     * 查询我的粉丝的id
     * @param userId
     * @return
     */
    List<Long> selectMyFansId(@Param("userId")long userId);
}




