package com.cornerstone4j.web.autoconfigure;

import com.cornerstone4j.web.autoconfigure.module.JavaTimeModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;


@Configuration(
    proxyBeanMethods = false
)
@ComponentScan(
    basePackages = {"com.cornerstone4j.web"}
)
public class CustomizeWebAutoConfiguration implements WebMvcConfigurer {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            // Long类型序列化规则，数值超过2^53-1，在JS会出现精度丢失问题，因此Long自动序列化为字符串类型
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);

            // LocalDateTime序列化、反序列化规则
            builder.modules(new JavaTimeModule());
        };
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new StringDecoder());
    }

    private static class StringDecoder implements Formatter<String> {
        @Override
        public String parse(@Nullable String text, @Nullable Locale locale) {
            return URLDecoder.decode(text, StandardCharsets.UTF_8);
        }

        @Override
        public String print(@Nullable String object, @Nullable Locale locale) {
            return object;
        }
    }


}
