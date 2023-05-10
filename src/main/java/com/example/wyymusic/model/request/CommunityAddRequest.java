package com.example.wyymusic.model.request;

import lombok.Data;

import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/4/30 20:41
 **/
@Data
public class CommunityAddRequest {
    private String title;

    private String text;

    private List<String> imgList;

    private Long musicId;
}
