package com.eh.stock.config.redis;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonLock {

    String value(); // Lock의 이름 (고유값)
    long waitTime() default 15000L; // Lock 획득을 시도하는 최대 시간 (ms)
    long leaseTime() default 15000L; // 락을 획득한 후, 점유하는 최대 시간 (ms)

}
