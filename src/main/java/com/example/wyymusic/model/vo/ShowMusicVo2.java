package com.example.wyymusic.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author xyc
 * @CreteDate 2023/5/15 23:15
 **/
@Data
public class ShowMusicVo2 {
    /**
     * 音乐Id
     */
    private Long musicId;

    /**
     * 音乐标题
     */
    private String musicTitle;

    /**
     * 作者名字
     */
    private String nickname;

    /**
     * 封面地址
     */
    private String showImgUrl;

    /**
     * 作品地址
     */
    private String musicAddress;
}
