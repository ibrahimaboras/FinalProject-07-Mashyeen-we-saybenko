//package com.example.user.config;
//
//import com.example.user.model.User;
//import com.example.user.model.UserProfile;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableCaching
//public class RedisConfig {
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        // Default cache configuration
//        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofSeconds(60)) // Default TTL for all caches
//                .disableCachingNullValues()
//                .serializeValuesWith(
//                        RedisSerializationContext.SerializationPair.fromSerializer(
//                                new Jackson2JsonRedisSerializer<>(Object.class)));
//
//        // Custom configurations for different entities
//        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//
//        // Cache configuration for users
//        cacheConfigurations.put("users",
//                defaultConfig.serializeValuesWith(
//                        RedisSerializationContext.SerializationPair.fromSerializer(
//                                new Jackson2JsonRedisSerializer<>(User.class))));
//
////        // Cache configuration for flights
////        cacheConfigurations.put("flights",
////                defaultConfig.serializeValuesWith(
////                        RedisSerializationContext.SerializationPair.fromSerializer(
////                                new Jackson2JsonRedisSerializer<>(Flight.class))));
//
//        // Cache configuration for UserProfile
//        cacheConfigurations.put("userProfiles", defaultConfig.serializeValuesWith(
//                RedisSerializationContext.SerializationPair.fromSerializer(
//                        new Jackson2JsonRedisSerializer<>(UserProfile.class))));
//
//        // Return the cache manager with the custom configurations
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(defaultConfig)
//                .withInitialCacheConfigurations(cacheConfigurations)
//                .build();
//    }
//
//}
//
//
//

package com.example.user.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Duration;
import java.util.Set;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration(ObjectMapper objectMapper) {
        objectMapper = objectMapper.copy();
        objectMapper = objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(1))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer(objectMapper)));
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        ObjectMapper objmapper = new ObjectMapper();

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration(objmapper))
                .initialCacheNames(Set.of("usersByFullName", "usersByEmail"))
                .build();
    }
}

