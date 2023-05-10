package com.example.wyymusic.model.request;

import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/5/5 20:26
 **/
@Data
public class RemoveCommentRequest {
    private Long userId;

    private Long commentId;
}
