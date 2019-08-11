package com.tensquare.redislock.aop;

import com.tensquare.redislock.anno.NoRepeatSubmit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @date 2019/6/12 15:30
 * @desciption TODO
 */
@Aspect
@Component
public class RepeatSubmitAspect {


    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.tensquare.redislock.anno.NoRepeatSubmit)")
    public void pointCut(){

    }
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        //生产的话需要从theadLocal里面获取用户信息,与request请求,request用来获取请求路径,这里用方法名与写死的userId代替
        String userId="111";
        String methodName = joinPoint.getSignature().getName();
        String key = userId+"_"+methodName;
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        NoRepeatSubmit noRepeatSubmit = signature.getMethod().getAnnotation(NoRepeatSubmit.class);
        long ttl = noRepeatSubmit.ttl();
        //获取锁对象
        RLock lock = redissonClient.getLock(key);
        if(lock.tryLock(1,TimeUnit.SECONDS)){
            Object o=null;
            try {
                o = joinPoint.proceed();
                return o;
            }catch (Exception e){
                throw  e;
            }finally {
                try {
                    lock.unlock();
                }catch (Exception e){
                    logger.error("释放锁出错",e);
                    throw new RuntimeException("释放锁出错");
                }
            }

        }else {
            logger.info("未获取到锁"+key);
            throw new RuntimeException("重复提交,拒绝请求") ;
        }
    }
}
