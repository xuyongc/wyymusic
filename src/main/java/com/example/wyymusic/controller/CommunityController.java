package com.example.wyymusic.controller;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.Results;
import com.example.wyymusic.model.domain.Community;
import com.example.wyymusic.model.request.CommunityAddRequest;
import com.example.wyymusic.model.vo.CommunityFeedVo;
import com.example.wyymusic.model.vo.CommunityVo;
import com.example.wyymusic.service.CommunityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/4/30 18:12
 **/
@Controller
@RequestMapping("/community")
@ResponseBody
public class CommunityController {

    @Resource
    private CommunityService communityService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addCommunity(@RequestBody CommunityAddRequest request) {
        return communityService.addCommunity(request.getTitle(), request.getText(), request.getImgList(), request.getMusicId());
    }

    @GetMapping("/get/feed/sql")
    public BaseResponse<List<CommunityVo>> getCommunityBySqlFeed(int pageNumber, int pageSize){
        return communityService.getCommunityBySqlFeed(pageNumber,pageSize);
    }

    @GetMapping("/get/feed")
    public BaseResponse<CommunityFeedVo> getCommunityByFeed(Long max, Integer offset) {
        return communityService.getCommunityByFeed(max, offset);
    }

    @GetMapping("/get/late")
    public BaseResponse<List<CommunityVo>> getLateCommunityVo() {
        return communityService.getLateCommunityVo();
    }

    @GetMapping("/remove")
    public BaseResponse<Boolean> removeCommunity(Long commId){
        return communityService.removeCommunity(commId);
    }
}
