package com.example.wyymusic.service;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.model.domain.MusicList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wyymusic.model.vo.MusicListVo;
import com.example.wyymusic.model.vo.MusicListVoAboutUser;
import com.example.wyymusic.model.vo.MusicVo;
import com.example.wyymusic.model.vo.ShowMusicVo;

import java.util.List;

/**
* @author xyc
* @description 针对表【musiclist(歌单)】的数据库操作Service
* @createDate 2023-04-26 09:12:50
*/
public interface MusicListService extends IService<MusicList> {


    /**
     * 创建一个歌单
     * @param showImgUrl
     * @param musicListInfo
     * @return
     */
    BaseResponse<Boolean> createList(String showImgUrl,String musicListInfo);

    /**
     * 删除歌单
     * @param musicListId
     * @return
     */
    BaseResponse<Boolean> removeList(Long musicListId);

    /**
     * 批量添加
     * @param musicListId
     * @param musicIds
     * @return
     */
    BaseResponse<Boolean> addMusicBatch(Long musicListId, List<Long> musicIds);

    /**
     * 添加一个
     * @param musicListId
     * @param musicId
     * @return
     */
    BaseResponse<Boolean> addMusic(Long musicListId, Long musicId);

    /**
     * 删除一个歌单里面的歌曲
     * @param musicListId
     * @param musicId
     * @return
     */
    BaseResponse<Boolean> deleteMusic(Long musicListId, Long musicId);

    /**
     * 获取我创建的歌单
     * @param userId
     * @return
     */
    BaseResponse<List<MusicListVo>> getMusicList(Long userId);

    /**
     * 根据id获取音乐列表
     * @param musicListId
     * @return
     */
    BaseResponse<List<ShowMusicVo>> getMusicByList(Long musicListId);

    /**
     * 获取热点歌单
     * @return
     */
    BaseResponse<List<MusicListVo>> getHotList();

    /**
     * 获取热点列表
     * @return
     */
    BaseResponse<List<MusicListVoAboutUser>> getHotListWithScore();

    /**
     * 查找歌单
     * @param context
     * @param pageNumber
     * @param pageSize
     * @return
     */
    BaseResponse<List<MusicListVo>> searchMusicList(String context, int pageNumber, int pageSize);

    /**
     * 收藏
     * @param musicListId
     * @return
     */
    BaseResponse<Long> likeMusicList(Long musicListId);

    /**
     * 添加收藏
     * @param musicListId
     * @return
     */
    BaseResponse<Boolean> favoriteMusicList(Long musicListId);

    /**
     * 获取我的收藏歌单
     * @return
     */
    BaseResponse<List<MusicListVo>> getFavoriteMusicList();
}
