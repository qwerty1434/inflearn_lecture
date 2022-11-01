package com.example.firstservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/first-service")
@Slf4j
public class FirstServiceController {

    private final Environment env; // yml 파일의 값을 가져오기 위한 변수

    // 생성자 주입
    public FirstServiceController(Environment env){
        this.env = env;
    }


    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the First service.";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("first-request")String header){
        log.info(header);
        return header;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request){
        log.info("Server port ={}",request.getServerPort());
        return String.format("Hi, there THis is a meesage from FirstService %s",env.getProperty("local.server.port"));
    }


}
