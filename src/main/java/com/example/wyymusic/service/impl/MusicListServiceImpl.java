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
import com.example.wyymusic.mapper.MusicListMapper;
import com.example.wyymusic.model.domain.Music;
import com.example.wyymusic.model.domain.MusicList;
import com.example.wyymusic.model.vo.*;
import com.example.wyymusic.service.FavoriteService;
import com.example.wyymusic.service.MusicListService;
import com.example.wyymusic.service.MusicService;

import com.example.wyymusic.utils.RedisUtil;
import com.example.wyymusic.utils.UserHolder;
import com.google.gson.Gson;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static com.example.wyymusic.constant.CommonConstant.*;

/**
 * @author xyc
 * @description 针对表【musiclist(歌单)】的数据库操作Service实现
 * @createDate 2023-04-26 09:12:50
 */
@Service
public class MusicListServiceImpl extends ServiceImpl<MusicListMapper, MusicList> implements MusicListService {

    @Resource
    private MusicService musicService;

    @Resource
    private RedisUtil redisutil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FavoriteService favoriteService;

    /**
     * 创建歌单
     *
     * @return
     */
    @Override
    public BaseResponse<Boolean> createList(String showImgUrl,String musicListInfo) {

        Long userId = UserHolder.getUser().getUserId();

        MusicList musiclist = new MusicList();
        musiclist.setUserId(userId);
        musiclist.setShowImgUrl(showImgUrl);
        musiclist.setMusicListInfo(musicListInfo);

        boolean isSuccess = this.save(musiclist);

        return Results.success(isSuccess);
    }

