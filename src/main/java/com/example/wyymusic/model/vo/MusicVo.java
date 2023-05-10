package com.example.wyymusic.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 歌曲表
 * @author xyc
 * @TableName music
 */
@Data
public class MusicVo implements Serializable {
    /**
     * 音乐Id
     */
    private Long musicId;

    /**
     * 音乐标题
     */
    private String musicTitle;

    /**
     * 作者Id
     */
    private Long musicUserId;

    /**
     * 封面地址
     */
    private String showImgUrl;

    /**
     * 作品地址
     */
    private String musicAddress;

    /**
     * 歌词
     */
    private String musicWord;

    /**
     * 作者
     */
    private String author;

    /**
     * 喜欢数
     */
    private Long musicLikeNumber;

    /**
     * 收藏数量
     */
    private Long musicFavoriteNumber;
}