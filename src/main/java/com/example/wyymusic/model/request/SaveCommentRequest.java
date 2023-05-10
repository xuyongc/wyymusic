package com.example.wyymusic.model.request;

import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/5/5 20:21
 **/
@Data
public class SaveCommentRequest {

    private Long parentId;

    private Long otherId;

    private String content;
}
