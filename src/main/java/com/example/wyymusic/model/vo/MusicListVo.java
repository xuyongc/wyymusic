package com.example.wyymusic.model.vo;

import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/4/30 14:27
 **/
@Data
public class MusicListVo {
    private Long musicListId;

    private Long userId;

    private String showImgUrl;

    private String musicListInfo;

    /**
     * 喜欢数
     */
    private Long musicLikeNumber;

    /**
     * 收藏数量
     */
    private Long musicFavoriteNumber;
}
