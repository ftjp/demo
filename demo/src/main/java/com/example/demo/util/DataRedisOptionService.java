package com.example.demo.util;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class DataRedisOptionService {
    public static final ThreadLocal<Set<String>> lockThreadLocal = new ThreadLocal();
    private static final Logger log = LoggerFactory.getLogger(DataRedisOptionService.class);
    private RedisTemplate<String, Object> redisTemplate;

    public DataRedisOptionService() {
    }

    @Bean
    public RedisTemplate setRedisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        this.redisTemplate = new RedisTemplate();
        this.redisTemplate.setKeySerializer(stringRedisSerializer);
        this.redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        this.redisTemplate.setHashKeySerializer(stringRedisSerializer);
        this.redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisConnectionFactory.setShareNativeConnection(false);
        this.redisTemplate.setConnectionFactory(redisConnectionFactory);
        return this.redisTemplate;
    }

    public Boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                return this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception var5) {
            Exception e = var5;
            log.error("指定缓存过期时间异常", e);
        }

        return false;
    }

    public Long getExpire(String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public Boolean hasKey(String key) {
        try {
            return this.redisTemplate.hasKey(key);
        } catch (Exception var3) {
            Exception e = var3;
            log.error("判断key是否存在异常", e);
            return false;
        }
    }

    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                this.redisTemplate.delete(key[0]);
            } else {
                Set occ = new HashSet(CollectionUtils.arrayToList(key));
                this.redisTemplate.delete(occ);
            }
        }

    }

    public Object get(String key) {
        return key == null ? null : this.redisTemplate.opsForValue().get(key);
    }

    public List<Object> multiGet(List<String> keyList) {
        ValueOperations<String, Object> valueOperations = this.redisTemplate.opsForValue();
        return valueOperations.multiGet(keyList);
    }

    public boolean set(String key, Object value) {
        try {
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception var4) {
            Exception e = var4;
            log.error("数据保存到redis异常", e);
            return false;
        }
    }

    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                this.set(key, value);
            }

            return true;
        } catch (Exception var6) {
            Exception e = var6;
            log.error("数据保存到redis并设置过期时间异常", e);
            return false;
        }
    }

    public void multiSave(Map<String, Object> source) {
        this.redisTemplate.opsForValue().multiSet(source);
    }

    public void multiSave(Map<String, Object> source, long time) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        this.redisTemplate.executePipelined((RedisCallback<?>) (connection) -> {
            connection.openPipeline();
            source.forEach((key, value) -> {
                connection.set(Objects.requireNonNull(stringRedisSerializer.serialize(key)), Objects.requireNonNull(jackson2JsonRedisSerializer.serialize(value)));
                connection.expire(Objects.requireNonNull(stringRedisSerializer.serialize(key)), TimeUnit.MINUTES.toSeconds(time));
            });
            connection.close();
            return null;
        });
    }

    public Long incr(String key) {
        return this.redisTemplate.opsForValue().increment(key);
    }

    public Long incrAndExpire(String key, long time) {
        Long value = this.redisTemplate.opsForValue().increment(key);
        this.expire(key, time);
        return value;
    }

    public Long incr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        } else {
            return this.redisTemplate.opsForValue().increment(key, delta);
        }
    }

    public Long decr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递减因子必须大于0");
        } else {
            return this.redisTemplate.opsForValue().increment(key, -delta);
        }
    }

    public Object hget(String key, String item) {
        return this.redisTemplate.opsForHash().get(key, item);
    }

    public Map<Object, Object> hmget(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    public List<Object> multiGet(String hashKey, List<String> keyList) {
        HashOperations<String, String, Object> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.multiGet(hashKey, keyList);
    }

    public boolean hmset(String key, Map<String, Object> map) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception var4) {
            Exception e = var4;
            log.error("数据保存到redis异常", e);
            return false;
        }
    }

    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            Exception e = var6;
            log.error("数据保存到redis并设置过期时间异常", e);
            return false;
        }
    }

    public boolean hset(String key, String item, Object value) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception var5) {
            Exception e = var5;
            log.error("数据保存到redis并设置过期时间异常", e);
            return false;
        }
    }

    public boolean hset(String key, String item, Object value, long time) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var7) {
            Exception e = var7;
            log.error("数据保存到redis并设置过期时间异常", e);
            return false;
        }
    }

    public void hdel(String key, Object... item) {
        this.redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hHasKey(String key, String item) {
        return this.redisTemplate.opsForHash().hasKey(key, item);
    }

    public double hincr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, by);
    }

    public double hdecr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, -by);
    }

    public double delByHashKey(String hashKey) {
        HashOperations<String, String, Object> hashOperations = this.redisTemplate.opsForHash();
        Set<String> keys = hashOperations.keys(hashKey);
        this.hdel(hashKey, keys);
        return 1.0;
    }

    public Set<Object> sGet(String key) {
        try {
            return this.redisTemplate.opsForSet().members(key);
        } catch (Exception var3) {
            Exception e = var3;
            log.error("从redis获取数据异常", e);
            return null;
        }
    }

    public Boolean sHasKey(String key, Object value) {
        try {
            return this.redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception var4) {
            Exception e = var4;
            log.error("从redis获取数据异常", e);
            return false;
        }
    }

    public Long sSet(String key, Object... values) {
        try {
            return this.redisTemplate.opsForSet().add(key, values);
        } catch (Exception var4) {
            Exception e = var4;
            log.error("数据保存到redis异常", e);
            return null;
        }
    }

    public Long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = this.redisTemplate.opsForSet().add(key, values);
            if (time > 0L) {
                this.expire(key, time);
            }

            return count;
        } catch (Exception var6) {
            Exception e = var6;
            log.error("数据保存到redis异常", e);
            return null;
        }
    }

    public Long sGetSetSize(String key) {
        try {
            return this.redisTemplate.opsForSet().size(key);
        } catch (Exception var3) {
            Exception e = var3;
            log.error("获取set缓存数据长度异常", e);
            return null;
        }
    }

    public Long setRemove(String key, Object... values) {
        try {
            return this.redisTemplate.opsForSet().remove(key, values);
        } catch (Exception var4) {
            Exception e = var4;
            log.error("删除缓存key异常", e);
            return null;
        }
    }

    public List<Object> lGet(String key, long start, long end) {
        try {
            return this.redisTemplate.opsForList().range(key, start, end);
        } catch (Exception var7) {
            Exception e = var7;
            log.error("获取redis数据异常", e);
            return null;
        }
    }

    public Long lGetListSize(String key) {
        try {
            return this.redisTemplate.opsForList().size(key);
        } catch (Exception var3) {
            Exception e = var3;
            log.error("获取list缓存数据长度异常", e);
            return null;
        }
    }

    public Object lGetIndex(String key, long index) {
        try {
            return this.redisTemplate.opsForList().index(key, index);
        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
            return null;
        }
    }

    public boolean lSet(String key, Object value) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception var4) {
            Exception e = var4;
            e.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, Object value, long time) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            Exception e = var6;
            e.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, List<Object> value) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception var4) {
            Exception e = var4;
            e.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, List<Object> value, long time) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            Exception e = var6;
            e.printStackTrace();
            return false;
        }
    }

    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            this.redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception var6) {
            Exception e = var6;
            e.printStackTrace();
            return false;
        }
    }

    public Long lRemove(String key, long count, Object value) {
        try {
            return this.redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception var6) {
            Exception e = var6;
            e.printStackTrace();
            return null;
        }
    }

    public Long zSetRemove(String key, Object... values) {
        try {
            return this.redisTemplate.opsForZSet().remove(key, values);
        } catch (Exception var4) {
            Exception e = var4;
            log.error("删除缓存key异常", e);
            return null;
        }
    }

    public Long zSetRemoveByScore(String key, double minScore, double maxScore) {
        try {
            return this.redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);
        } catch (Exception var7) {
            Exception e = var7;
            log.error("删除缓存key异常", e);
            return null;
        }
    }

    public Long sZset(String key, Set<ZSetOperations.TypedTuple<Object>> values) {
        try {
            return this.redisTemplate.opsForZSet().add(key, values);
        } catch (Exception var4) {
            Exception e = var4;
            log.error("数据保存到redis异常", e);
            return null;
        }
    }

    public Boolean sZset(String key, Object value, double score) {
        try {
            return this.redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception var6) {
            Exception e = var6;
            log.error("数据保存到redis异常", e);
            return false;
        }
    }

    public Long zSetGetCount(String key) {
        try {
            return this.redisTemplate.opsForZSet().zCard(key);
        } catch (Exception var3) {
            Exception e = var3;
            log.error("从redis获取数据异常", e);
            return null;
        }
    }

    public Set<ZSetOperations.TypedTuple<Object>> zSetGet(String key, int start, int end, int rType) {
        try {
            return rType <= 0 ? this.redisTemplate.opsForZSet().rangeWithScores(key, start, end) : this.redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        } catch (Exception var6) {
            Exception e = var6;
            log.error("从redis获取数据异常", e);
            return null;
        }
    }

    public Double incrementScore(String key, String vKey, double score) {
        try {
            return this.redisTemplate.opsForZSet().incrementScore(key, vKey, score);
        } catch (Exception var6) {
            Exception e = var6;
            log.error("数据保存到redis异常", e);
            return null;
        }
    }

    public Boolean addIfAbsent(String key, String value, double score) {
        try {
            return this.redisTemplate.opsForZSet().addIfAbsent(key, value, score);
        } catch (Exception var6) {
            Exception e = var6;
            log.error("数据保存到redis异常", e);
            return false;
        }
    }

    public Double score(String key, String value) {
        try {
            return this.redisTemplate.opsForZSet().score(key, value);
        } catch (Exception var4) {
            Exception e = var4;
            log.error("数据保存到redis异常", e);
            return null;
        }
    }

    public Long zSetRank(String key, Object value, int rType) {
        try {
            return rType > 0 ? this.redisTemplate.opsForZSet().reverseRank(key, value) : this.redisTemplate.opsForZSet().rank(key, value);
        } catch (Exception var5) {
            Exception e = var5;
            log.error("数据保存到redis异常", e);
            return null;
        }
    }

    public boolean tryLock(String key, long sleepTime, long releaseTime) throws InterruptedException {
        if (!StrUtil.isBlank(key) && releaseTime > 0L && sleepTime > 0L) {
            for (int i = 0; i < 20; ++i) {
                if (this.lock(key, 1, releaseTime)) {
                    return true;
                }

                Thread.sleep((long) i * sleepTime);
            }

            log.debug("thread:{},根据key:{}创建锁结果:{}", Thread.currentThread().getId(), key, false);
            return false;
        } else {
            log.error("key:{}不能为空或者releaseTime:{}<=sleepTime:{}<=0", key, releaseTime, sleepTime);
            return false;
        }
    }

    public boolean lock(String key, int value, long releaseTime) {
        if (!StrUtil.isBlank(key) && releaseTime > 0L) {
            Boolean boo = this.redisTemplate.opsForValue().setIfAbsent(key, value, releaseTime, TimeUnit.SECONDS);
            if (boo != null && boo) {
                Set<String> set = lockThreadLocal.get();
                if (CollectionUtil.isNotEmpty(set)) {
                    set.add(key);
                } else {
                    set = new HashSet();
                    set.add(key);
                }

                lockThreadLocal.set(set);
            }

            return boo != null && boo;
        } else {
            log.error("key:{}不能为空或者releaseTime:{}<=0", key, releaseTime);
            return false;
        }
    }

    public void deleteLock(String key) {
        if (StrUtil.isBlank(key)) {
            log.error("key:{}不能为空", key);
        } else {
            if (lockThreadLocal.get() != null && lockThreadLocal.get().contains(key)) {
                Boolean boo = this.redisTemplate.delete(key);
                if (Boolean.TRUE.equals(boo)) {
                    lockThreadLocal.get().remove(key);
                    if (lockThreadLocal.get().size() == 0) {
                        lockThreadLocal.remove();
                    }
                } else {
                    log.info("thread:{},根据key:{},删除锁结果2:false", Thread.currentThread().getId(), key);
                }
            } else {
                log.info("thread:{},根据key:{},删除锁结果:false", Thread.currentThread().getId(), key);
                lockThreadLocal.remove();
            }

        }
    }
}
