package com.ijson.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@ImportResource(locations = {"classpath:applicationContext.xml"})
@SpringBootApplication
@EnableCaching
@PropertySource(value = {"classpath:application.properties","classpath:administrator.properties"})
public class BlogWebBootstrap {

    public static void main(String[] args) {
        //System.setProperty("logback.configurationFile", "/Users/cuiyongxu/logback.xml");
        System.setProperty("file.encoding", "UTF-8");
        SpringApplication.run(BlogWebBootstrap.class, args);
    }

}
