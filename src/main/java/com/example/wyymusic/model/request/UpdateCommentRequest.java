package com.example.wyymusic.model.request;

import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/5/5 20:24
 **/
@Data
public class UpdateCommentRequest {

    private Long userId;

    private Long commentId;

    private String content;
}
