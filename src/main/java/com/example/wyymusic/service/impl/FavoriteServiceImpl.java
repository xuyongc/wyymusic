package com.example.wyymusic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.ErrorCode;
import com.example.wyymusic.common.Results;
import com.example.wyymusic.common.exception.BusinessException;
import com.example.wyymusic.model.domain.Favorite;
import com.example.wyymusic.model.domain.User;
import com.example.wyymusic.service.FavoriteService;
import com.example.wyymusic.mapper.FavoriteMapper;
import com.example.wyymusic.utils.UserHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;

import static com.example.wyymusic.constant.CommonConstant.MUSIC_LIST_PREFIX;
import static com.example.wyymusic.constant.CommonConstant.MUSIC_PREFIX;

/**
 * @author xyc
 * @description 针对表【favorite(收藏夹)】的数据库操作Service实现
 * @createDate 2023-04-26 09:12:49
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite>
        implements FavoriteService {


    @Override
    public <R> BaseResponse<List<R>> getFavorite(int prefix, String column, BiFunction<String, Set<Long>, List<R>> biFunction) {
        Long userId = UserHolder.getUser().getUserId();
        Favorite userFavorite = this.query().eq("userId", userId).one();

        if (userFavorite == null) {
            return Results.success(new ArrayList<>());
        }

        Gson gson = new Gson();
        java.lang.reflect.Type mapResult = new TypeToken<HashMap<Integer, Set<Long>>>() {}.getType();
        Map<Integer, Set<Long>> map = gson.fromJson(userFavorite.getMusics(), mapResult);

        Set<Long> idSet = map.get(prefix);
        List<R> list = biFunction.apply(column, idSet);
        return Results.success(list);
    }

    @Override
    public BaseResponse<Boolean> setFavorite(int prefix, Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long userId = UserHolder.getUser().getUserId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Favorite userFavorite = this.query().eq("userId", userId).one();

        if (userFavorite == null){
            userFavorite = new Favorite();
            userFavorite.setUserId(userId);
        }
        Gson gson = new Gson();
        java.lang.reflect.Type mapResult = new TypeToken<HashMap<Integer, Set<Long>>>() {}.getType();
        Map<Integer, Set<Long>> map = gson.fromJson(userFavorite.getMusics(), mapResult);

        if (map == null){
            map = new HashMap<>();
            map.put(MUSIC_LIST_PREFIX,new HashSet<>());
            map.put(MUSIC_PREFIX,new HashSet<>());
        }

        Set<Long> longs = map.get(prefix);
        longs.add(id);
        map.put(prefix, longs);

        String favJson = gson.toJson(map);
        userFavorite.setMusics(favJson);
        boolean isSave = this.saveOrUpdate(userFavorite);
        return Results.success(isSave);
    }

}