    /**
     * 删除歌单
     *
     * @param musicListId
     * @return
     */
    @Override
    public BaseResponse<Boolean> removeList(Long musicListId) {
        if (musicListId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<MusicList> deleteWrapper = new QueryWrapper<MusicList>().eq("musicListId", musicListId).eq("userId",UserHolder.getUser().getUserId());

        boolean isSuccess = remove(deleteWrapper);

        return Results.success(isSuccess);
    }
    
    /**
     * 批量加入歌曲
     *
     * @param musicListId
     * @param musicIds
     * @return
     */
    @Override
    public BaseResponse<Boolean> addMusicBatch(Long musicListId, List<Long> musicIds) {

        if (musicIds == null || musicIds.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        MusicList musicList = isHaveMusicList(musicListId);

        BiPredicate<List<Long>, Set<Long>> biFunction = (List<Long> ids, Set<Long> set) -> set.addAll(ids);

        Boolean isSuccess = updateMusicList(musicListId, biFunction, musicIds, musicList);

        return Results.success(isSuccess);
    }

    /**
     * 加入单曲
     *
     * @param musicListId
     * @param musicId
     * @return
     */
    @Override
    public BaseResponse<Boolean> addMusic(Long musicListId, Long musicId) {
        isHaveMusic(musicId);

        MusicList musicList = isHaveMusicList(musicListId);


        BiPredicate<Long, Set<Long>> biFunction = (Long id, Set<Long> set) -> set.add(id);

        Boolean isSuccess = updateMusicList(musicListId, biFunction, musicId, musicList);

        return Results.success(isSuccess);
    }

    /**
     * 歌单里面删除一个音乐
     *
     * @param musicListId
     * @param musicId
     * @return
     */
    @Override
    public BaseResponse<Boolean> deleteMusic(Long musicListId, Long musicId) {

        isHaveMusic(musicId);
        MusicList musicList = isHaveMusicList(musicListId);

        BiPredicate<Long, Set<Long>> biFunction = (Long id, Set<Long> set) -> set.remove(id);

        Boolean isSuccess = updateMusicList(musicListId, biFunction, musicId, musicList);

        return Results.success(isSuccess);
    }


    /**
     * 获取我的创建的歌单
     *
     * @param userId
     * @return
     */
    @Override
    public BaseResponse<List<MusicListVo>> getMusicList(Long userId) {
        
        List<MusicList> musicLists = query().eq("userId", userId).list();

        List<MusicListVo> musicListVos = musicLists.stream().map(musicList -> BeanUtil.toBean(musicList, MusicListVo.class)).collect(Collectors.toList());

        return Results.success(musicListVos);
    }



    /**
     * 根据歌单id获取里面音乐列表
     *
     * @param musicListId
     * @return
     */
    @Override
    public BaseResponse<List<ShowMusicVo>> getMusicFromPlaylist(Long musicListId) {
        //判断歌单是否存在
        MusicList musicList = isHaveMusicList(musicListId);
        //设置redis热点+1
        redisutil.setHotMusic(MUSIC_LIST_HOT, musicListId);
        //获取歌曲列表
        String musicIds = musicList.getMusicListContentIds();
        //转为set
        Set<Long> set = setMusicIdsToSet(musicIds);
        //批量查询
        List<Music> musics = musicService.query().in("musicId", set).list();
        List<ShowMusicVo> showMusicVos = musics.stream().map(music -> BeanUtil.copyProperties(music, ShowMusicVo.class)).collect(Collectors.toList());
        return Results.success(showMusicVos);
    }


    @Override
    public BaseResponse<List<MusicListVo>> getHotList() {
        Set<String> ids = stringRedisTemplate.opsForZSet().reverseRange(MUSIC_LIST_HOT, 0, -1);
        if (ids == null || ids.isEmpty()) {
            return Results.success(new ArrayList<>());
        }
        String idStr = StrUtil.join(",", ids);
        List<MusicList> musicLists = query().in("musicListId", ids).last("ORDER BY FIELD(musicListId," + idStr + ")").list();
        if (musicLists == null) {
            return Results.success(new ArrayList<>());
        }
        List<MusicListVo> musicVos = musicLists.stream().map(musicList -> BeanUtil.copyProperties(musicList, MusicListVo.class)).collect(Collectors.toList());
        return Results.success(musicVos);
    }

    @Override
    public BaseResponse<List<MusicListVoAboutUser>> getHotListWithScore() {
        UserVo user = UserHolder.getUser();
//        判断用户是否为空
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断是否为管理员
        if (user.getUserRole() != ADMIN_CODE) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //获取歌单id
        Set<ZSetOperations.TypedTuple<String>> musicListIdWithScore = stringRedisTemplate.opsForZSet().reverseRangeWithScores(MUSIC_LIST_HOT, 0, -1);
        if (musicListIdWithScore == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "目前没有歌曲被访问");
        }
        throw new BusinessException(ErrorCode.NULL_ERROR);
    }


    @Override
    public BaseResponse<List<MusicListVo>> searchMusicList(String context, int pageNumber, int pageSize) {
        if (context == null || context.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<MusicList> musicListQueryWrapper = new QueryWrapper<>();
        musicListQueryWrapper.like("musicListInfo", context);
        List<MusicList> musicLists = this.page(new Page<>(pageNumber, pageSize), musicListQueryWrapper).getRecords();
        if (musicLists == null) {
            return Results.success(new ArrayList<>());
        }

        List<MusicListVo> musicVoList = BeanUtil.copyToList(musicLists, MusicListVo.class);
        return Results.success(musicVoList);
    }

    @Override
    public BaseResponse<Long> likeMusicList(Long musicListId) {
        if (musicListId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = UserHolder.getUser().getUserId();

        String key = MUSIC_LIST_LIKE_USER + userId;
        Boolean isLiked = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        if (Boolean.FALSE.equals(isLiked)) {
            boolean isSuccess = update().setSql("musicLikeNumber = musicLikeNumber + 1").eq("musicListId", musicListId).update();
            if (isSuccess) {
                stringRedisTemplate.opsForSet().add(key, userId.toString());
            }
        } else {
            boolean isSuccess = update().setSql("musicLikeNumber = musicLikeNumber - 1").eq("musicListId", musicListId).update();
            if (isSuccess) {
                stringRedisTemplate.opsForSet().remove(key, userId.toString());
            }
        }
        return Results.success();
    }

    @Override
    public BaseResponse<Boolean> favoriteMusicList(Long musicListId){
        return favoriteService.setFavorite(MUSIC_LIST_PREFIX,musicListId);
    }

    /**
     * 获取我的喜欢的音乐
     *
     * @return
     */
    @Override
    public BaseResponse<List<MusicListVo>> getFavoriteMusicList() {
        BiFunction<String, Set<Long>, List<MusicListVo>> biFunction = (String column, Set<Long> set) -> {
            List<MusicList> musicLists = this.query().in(column, set).list();
            return BeanUtil.copyToList(musicLists, MusicListVo.class);
        };
       return favoriteService.getFavorite(MUSIC_LIST_PREFIX, "musicListId", biFunction);
    }

    /**
     * 修改通用
     *
     * @param musicListId
     * @param biPredicate
     * @param ids
     * @param <ID>
     * @return
     */
    private <ID> Boolean updateMusicList(Long musicListId, BiPredicate<ID, Set<Long>> biPredicate, ID ids, MusicList musicList) {
        if (musicList == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该歌单不存在");
        }

        String musicIds = musicList.getMusicListContentIds();

        Gson gson = new Gson();
        Set<Long> set = gson.fromJson(musicIds, Set.class);
        if (set == null) {
            set = new HashSet<>();
        }

        boolean isSet = biPredicate.test(ids, set);

        if (!isSet) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR);
        }

        return update().eq("musicListId", musicListId).eq("userId",UserHolder.getUser().getUserId()).set("musicListContentIds", gson.toJson(set)).update();
    }

    /**
     * 音乐是否存在
     *
     * @param musicId
     */
    private void isHaveMusic(Long musicId) {
        if (musicId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long isHave = query().eq("musicId", musicId).count();


        if (isHave <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该歌曲不存在");
        }
    }

    /**
     * 判断歌单是否存在
     *
     * @param musicListId
     * @return
     */
    private MusicList isHaveMusicList(Long musicListId) {
        if (musicListId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        MusicList musicList = query().eq("musicListId", musicListId).one();

        if (musicList == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该歌单不存在");
        }

        return musicList;
    }

    private Set<Long> setMusicIdsToSet(String musicIds) {
        Gson gson = new Gson();
        Set<Long> set = gson.fromJson(musicIds, Set.class);
        if (set == null) {
            set = new HashSet<>();
        }
        return set;
    }
}




