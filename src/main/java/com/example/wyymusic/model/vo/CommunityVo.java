package com.example.wyymusic.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/4/30 23:39
 **/
@Data
public class CommunityVo {
    /**
     * 表ID
     */
    private Long communityId;

    /**
     * 作者Id
     */
    private Long userId;


    private String nickName;

    private String imagePath;
    /**
     * 图片地址
     */
    private List<String> imgList;

    /**
     * 歌曲推荐地址
     */
    private Long musicId;

    /**
     * 动态文字内容
     */
    private String textContent;
}
