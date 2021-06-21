package ltd.newbee.mall.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具类
 **/
@Component
public class RedisCache {

    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * string类型递增
     *
     * @param key 缓存的键值
     * @return 递增后返回值
     */
    public Long increment(final String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * string类型递减
     *
     * @param key redis键
     * @return 递减后返回值
     */
    public Long decrement(final String key) {
        return redisTemplate.opsForValue().decrement(key);

    }

    /**
     * string类型原子递减，不小于-1
     *
     * @param key redis键
     * @return 递减后返回值
     */
    public Long luaDecrement(final String key) {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(buildLuaDecrScript(), Long.class);
        Number execute = (Number) redisTemplate.execute(redisScript, Collections.singletonList(key));
        return execute.longValue();
    }

    /**
     * lua原子自减脚本
     */
    private String buildLuaDecrScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) < 0 then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('decr',KEYS[1])" +
                "\nreturn c;";
    }

    /**
     * 设置redis键值对
     *
     * @param key   redis键
     * @param value redis值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置redis键值对
     *
     * @param key      redis键
     * @param value    redis值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置过期时间
     *
     * @param key     redis键
     * @param timeout 超时时间
     * @return boolean
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     *
     * @param key     redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象
     *
     * @param key redis键
     * @return redis值
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key redis键
     * @return boolean
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return 删除个数
     */
    public long deleteObject(final Collection<Object> collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 删除指定前缀的key
     */
    public long deleteLikesKeyObject(String prefix) {
        return redisTemplate.delete(getLikesKeyList(prefix));
    }

    /**
     * 获取指定前缀键值对
     */
    public <T> List<T> getLikesKeyList(String prefix) {
        // 获取所有的key
        Set<String> keys = redisTemplate.keys(prefix);
        // 批量获取数据
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 缓存Set
     *
     * @param key   缓存键值
     * @param value 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> long setCacheSet(final String key, final Object value) {
        Long count = redisTemplate.opsForSet().add(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 判断key-set中是否存在value
     *
     * @param key   缓存键值
     * @param value 缓存的数据
     * @return boolean
     */
    public Boolean containsCacheSet(final String key, final Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 缓存Map
     *
     * @param key     redis键
     * @param dataMap map对象
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

}
