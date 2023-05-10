package com.example.wyymusic.model.request;

import com.example.wyymusic.common.PageRequest;
import lombok.Data;

/**
 * @author xyc
 * @CreteDate 2023/5/5 22:27
 **/
@Data
public class SearchRequest  extends PageRequest {
    private String context;
}
