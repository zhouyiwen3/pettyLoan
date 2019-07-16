package com.ps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * H5App
 *
 * @author zzz
 * @date 2019/07/08
 */

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class H5App {
    public static void main( String[] args ){
        SpringApplication.run(H5App.class);
    }

}
