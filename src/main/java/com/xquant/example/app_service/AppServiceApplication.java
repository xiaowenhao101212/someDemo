package com.xquant.example.app_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 05429
 */
@SpringBootApplication
@MapperScan("com.xquant.example.app_service.mapper")
public class AppServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppServiceApplication.class, args);
		System.out.println("工程啓動成功");
	}

}
