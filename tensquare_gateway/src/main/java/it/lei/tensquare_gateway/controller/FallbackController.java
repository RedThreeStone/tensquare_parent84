package it.lei.tensquare_gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @date 2019/5/10 17:40
 * @desciption TODO
 */
@RestController
public class FallbackController {
    @RequestMapping(value = "/fallback")
    public String fallback(){
        return "啦啦fallback";
    }
}
