package com.tensquare.redislock.controller;

import com.tensquare.redislock.anno.NoRepeatSubmit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @date 2019/6/12 14:57
 * @desciption TODO
 */
@RestController
public class LockTestController {


    @RequestMapping(value = "/lockTest")
    @NoRepeatSubmit(ttl = 10)
    public String lockTest(){
        return "请求成功";
    }
}
