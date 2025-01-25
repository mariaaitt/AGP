package com.traveloffersystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @RequestMapping("/")
    public String index() {
        // 返回 /WEB-INF/views/index.jsp
        return "index";
    }
}
