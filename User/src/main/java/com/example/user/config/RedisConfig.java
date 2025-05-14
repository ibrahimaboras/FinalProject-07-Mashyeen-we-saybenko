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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        // Set up ObjectMapper with Jackson for handling JSON serialization
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()) // For Java 8 Time support
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Avoid using timestamps for dates

        // Use Jackson-based serializer for Redis values
        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(mapper);

        // Configure Redis cache with the custom serializer
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer) // Apply serializer
                );
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // Configure the RedisCacheManager with the cache configuration and connection factory
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration()) // Apply default cache configuration
                .build(); // Return the built RedisCacheManager instance
    }
}

