package com.example.wyymusic.mapper;

import com.example.wyymusic.model.domain.MusicList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
* @author xyc
* @description 针对表【musiclist(歌单)】的数据库操作Mapper
* @createDate 2023-04-26 09:12:50
* @Entity com.example.wyymusic.model.domain.Musiclist
*/
public interface MusicListMapper extends BaseMapper<MusicList> {

    /**
     * 获取歌单通过id列表
     * @param ids
     * @return
     */
    List<MusicList> selectMusicListByIds(Set<String> ids);
}




