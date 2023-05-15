package com.example.wyymusic.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 动态表（社区表）
 * @author xyc
 * @TableName community
 */
@TableName(value ="community")
@Data
public class Community implements Serializable {
    /**
     * 表ID
     */
    @TableId(type = IdType.AUTO)
    private Long communityId;

    /**
     * 作者Id
     */
    private Long userId;

    /**
     * 图片地址
     */
    private String imgList;

    /**
     * 歌曲推荐地址
     */
    private Long musicId;

    /**
     * 动态文字内容
     */
    private String textContent;

    /**
     * 喜欢数
     */
    private Long communityLikeNumber;

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