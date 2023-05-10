package com.example.wyymusic.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 歌单
 * @author xyc
 * @TableName musiclist
 */
@TableName(value ="musiclist")
@Data
public class MusicList implements Serializable {
    /**
     * 表ID
     */
    @TableId(type = IdType.AUTO)
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
     * 歌曲内容地址
     */
    private String musicListContentIds;

    /**
     * 喜欢数
     */
    private Long musicLikeNumber;

    /**
     * 收藏数量
     */
    private Long musicFavoriteNumber;

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