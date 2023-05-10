package com.example.wyymusic.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/5/8 18:12
 **/
@Data
public class CommunityFeedVo  {
    private Long minTime;

    private List<CommunityVo> communityVoList;
}
