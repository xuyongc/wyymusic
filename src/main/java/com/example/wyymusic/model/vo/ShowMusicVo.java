package com.example.wyymusic.model.vo;

import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/5/3 0:11
 **/
@Data
public class ShowMusicVo {
    /**
     * 音乐Id
     */
    private Long musicId;

    /**
     * 音乐标题
     */
    private String musicTitle;

    /**
     * 封面地址
     */
    private String showImgUrl;
}
