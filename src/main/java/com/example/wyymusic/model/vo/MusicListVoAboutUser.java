package com.example.wyymusic.model.vo;

import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/5/3 11:45
 **/
@Data
public class MusicListVoAboutUser {

    private Long musicListId;

    private Long userId;

    private String nickName;

    private String musicListImg;

    private String musicListName;

    private String musicListInfo;

    /**
     * 喜欢数
     */
    private Long musicLikeNumber;

    /**
     * 收藏数量
     */
    private Long musicFavoriteNumber;

    /**
     * 比重
     */
    private Double score;
}
