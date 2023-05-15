package com.example.wyymusic.controller;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.model.request.MusicUploadRequest;
import com.example.wyymusic.model.dto.SearchRequest;
import com.example.wyymusic.model.vo.HotMusicVo;
import com.example.wyymusic.model.vo.ListeningVo;
import com.example.wyymusic.model.vo.MusicVo;
import com.example.wyymusic.model.vo.ShowMusicVo2;
import com.example.wyymusic.service.MusicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/4/28 17:58
 **/
@Controller
@RequestMapping({"/music"})
@ResponseBody
public class MusicController {
    @Resource
    private MusicService musicService;

    @PostMapping("/upload")
    public BaseResponse<Boolean> musicUpload(@RequestBody MusicUploadRequest request){
        return musicService.musicUpload(request.getMusicTitle(),request.getLyr(),request.getMusicFilePath(),request.getImgPath());
    }

    @GetMapping("/listen")
    public BaseResponse<MusicVo> listenMusic(Long musicId){
        return musicService.listenMusic(musicId);
    }


    @GetMapping("/like")
    public BaseResponse<Long> addMusicLike(Long musicId){
        return musicService.likeMusic(musicId);
    }

    @GetMapping("/listening")
    public BaseResponse<List<ListeningVo>> getListening(Long musicId){
        return musicService.getListening(musicId);
    }

    @GetMapping("/hot")
    public BaseResponse<List<HotMusicVo>> getHotMusic(){
        return musicService.getHotMusic();
    }

    @PostMapping("/search")
    public BaseResponse<List<MusicVo>> searchMusic(@RequestBody SearchRequest request){
        return musicService.searchMusic(request.getContext(),request.getPageNumber(),request.getPageSize());
    }

    @GetMapping("/Album")
    public BaseResponse<List<MusicVo>> getEAlBum(){
        return musicService.eAlbum();
    }

    @GetMapping("/get/favorite")
    public BaseResponse<List<MusicVo>> getFavorite(){
        return musicService.getFavoriteMusic();
    }

    @GetMapping("/get/listened")
    public BaseResponse<List<ShowMusicVo2>> getListened(){
        return musicService.getListened();
    }

    @GetMapping("/get/listened/week")
    public BaseResponse<List<ShowMusicVo2>> getListenedWeek(){
        return musicService.getListenedWeek();
    }

    @GetMapping("/set/favorite")
    public BaseResponse<Boolean> setFavorite(Long musicId){
        return musicService.favoriteMusic(musicId);
    }
}
