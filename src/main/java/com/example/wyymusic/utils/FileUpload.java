package com.example.wyymusic.utils;

import cn.hutool.core.util.RandomUtil;
import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.ErrorCode;
import com.example.wyymusic.common.Results;
import com.example.wyymusic.common.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.wyymusic.constant.CommonConstant.*;

/**
 * @author xyc
 * @CreteDate 2023/4/28 22:19
 **/
@Component
public class FileUpload {

    public String uploadImage(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        BufferedImage bufferedImage = null;
        try {
            InputStream imageFile = file.getInputStream();
            bufferedImage = ImageIO.read(imageFile);
            imageFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bufferedImage == null) {
            throw new BusinessException(ErrorCode.ERROR, "图片格式只能是bmp/gif/jpg/png");
        }
        if (fileName == null) {
            fileName = "";
        }
        return fileUpload(IMAGE_UPLOAD_SPACE, fileName, file);
    }


    public String uploadMusic(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();

        if (!MUSIC_TYPE.equals(fileType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件格式错误");
        }
        if (fileName == null) {
            fileName = "";
        }
        return fileUpload(MP3_UPLOAD_SPACE, fileName, file);
    }

    public String fileUpload(String path, String fileName, MultipartFile file) {
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDD_HHmmss");
        StringBuilder tempName = new StringBuilder();

        tempName.append(sdf.format(new Date())).append(RandomUtil.randomInt(100, 999)).append(suffixName);
        File fileDirectory = new File(path);
        File destFile = new File(path + tempName);

        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new BusinessException(ErrorCode.ERROR, "文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            return tempName.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    }

    public BaseResponse<Boolean> fileDelete(String filePrefix, List<String> paths) {
        if (paths == null || paths.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        for (String path : paths) {
            fileDeleteOne(filePrefix,path);
        }

        return Results.success(true);
    }

    public BaseResponse<Boolean> fileDelete(String filePrefix, String... path) {
        List<String> pathList = Arrays.stream(path).collect(Collectors.toList());
        return fileDelete(filePrefix, pathList);
    }

    /**
     * 优化file对象重复创建
     * @param filePrefix
     * @param path
     * @return
     */
    private BaseResponse<Boolean> fileDeleteOne(String filePrefix, String path) {
        File file = new File(filePrefix + path);
        boolean isHave = file.exists();
        if (!isHave) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不存在,删除失败");
        }
        boolean isDelete = file.delete();
        if (!isDelete) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "删除失败");
        }
        return Results.success(true);
    }

}
