package com.example.demo.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * description:redis操作工具类
 *
 * @author: LJP
 * @date: 2024/8/16 16:12
 */
@Slf4j
@Component
public class DataRedisOptionUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DataRedisOptionService dataRedisOptionService;

    public static final ThreadLocal<Set<String>> lockThreadLocal = DataRedisOptionService.lockThreadLocal;

    /**
     * 尝试批量创建锁
     *
     * @param keyList     锁的Key列表
     * @param sleepTime   尝试拿锁的等待时间(毫秒)
     * @param releaseTime 锁过期时间(秒) 防止死锁
     * @return 是否所有锁都成功获取
     * @throws InterruptedException
     */
    private boolean lockKeyList(List<String> keyList, long sleepTime, long releaseTime) throws InterruptedException {
        if (keyList == null || keyList.isEmpty() || releaseTime <= 0 || sleepTime <= 0) {
            log.error("keys不能为空或者releaseTime:{}<=0 或者 sleepTime:{}<=0", releaseTime, sleepTime);
            return false;
        }
        // 一开始都未获取锁
        Map<String, Boolean> lockMap = keyList.stream().collect(Collectors.toMap(Function.identity(), key -> false, (v1, v2) -> v1));
        for (int i = 0; i < 20; i++) {
            // 尝试获取锁
            lockMap.entrySet().stream().filter(entry -> !entry.getValue()).forEach(entry -> {
                Boolean isLocked = redisTemplate.opsForValue().setIfAbsent(entry.getKey(), 1, releaseTime, TimeUnit.SECONDS);
                lockMap.put(entry.getKey(), isLocked);
            });
            List<Boolean> unLockList = lockMap.values().stream().filter(boo -> boo != null && !boo).collect(Collectors.toList());
            if (CollUtil.isEmpty(unLockList)) {
                break;
            }
            // 每次失败睡眠 i * sleepTime 毫秒
            Thread.sleep(i * sleepTime);
        }
        // 超过时间还未获取锁，则释放之前占有的锁
        List<String> unLockKeyList = lockMap.entrySet().stream().filter(entry -> Boolean.FALSE.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(unLockKeyList)) {
            List<String> lockList = lockMap.entrySet().stream().filter(entry -> Boolean.TRUE.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
            redisTemplate.delete(lockList);
            log.error(String.format("批量获取锁失败，以获取：【%s】，未获取：【%s】", lockList, unLockKeyList));
            log.debug("线程:{} 批量创建锁结果:{}", Thread.currentThread().getId(), false);
            return false;
        }
        keyList.forEach(key -> {
            Set<String> set = lockThreadLocal.get();
            if (CollUtil.isNotEmpty(set)) {
                set.add(key);
            } else {
                set = new HashSet<>();
                set.add(key);
            }
            lockThreadLocal.set(set);
        });
        log.debug("线程:{} 批量创建锁：{}，结果:{}", Thread.currentThread().getId(), keyList, true);
        return true;
    }

    /**
     * description: 业务逻辑为无参无返回
     *
     * @author: LJP
     * @date: 2024/9/4 16:32
     */
    public void tryLockKey(String key, Runnable action) {
        // 代码可复用消费式接口，返回true即可
        tryLockKey(key, () -> {
            action.run();
            return true;
        });
    }

    public void tryLockKey(List<String> keyList, Runnable action) {
        tryLockKey(keyList, () -> {
            action.run();
            return true;
        });
    }

    /**
     * 根据key删除锁
     *
     * @param keyList 锁的keyList
     */
    private void deleteLockBatch(List<String> keyList) {
        if (CollUtil.isEmpty(keyList)) {
            log.error("key:{}不能为空", keyList);
            return;
        }
        Set<String> localKeySet = lockThreadLocal.get();
        if (CollUtil.isEmpty(localKeySet)) {
            log.info("线程:{} 根据key:{} 删除锁结果:{}", Thread.currentThread().getId(), keyList, false);
            lockThreadLocal.remove();
            return;
        }
        // 当前线程没有持有锁，不可删除
        keyList.stream().filter(key -> !localKeySet.contains(key)).forEach(key -> {
            log.info("线程:{} 根据key:{} 删除锁结果:{}", Thread.currentThread().getId(), key, false);
        });
        // 删除持有的锁
        List<String> deleteKeyList = lockThreadLocal.get().stream().filter(keyList::contains).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(deleteKeyList)) {
            Long delete = redisTemplate.delete(deleteKeyList);
            deleteKeyList.forEach(key -> lockThreadLocal.get().remove(key));
            if (lockThreadLocal.get().isEmpty()) {
                lockThreadLocal.remove();
            }
            if (delete == null || deleteKeyList.size() > delete) {
                log.info("线程:{} 根据deleteKeyList:{} 删除锁个数为:{}", Thread.currentThread().getId(), deleteKeyList, delete);
            }
        }
    }

    /**
     * description: 获得分布式锁，执行完后有返回值
     *
     * @author: LJP
     * @date: 2024/9/4 15:51
     */
    public <T> T tryLockKey(String key, Supplier<T> action) {
        if (action == null || StrUtil.isBlank(key)) {
            log.error("action:{} key:{}不能为空", action, key);
            throw new RuntimeException(String.format("action:[%s] key:[%s]不能为空", action, key));
        }
        // 获取分布式锁
        boolean isLocked = false;
        try {
            // 获取分布式锁，5ms自旋一次，有效期60S
            isLocked = dataRedisOptionService.tryLock(key, 5, 60);
            if (!isLocked) {
                log.error(String.format("获取锁失败:【%s】", key));
                throw new RuntimeException(String.format("获取锁失败:【%s】", key));
            }
            return action.get();
        } catch (Exception e) {
            log.error("error msg:", e);
            throw new RuntimeException("网络异常");
        } finally {
            if (isLocked) {
                dataRedisOptionService.deleteLock(key);
            }
        }
    }

    public <T> T tryLockKey(List<String> keyList, Supplier<T> action) {
        if (action == null || CollUtil.isEmpty(keyList)) {
            log.error("action:{} keyList:{}不能为空", action, keyList);
            throw new RuntimeException(String.format("action:[%s] keyList:[%s]不能为空", action, keyList));
        }
        // 获取分布式锁
        Boolean isLocked = false;
        try {
            // 获取分布式锁，5ms自旋一次，有效期60S
            isLocked = lockKeyList(keyList, 5, 60);
            if (!isLocked) {
                log.error(String.format("批量获取锁失败:【%s】", keyList));
                throw new RuntimeException(String.format("批量获取锁失败:【%s】", keyList));
            }
            return action.get();
        } catch (Exception e) {
            log.error("error msg:", e);
            throw new RuntimeException("网络异常");
        } finally {
            // 已经上锁了的key，需要删除
            if (isLocked) {
                deleteLockBatch(keyList);
            }
        }
    }

}
