package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.BaseClient;
import com.tensquare.qa.pojo.Label;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class BaseCientImpl implements BaseClient {
    @Override
    public Result findById(String labelId) {
        return new Result(false, StatusCode.ERROR,"服务出错啦，服务器熔断啦");
    }

    @Override
    public Result save(Label label) {
        return null;
    }
}
