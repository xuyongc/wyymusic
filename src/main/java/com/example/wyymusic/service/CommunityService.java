package com.example.wyymusic.service;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.model.domain.Community;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wyymusic.model.vo.CommunityFeedVo;
import com.example.wyymusic.model.vo.CommunityVo;

import java.util.List;

/**
* @author xyc
* @description 针对表【community(动态表（社区表）)】的数据库操作Service
* @createDate 2023-04-26 09:12:49
*/
public interface CommunityService extends IService<Community> {

    /**
     * 添加一个动态
     * @param title
     * @param text
     * @param imgPaths
     * @param musicId
     * @return
     */
    BaseResponse<Boolean> addCommunity(String title, String text, List<String> imgPaths, Long musicId);

    /**
     * 删除一个动态
     * @param commId
     * @return
     */
    BaseResponse<Boolean> removeCommunity(Long commId);

    /**
     * 修改一个动态
     * @param commId
     * @param title
     * @param text
     * @param imgPaths
     * @param musicId
     * @return
     */
    BaseResponse<Boolean> updateCommunity(Long commId, String title, String text, List<String> imgPaths, Long musicId);

    /**
     * 获取动态通过Feed流
     * @param max
     * @param offset
     * @return
     */
    BaseResponse<CommunityFeedVo> getCommunityByFeed(Long max, Integer offset);

    /**
     * 获取最近的动态
     * @return
     */
    BaseResponse<List<CommunityVo>> getLateCommunityVo();

    /**
     * 获取我的所有动态
     * @param userId
     * @return
     */
    BaseResponse<List<CommunityVo>> getMyCommunity(Long userId);
}
