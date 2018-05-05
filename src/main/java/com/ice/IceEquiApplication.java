package com.ice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月19日
 * @Desc SPRING BOOT 启动
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration
@MapperScan("com.ice.dao")
public class IceEquiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IceEquiApplication.class, args);
    }

}
