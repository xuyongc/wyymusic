package com.example.wyymusic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.model.domain.Music;
import com.example.wyymusic.model.vo.HotMusicVo;
import com.example.wyymusic.model.vo.ListeningVo;
import com.example.wyymusic.model.vo.MusicVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xyc
 * @description 针对表【music(歌曲表)】的数据库操作Service
 * @createDate 2023-04-26 09:12:50
 */
public interface MusicService extends IService<Music> {


    /**
     * 音乐上传
     * @param musicTitle
     * @param lyr
     * @param musicFilePath
     * @param imgPath
     * @return
     */
    BaseResponse<Boolean> musicUpload(String musicTitle, String lyr, String musicFilePath, String imgPath);

    /**
     * 听音乐
     * @param musicId
     * @return
     */
    BaseResponse<MusicVo> listenMusic(Long musicId);

    /**
     * 设置正在听的人
     * @param musicId
     */
    void setListening(Long musicId);

    /**
     * 设置今日热门
     * @param musicId
     */
    void setHotMusic(Long musicId);

    /**
     * 获取听过的用户
     * @param musicId
     * @return
     */
    BaseResponse<List<ListeningVo>> getListening(Long musicId);

    /**
     * 获取热点音乐
     * @return
     */
    BaseResponse<List<HotMusicVo>> getHotMusic();

    /**
     * 点赞
     * @param musicId
     * @return
     */
    BaseResponse<Long> likeMusic(Long musicId);

    /**
     * 搜索歌曲
     * @param context
     * @param pageNumber
     * @param pageSize
     * @return
     */
    BaseResponse<List<MusicVo>> searchMusic(String context, int pageNumber, int pageSize);

    /**
     * 新歌曲
     * @return
     */
    BaseResponse<List<MusicVo>> eAlbum();

    /**
     * 获取喜欢的音乐
     * @return
     */
    BaseResponse<List<MusicVo>> getFavoriteMusic();

    /**
     * 添加新欢音乐
     * @param musicId
     * @return
     */
    BaseResponse<Boolean> favoriteMusic(Long musicId);
}
