package com.tensquare.redislock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * @author Administrator
 * @date 2019/6/12 14:51
 * @desciption TODO
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class LockApplication {
    public static void main(String[] args) {
        SpringApplication.run(LockApplication.class, args);
    }

    @Bean(name = "myRedisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer();

        redisTemplate.setDefaultSerializer(redisSerializer);

        return redisTemplate;
    }

    @Bean
    public RedissonClient redissionClient(RedisProperties redisProperties) {
        Config config = new Config();

        config
                //看门狗超时时间（预期时间为三分之一）
                .setLockWatchdogTimeout(5*3*1000)
                //单机模式
                .useSingleServer()
                //redis地址
                .setAddress("redis://"+redisProperties.getHost() + ":" + redisProperties.getPort())
                //redis密码
                .setPassword(redisProperties.getPassword())
                //最小空闲数
                .setConnectionMinimumIdleSize(redisProperties.getLettuce().getPool().getMinIdle())
                //最大连接数量
                .setConnectionPoolSize(redisProperties.getLettuce().getPool().getMaxActive());

        //重试次数，重试间隔 采用默认值
        return Redisson.create(config);
    }
}
