package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.BaseCientImpl;
import com.tensquare.qa.pojo.Label;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "tensquare-base",fallback = BaseCientImpl.class)
public interface BaseClient {

    @RequestMapping(value = "/label/{labelId}", method = RequestMethod.GET)
    Result findById(@PathVariable("labelId") String labelId);

    @RequestMapping(method = RequestMethod.POST)
    Result save(@RequestBody Label label);
}
