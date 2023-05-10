package com.example.wyymusic.controller;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.Results;
import com.example.wyymusic.utils.FileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author xyc
 * @CreteDate 2023/4/28 23:20
 **/
@Controller
@RequestMapping("/upLoad")
@ResponseBody
public class FileController {

    @Resource
    private FileUpload fileUpload;

    @PostMapping("/music")
    public BaseResponse<String> musicUpload(MultipartFile musicFile){
        return Results.success(fileUpload.uploadMusic(musicFile));
    }

    @PostMapping("/image")
    public BaseResponse<String> imageUpload(MultipartFile imageFile){
        return Results.success(fileUpload.uploadImage(imageFile));
    }

}
