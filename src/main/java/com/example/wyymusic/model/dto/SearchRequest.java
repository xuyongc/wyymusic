package com.example.wyymusic.model.dto;

import com.example.wyymusic.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xyc
 * @CreteDate 2023/5/5 22:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchRequest  extends PageRequest {
    private String context;
}
