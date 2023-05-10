package com.example.wyymusic.service;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.model.domain.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

/**
* @author xyc
* @description 针对表【favorite(收藏夹)】的数据库操作Service
* @createDate 2023-04-26 09:12:49
*/
public interface FavoriteService extends IService<Favorite> {

    /**
     * 获取我收藏的
     * @param prefix
     * @param column
     * @param biFunction
     * @param <R>
     * @return
     */
    <R> BaseResponse<List<R>> getFavorite(int prefix, String column, BiFunction<String, Set<Long>, List<R>> biFunction);


    /**
     * 添加收藏
     * @param prefix
     * @param id
     * @return
     */
    BaseResponse<Boolean> setFavorite(int prefix, Long id);
}
