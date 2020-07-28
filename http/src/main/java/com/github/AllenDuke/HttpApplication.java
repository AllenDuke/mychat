package com.github.AllenDuke;


import com.github.AllenDuke.producerService.RPCServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.github.AllenDuke.dao")
public class HttpApplication {

    public static void main(String[] args) throws Exception {
        RPCServer.startServer();
        SpringApplication.run(HttpApplication.class, args);
    }

}
