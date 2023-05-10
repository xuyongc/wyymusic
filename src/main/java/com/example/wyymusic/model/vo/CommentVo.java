package com.example.wyymusic.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/5/5 20:08
 **/
@Data
public class CommentVo {

    private Long userId;

    private String nickName;

    private String imagePath;

    private Long commentId;

    private String content;

    private List<CommentVo> childList;
}
