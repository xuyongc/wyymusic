package com.example.wyymusic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xyc
 */
@SpringBootApplication
@MapperScan("com.example.wyymusic.mapper")
public class WyymusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(WyymusicApplication.class, args);
    }

}
