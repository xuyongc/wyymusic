package com.example.wyymusic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.ErrorCode;
import com.example.wyymusic.common.Results;
import com.example.wyymusic.common.exception.BusinessException;
import com.example.wyymusic.mapper.CommunityMapper;
import com.example.wyymusic.model.domain.Community;
import com.example.wyymusic.model.domain.User;
import com.example.wyymusic.model.vo.CommunityFeedVo;
import com.example.wyymusic.model.vo.CommunityVo;
import com.example.wyymusic.service.CommunityService;
import com.example.wyymusic.service.FollowService;
import com.example.wyymusic.service.UserService;
import com.example.wyymusic.utils.FileUpload;
import com.example.wyymusic.utils.UserHolder;
import com.google.gson.Gson;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.wyymusic.constant.CommonConstant.FEED_PREFIX;
import static com.example.wyymusic.constant.CommonConstant.IMAGE_UPLOAD_SPACE;

/**
 * @author xyc
 * @description 针对表【community(动态表（社区表）)】的数据库操作Service实现
 * @createDate 2023-04-26 09:12:49
 */
@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community>
        implements CommunityService {

    @Resource
    private FileUpload fileUpload;

    @Resource
    private FollowService followService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;

    @Override
    public BaseResponse<Boolean> addCommunity(String title, String text, List<String> imgPaths, Long musicId) {
        Long userId = UserHolder.getUser().getUserId();
        isTrue(title, text, imgPaths, musicId, userId);

        Community community = pageComm(title, text, imgPaths, userId, musicId);

        boolean isSuccess = this.save(community);
        if (!isSuccess) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Long> fansId = followService.getFansId();

        if (fansId != null){
            for (Long id : fansId) {
                stringRedisTemplate.opsForZSet().add(FEED_PREFIX + id,community.getCommunityId().toString(),System.currentTimeMillis());
            }
        }

        return Results.success(true);
    }

    @Override
    public BaseResponse<Boolean> removeCommunity(Long commId) {

        Community comm = getById(commId);
        if (comm == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        deleteImg(comm);

        boolean isSuccess = removeById(comm);

        if (!isSuccess) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return Results.success(true);
    }

    @Override
    public BaseResponse<Boolean> updateCommunity(Long commId, String title, String text, List<String> imgPaths, Long musicId) {

        Long userId = UserHolder.getUser().getUserId();
        isTrue(title, text, imgPaths, musicId, userId);

        Community comm = getById(commId);
        if (comm == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        deleteImg(comm);

        Community community = pageComm(title, text, imgPaths, userId, musicId);

        boolean isSuccess = update().eq("communityId", community.getCommunityId())
                .eq("userId",userId)
                .set("textContent", community.getTextContent())
                .set("imgList", community.getImgList())
                .set("musicId", community.getMusicId()).update();

        if (!isSuccess) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return Results.success(true);
    }

    @Override
    public BaseResponse<CommunityFeedVo> getCommunityByFeed(Long max, Integer offset){
        Long userId = UserHolder.getUser().getUserId();

        String key = FEED_PREFIX + userId;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, max, offset, 2);
        if (typedTuples == null || typedTuples.isEmpty()){
            return Results.success();
        }

        List<Long> ids = new ArrayList<>(typedTuples.size());
        long minTime = 0;

        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            ids.add(Long.valueOf(typedTuple.getValue()));
            minTime = typedTuple.getScore().longValue();
        }

        String idStr = StrUtil.join(",", ids);

        List<Community> communityList = this.query().in("communityId",ids).last("ORDER BY FIELD(communityId," + idStr + ")").list();

        List<CommunityVo> communityVos = getCommunityVo(communityList);

        CommunityFeedVo communityFeedVo = new CommunityFeedVo();
        communityFeedVo.setMinTime(minTime);
        communityFeedVo.setCommunityVoList(communityVos);

        return Results.success(communityFeedVo);
    }

    @Override
    public BaseResponse<List<CommunityVo>> getLateCommunityVo(){
        QueryWrapper<Community> communityQueryWrapper = new QueryWrapper<>();
        communityQueryWrapper.orderByDesc("communityId");
        List<Community> communityList = this.page(new Page<>(0, 10 ),communityQueryWrapper).getRecords();

        List<CommunityVo> communityVos = getCommunityVo(communityList);

        return Results.success(communityVos);
    }

    @Override
    public BaseResponse<List<CommunityVo>> getMyCommunity(Long userId) {

        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<Community> communityListList = query().eq("userId", userId).list();

        List<CommunityVo> commVos = communityListList.stream().map(community -> BeanUtil.copyProperties(community, CommunityVo.class)).collect(Collectors.toList());
        return Results.success(commVos);
    }


    public List<CommunityVo> getCommunityVo(List<Community> communityList){
        Gson gson = new Gson();
        return communityList.stream().map(community -> {
            User user = userService.getById(community.getUserId());
            CommunityVo communityVo = BeanUtil.toBean(community, CommunityVo.class);
            List<String> list = gson.fromJson(community.getImgList(), List.class);
            communityVo.setImgList(list);
            communityVo.setNickName(user.getNickName());
            communityVo.setImagePath(user.getAvatarUrl());

            return communityVo;
        }).collect(Collectors.toList());
    }


    public void isTrue(String title, String text, List<String> imgPaths, Long musicId, Long userId) {

        if (title.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (text.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (musicId != null && musicId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (imgPaths == null || imgPaths.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    public void deleteImg(Community comm) {
        String imgList = comm.getImgList();
        Gson gson = new Gson();
        List<String> imgPaths = gson.fromJson(imgList, List.class);
        fileUpload.fileDelete(IMAGE_UPLOAD_SPACE,imgPaths);
    }

    public Community pageComm(String title, String text, List<String> imgPaths, Long userId, Long musicId) {
        Gson gson = new Gson();
        String imgPathJson = gson.toJson(imgPaths);

        Community community = new Community();
        community.setUserId(userId);
        community.setImgList(imgPathJson);
        community.setMusicId(musicId);
        community.setTextContent(text);

        return community;
    }
}




