package com.example.wyymusic.controller;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.model.domain.MusicList;
import com.example.wyymusic.model.request.MusicAddBatchRequest;
import com.example.wyymusic.model.request.SaveMusicListRequest;
import com.example.wyymusic.model.request.SearchRequest;
import com.example.wyymusic.model.vo.MusicListVo;
import com.example.wyymusic.model.vo.MusicListVoAboutUser;
import com.example.wyymusic.model.vo.MusicVo;
import com.example.wyymusic.model.vo.ShowMusicVo;
import com.example.wyymusic.service.MusicListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/4/29 23:37
 **/
@Controller
@RequestMapping("/music/list")
@ResponseBody
public class MusicListController {

    @Resource
    private MusicListService musicListService;

    /**
     * 创建歌单
     * @param request
     * @return
     */
    @PostMapping("/create")
    public BaseResponse<Boolean> createMusicList(@RequestBody SaveMusicListRequest request){
        return musicListService.createList(request.getShowImgUrl(),request.getMusicListInfo());
    }

    /**
     * 删除歌单
     * @param musicListId
     * @return
     */
    @GetMapping("/removeList")
    public BaseResponse<Boolean> removeMusicList(Long musicListId){
        return musicListService.removeList(musicListId);
    }

    /**
     * 单曲批量添加到歌单
     * @param request
     * @return
     */
    @PostMapping("/add/batch")
    public BaseResponse<Boolean> addMusicBatch(@RequestBody MusicAddBatchRequest request){
        return musicListService.addMusicBatch(request.getMusicListId(),request.getMusicIds());
    }

    /**
     * 单曲添加到歌单
     * @param musicListId
     * @param musicId
     * @return
     */
    @GetMapping("/add/one")
    public BaseResponse<Boolean> addMusic(Long musicListId, Long musicId){
        return musicListService.addMusic(musicListId,musicId);
    }

    /**
     * 单曲从歌单里面删除
     * @param musicListId
     * @param musicId
     * @return
     */
    @GetMapping("/delete/one")
    public BaseResponse<Boolean> deleteMusic(Long musicListId, Long musicId){
        return musicListService.deleteMusic(musicListId,musicId);
    }

    /**
     * 获取某个人创建的歌单
     * @param userId
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<List<MusicListVo>> getMusicList(Long userId){
        return musicListService.getMusicList(userId);
    }

    /**
     * 获取歌单里面的所有歌曲
     * @param musicListId
     * @return
     */
    @GetMapping("/get/music")
    public BaseResponse<List<ShowMusicVo>> getMusicByList(Long musicListId){
        return musicListService.getMusicByList(musicListId);
    }

    /**
     * 获取热门歌单
     * @return
     */
    @GetMapping("/get/hot")
    public BaseResponse<List<MusicListVo>> getHotList(){
        return musicListService.getHotList();
    }

    /**
     * 管理员获取歌单和分数，便于排序
     * @return
     */
    @GetMapping("/admin/get/hot")
    public BaseResponse<List<MusicListVoAboutUser>> getHotListWithScore(){
        return musicListService.getHotListWithScore();
    }

    /**
     * 搜索歌单
     * @param request
     * @return
     */
    @PostMapping("/search")
    public BaseResponse<List<MusicListVo>> searchMusic(@RequestBody SearchRequest request){
        return musicListService.searchMusicList(request.getContext(),request.getPageNumber(),request.getPageSize());
    }

    /**
     * 给歌单点赞
     * @param musicListId
     * @return
     */
    @GetMapping("/like")
    public BaseResponse<Long> likeMusicList(Long musicListId) {
        return musicListService.likeMusicList(musicListId);
    }

    /**
     * 获取我收藏的歌单
     * @return
     */
    @GetMapping("/get/favorite")
    public BaseResponse<List<MusicListVo>> getFavorite(){
        return musicListService.getFavoriteMusicList();
    }

    /**
     * 把歌单放到我的收藏夹
     * @param musicListId
     * @return
     */
    @GetMapping("/set/favorite")
    public BaseResponse<Boolean> setFavorite(Long musicListId){
        return musicListService.favoriteMusicList(musicListId);
    }
}
