package com.example.wyymusic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.io.File;

import static com.example.wyymusic.constant.CommonConstant.*;

/**
 * @author xyc
 * @CreteDate 2023/4/26 15:45
 **/
@Configuration
public class MvcConfig implements WebMvcConfigurer {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FlushRedisInterceptor(stringRedisTemplate)).order(0);
//        registry.addInterceptor(new LoginInterceptor()).order(1).addPathPatterns("/comment/**","/community/**","/upload/**","/index/set/img").
//                excludePathPatterns("/comment/get","/community/get/feed","/community/get/late");
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/index/set/img","/index/remove/img").order(2);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                //当**Credentials为true时，**Origin不能为星号，需为具体的ip地址【如果接口不带cookie,ip无需设成具体ip】
                .allowedOriginPatterns("http://127.0.0.1:5173/","http://localhost:9528/")
                //是否允许证书 不再默认开启
//                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("*");
                //跨域允许时间
//                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + FILE_UPLOAD_SPACE);
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + IMAGE_UPLOAD_SPACE);
        registry.addResourceHandler("/music/**").addResourceLocations("file:" + MP3_UPLOAD_SPACE);
    }
}
