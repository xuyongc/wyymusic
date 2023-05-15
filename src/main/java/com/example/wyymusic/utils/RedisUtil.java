package com.example.wyymusic.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.example.wyymusic.constant.CommonConstant.MUSIC_HOT;
import static com.example.wyymusic.constant.TimeConstant.MUSIC_HOT_TL;
import static com.example.wyymusic.constant.TimeConstant.MUSIC_LIST_HOT_TL;

/**
 * @author xyc
 * @CreteDate 2023/5/3 11:01
 **/
@Component
public class RedisUtil {

    @Resource
    public StringRedisTemplate stringRedisTemplate;


    public void setHotMusic(String hotKey,Long id){
        Double score = stringRedisTemplate.opsForZSet().score(hotKey, id.toString());
        if (score == null){
            stringRedisTemplate.opsForZSet().add(hotKey,id.toString(),1);
            stringRedisTemplate.expire(hotKey,MUSIC_LIST_HOT_TL, TimeUnit.DAYS);
        }else{
            stringRedisTemplate.opsForZSet().add(hotKey,id.toString(),score + 1);
        }
    }

    public void setScore(String key,Long time,TimeUnit timeType,Long id){
        Double score = stringRedisTemplate.opsForZSet().score(key,id.toString());
        if (score == null){
            stringRedisTemplate.opsForZSet().add(key,id.toString(),1);
            if (time != null){
                stringRedisTemplate.expire(key,time, timeType);
            }
        }else{
            stringRedisTemplate.opsForZSet().add(key,id.toString(),score + 1);
        }
    }

    public Set<String> getKeysByDesc(String key){
        return stringRedisTemplate.opsForZSet().reverseRange(key,0,-1);
    }
}
