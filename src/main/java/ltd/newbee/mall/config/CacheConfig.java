package ltd.newbee.mall.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import ltd.newbee.mall.common.Constants;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig implements CachingConfigurer {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new GenericFastJsonRedisSerializer();
    }

    /**
     * lettuce客户端配置
     *
     * @return LettuceClientConfigurationBuilderCustomizer
     */
    @Bean
    public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
        return clientConfigurationBuilder -> {
            clientConfigurationBuilder.clientOptions(ClientOptions.builder()
                    .socketOptions(SocketOptions.builder()
                            .keepAlive(SocketOptions.KeepAliveOptions.builder()
                                    .enable(true)
                                    .interval(Duration.ofSeconds(10))
                                    .idle(Duration.ofSeconds(30))
                                    .count(3)
                                    .build())
                            .build())
                    .build()
            ).build();
        };
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig())
                .transactionAware()
                .build();
    }

    private RedisCacheConfiguration defaultCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith(Constants.REDIS_KEY_PREFIX)
                .entryTtl(Duration.ofSeconds(300))
                .disableCachingNullValues();
    }


    @Bean(name = "cacheKeyGenerator")
    public KeyGenerator cacheKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

}
