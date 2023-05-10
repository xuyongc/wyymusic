package com.example.wyymusic.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 歌曲表
 * @author xyc
 * @TableName music
 */
@TableName(value ="music")
@Data
public class Music implements Serializable {
    /**
     * 音乐Id
     */
    @TableId(type = IdType.AUTO)
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
     * 喜欢数
     */
    private Long musicLikeNumber;

    /**
     * 收藏数量
     */
    private Long musicFavoriteNumber;

    /**
     * 状态 0-正常 1-审核中 2-下架
     */
    private Integer musicStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 逻辑-1删除 -0存在
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}