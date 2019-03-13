package org.cat73.pager.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Nonnull;

@SpringBootApplication
@MapperScan(basePackages = "org.cat73.pager.application.**.mapper")
public class PagerTestApplication {
    public static void main(@Nonnull String[] args) {
        SpringApplication.run(PagerTestApplication.class, args);
    }
}
