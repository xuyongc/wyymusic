package com.example.wyymusic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.ErrorCode;
import com.example.wyymusic.common.Results;
import com.example.wyymusic.common.exception.BusinessException;
import com.example.wyymusic.model.domain.Music;
import com.example.wyymusic.model.domain.User;
import com.example.wyymusic.model.vo.*;
import com.example.wyymusic.service.FavoriteService;
import com.example.wyymusic.service.MusicService;
import com.example.wyymusic.mapper.MusicMapper;
import com.example.wyymusic.service.UserService;
import com.example.wyymusic.utils.FileUpload;
import com.example.wyymusic.utils.RedisUtil;
import com.example.wyymusic.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static com.example.wyymusic.constant.CommonConstant.*;
import static com.example.wyymusic.constant.TimeConstant.*;

/**
 * @author xyc
 * @description 针对表【music(歌曲表)】的数据库操作Service实现
 * @createDate 2023-04-26 09:12:50
 */
@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music>
        implements MusicService {

    @Resource
    private FavoriteService favoriteService;

    @Resource
    private UserService userService;

    @Resource
    private FileUpload fileUpload;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public BaseResponse<Boolean> musicUpload(String musicTitle, String lyr, String musicFilePath, String imgFilePath) {
        if (StringUtils.isAnyBlank(musicTitle, lyr, musicFilePath, imgFilePath)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        UserVo user = UserHolder.getUser();
        Long userId = user.getUserId();
        Integer userRole = user.getUserRole();
        if (userRole != AUTHOR_CODE) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "你不是作者");
        }

        Music music = new Music();
        music.setMusicTitle(musicTitle);
        music.setMusicUserId(userId);
        music.setShowImgUrl(imgFilePath);
        music.setMusicAddress(musicFilePath);
        music.setMusicWord(lyr);

        boolean isSave = this.save(music);

        if (!isSave) {
            fileUpload.fileDelete(IMAGE_UPLOAD_SPACE, imgFilePath);
            fileUpload.fileDelete(MP3_UPLOAD_SPACE, musicFilePath);
        }

        return Results.success(true);
    }

    @Override
    public BaseResponse<MusicVo> listenMusic(Long musicId) {

        if (musicId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Music music = query().eq("musicId", musicId).one();

        if (music == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "该歌曲不存在");
        }

        UserVo user = UserHolder.getUser();
        if (user != null) {
            Long userId = user.getUserId();
            //设置正在听
            setListening(musicId, userId);
            //设置热点排行
            redisUtil.setHotMusic(MUSIC_HOT, musicId);
            //        设置我的听歌排行（永久）
            setListened(musicId, userId, LISTENED, null);
            //        设置我的听歌排行（七天）
            setListened(musicId, userId, LISTENED, LISTENED_WEEK_TL);
        }
        MusicVo musicVo = BeanUtil.copyProperties(music, MusicVo.class);
        return Results.success(musicVo);
    }


    public void setListening(Long musicId, Long userId) {
        String musicKey = MUSIC_LISTENING_USER + musicId;
        stringRedisTemplate.opsForZSet().add(musicKey, userId.toString(), System.currentTimeMillis());
        stringRedisTemplate.expire(musicKey, LISTENING_USER_TL, TimeUnit.MINUTES);
    }

    @Override
    public void setHotMusic(Long musicId) {
        String hotKey = MUSIC_HOT;
        Double score = stringRedisTemplate.opsForZSet().score(hotKey, musicId);
        if (score == null) {
            stringRedisTemplate.opsForZSet().add(hotKey, musicId.toString(), 1);
            stringRedisTemplate.expire(hotKey, MUSIC_HOT_TL, TimeUnit.HOURS);
        } else {
            stringRedisTemplate.opsForZSet().add(hotKey, musicId.toString(), score + 1);
        }
    }

    @Override
    public BaseResponse<List<ListeningVo>> getListening(Long musicId) {
        String musicKey = MUSIC_LISTENING_USER + musicId;

        Set<String> ids = stringRedisTemplate.opsForZSet().range(musicKey, 0, 4);

        if (ids == null || ids.isEmpty()) {
            return Results.success(new ArrayList<>());
        }

        List<Long> userIds = ids.stream().map(Long::valueOf).collect(Collectors.toList());

        List<ListeningVo> listeningPeoples = userService.query().in("userId", userIds).list().stream().map(user -> BeanUtil.copyProperties(user, ListeningVo.class)).collect(Collectors.toList());

        return Results.success(listeningPeoples);
    }

    @Override
    public BaseResponse<List<HotMusicVo>> getHotMusic() {
        Set<String> ids = stringRedisTemplate.opsForZSet().reverseRange(MUSIC_HOT, 0, -1);

        if (ids == null || ids.isEmpty()) {
            return Results.success(new ArrayList<>());
        }

        List<Long> musicIds = ids.stream().map(Long::valueOf).collect(Collectors.toList());
        String idStr = StrUtil.join(",", musicIds);
        List<HotMusicVo> hotMusic = this.query().in("musicId", musicIds).last("ORDER BY FIELD(musicListId," + idStr + ")").list().stream().map(music -> BeanUtil.copyProperties(music, HotMusicVo.class)).collect(Collectors.toList());

        return Results.success(hotMusic);
    }

    @Override
    public BaseResponse<Long> likeMusic(Long musicId) {
        if (musicId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = UserHolder.getUser().getUserId();

        String key = MUSIC_LIKE_USER + userId;
        Boolean isLiked = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        if (Boolean.FALSE.equals(isLiked)) {
            boolean isSuccess = update().setSql("musicLikeNumber = musicLikeNumber + 1").eq("musicId", musicId).update();
            if (isSuccess) {
                stringRedisTemplate.opsForSet().add(key, userId.toString());
            }
        } else {
            boolean isSuccess = update().setSql("musicLikeNumber = musicLikeNumber - 1").eq("musicId", musicId).update();
            if (isSuccess) {
                stringRedisTemplate.opsForSet().remove(key, userId.toString());
            }
        }
        return Results.success();
    }


    @Override
    public BaseResponse<List<MusicVo>> searchMusic(String context, int pageNumber, int pageSize) {
        if (context == null || context.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<Music> musicQueryWrapper = new QueryWrapper<>();
        musicQueryWrapper.like("musicTitle", context);
        List<Music> musics = this.page(new Page<>(pageNumber, pageSize), musicQueryWrapper).getRecords();
        if (musics == null) {
            return Results.success(new ArrayList<>());
        }

        List<MusicVo> musicVoList = BeanUtil.copyToList(musics, MusicVo.class);
        return Results.success(musicVoList);
    }

    @Override
    public BaseResponse<List<MusicVo>> eAlbum() {
        QueryWrapper<Music> musicQueryWrapper = new QueryWrapper<>();
        musicQueryWrapper.orderByDesc("musicId");

        List<Music> musics = this.page(new Page<>(0, 15), musicQueryWrapper).getRecords();
        if (musics == null) {
            return Results.success(new ArrayList<>());
        }

        List<MusicVo> musicVoList = BeanUtil.copyToList(musics, MusicVo.class);
        return Results.success(musicVoList);
    }

    /**
     * 获取我的喜欢的音乐
     *
     * @return
     */
    @Override
    public BaseResponse<List<MusicVo>> getFavoriteMusic() {
        BiFunction<String, Set<Long>, List<MusicVo>> biFunction = (String column, Set<Long> set) -> {
            List<Music> musics = this.query().in(column, set).list();
            return BeanUtil.copyToList(musics, MusicVo.class);
        };
        return favoriteService.getFavorite(MUSIC_PREFIX, "musicId", biFunction);
    }


    @Override
    public BaseResponse<List<ShowMusicVo2>> getListened() {
        Long userId = UserHolder.getUser().getUserId();
        return Results.success(getListened(LISTENED + userId));
    }

    @Override
    public BaseResponse<List<ShowMusicVo2>> getListenedWeek() {
        Long userId = UserHolder.getUser().getUserId();
        return Results.success(getListened(LISTENED_WEEK + userId));
    }

    public List<ShowMusicVo2> getListened(String prefix) {
        Long userId = UserHolder.getUser().getUserId();
        Set<String> musicIds = redisUtil.getKeysByDesc(prefix + userId);
        String idStr = CharSequenceUtil.join(",", musicIds);
        List<Music> musicList = this.query().in("musicId", musicIds).last("ORDER BY FIELD(musicListId," + idStr + ")").list();
        return toShowMusicVoList(musicList);
    }


    public void setListened(Long musicId, Long userId, String prefix, Long time) {
        //todo 只能保存100个
        redisUtil.setScore(prefix + userId, time, TimeUnit.DAYS, musicId);
    }

//    public ShowMusicVo2 toShowMusicVo(Music music) {
//
//    }

    public List<ShowMusicVo2> toShowMusicVoList(List<Music> musicList) {
        return musicList.stream().map(music -> {
            //todo 重复创建对象消耗内存
            User user = userService.query().eq("userId", music.getMusicId()).one();
            ShowMusicVo2 showMusicVo2 = BeanUtil.toBean(music, ShowMusicVo2.class);
            showMusicVo2.setNickname(user.getNickName());
            return showMusicVo2;
        }).collect(Collectors.toList());
    }

    @Override
    public BaseResponse<Boolean> favoriteMusic(Long musicId) {
        return favoriteService.setFavorite(MUSIC_PREFIX, musicId);
    }
}




