package com.example.wyymusic.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author xyc
 *
 */
@Data
public class UserVo implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 账户
     */
    private String userAccount;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 背景图片
     */
    private String bgImg;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 标签
     */
    private Set<String> tag;

    /**
     * 0-普通, 1- 作者 2-管理员
     */
    private Integer userRole;
}