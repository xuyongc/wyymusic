package com.example.wyymusic.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xyc
 * @CreteDate 2023/4/28 18:01
 **/
@Data
public class MusicUploadRequest {

    private String musicTitle;

    private String lyr;

    private String musicFilePath;

    private String imgPath;
}
