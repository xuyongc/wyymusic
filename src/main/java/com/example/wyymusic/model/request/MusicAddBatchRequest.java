package com.example.wyymusic.model.request;

import lombok.Data;

import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/4/30 15:09
 **/
@Data
public class MusicAddBatchRequest {

    private Long musicListId;

    private List<Long> musicIds;
}
