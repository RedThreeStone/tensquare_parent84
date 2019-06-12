package com.tensquare.redislock.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @date 2019/6/12 15:03
 * @desciption TODO
 */
@Component
public class RedisLockUtil {

    @Autowired
    @Qualifier("myRedisTemplate")
    private RedisTemplate redisTemplate;

    private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    /**
     *
     * @param key key
     * @param value 值
     * @param ttl 过期时间 秒为单位
     * @return
     */
    public boolean lock(String key,String value,Long ttl ){
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, value, ttl, TimeUnit.SECONDS);
        return flag;
    }

    /**
     * 解锁
     * @param key
     * @param value
     * @return
     */
    public boolean unlock(String key,String value){

//        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
//        redisScript.setScriptText(RELEASE_LOCK_LUA_SCRIPT);
//        redisScript.setResultType(Integer.class);
//        redisTemplate.execute(redisScript, Collections.singletonList(key), value);
        // 判断加锁与解锁是不是同一个客户端
        if (value.equals(redisTemplate.opsForValue().get(key))) {
            // 若在此时，这把锁突然不是这个客户端的，则会误解锁
            redisTemplate.delete(key);
            System.out.println("释放锁成功");
        }

        return true;
    }
}
