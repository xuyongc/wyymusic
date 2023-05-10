package com.example.wyymusic.service.impl;

import cn.hutool.json.JSONUtil;
import com.example.wyymusic.service.MusicListService;
import com.example.wyymusic.service.MusicService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MusicListServiceImplTest {

    @Resource
    private MusicListService musicListService;

    @Test
    void createList() {
//        musicListService.createList();
    }

    @Test
    void removeList() {
    }

    @Test
    void addBatch() {

    }

    @Test
    void testCreateList() {
    }

    @Test
    void testRemoveList() {
    }

    @Test
    void addMusicBatch() {
    }

    @Test
    void addMusic() {
        musicListService.addMusic(1L,1L);
    }

    @Test
    void getMusicList() {

    }
}