package com.tensquare.redislock.anno;

import java.lang.annotation.*;

/**
 * @author Administrator
 * @date 2019/6/12 15:28
 * @desciption TODO
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NoRepeatSubmit {
    //过期时间 秒为单位
    long ttl() default 5L;
}
