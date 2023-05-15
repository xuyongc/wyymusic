package com.example.wyymusic.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/5/13 10:49
 **/
@Data
public class ShowPlaylistVo {
    /**
     * 表ID
     */
    private Long musicListId;

    /**
     * 作者Id
     */
    private Long userId;

    /**
     * 封面地址
     */
    private String showImgUrl;

    /**
     * 歌单评价
     */
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
