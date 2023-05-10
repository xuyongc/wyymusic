package com.example.wyymusic.model.vo;

import lombok.Data;

/**
 * @author xyc
 */
@Data
public class FollowVo{

    /**
     * 关注id
     */
    private Long userId;

    /**
     * 关注id
     */
    private Long friendId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 是否相互关注
     */
    private Integer isFollowed;

    /**
     * 头像
     */
    private String avatarUrl;

}