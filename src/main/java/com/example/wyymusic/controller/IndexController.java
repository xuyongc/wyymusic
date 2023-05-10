package com.example.wyymusic.controller;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.ErrorCode;
import com.example.wyymusic.common.Results;
import com.example.wyymusic.common.exception.BusinessException;
import com.example.wyymusic.model.request.IndexImageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static com.example.wyymusic.constant.CommonConstant.INDEX_IMAGE_MAX_SIZE;
import static com.example.wyymusic.constant.CommonConstant.INDEX_SHOW_IMAGE;

/**
 * @author xyc
 * @CreteDate 2023/5/3 22:46
 **/
@Controller
@RequestMapping("/index")
@ResponseBody
public class IndexController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/set/img")
    public BaseResponse<Long> setIndexImag(IndexImageRequest request){
        Set<String> imageSet = stringRedisTemplate.opsForSet().members(INDEX_SHOW_IMAGE);
        if (imageSet != null && imageSet.size() > INDEX_IMAGE_MAX_SIZE) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "主页图片数量已达最大");
        }
        Long isSuccess = stringRedisTemplate.opsForSet().add(INDEX_SHOW_IMAGE, request.getImage());
        return Results.success(isSuccess);
    }

    @GetMapping("/get/img")
    public BaseResponse<Set<String>> getIndexImg(){
        Set<String> imageSet = stringRedisTemplate.opsForSet().members(INDEX_SHOW_IMAGE);
        return Results.success(imageSet);
    }

    @PostMapping("/remove/img")
    public BaseResponse<Long> removeIndexImg(IndexImageRequest request){
        Long isRemove = stringRedisTemplate.opsForSet().remove(INDEX_SHOW_IMAGE, request.getImage());
        return Results.success(isRemove);
    }
}
