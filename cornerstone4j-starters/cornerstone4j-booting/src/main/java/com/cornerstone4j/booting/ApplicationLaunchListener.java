package com.cornerstone4j.booting;


import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Configuration(
        proxyBeanMethods = false
)
@ComponentScan({"com.cornerstone4j.booting"})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ApplicationLaunchListener {
    /**
     * line separator
     */
    public static final String LINE_SEPARATOR = System.lineSeparator();
    /**
     * Application Name
     */
    private static final String APPLICATION_NAME = "spring.application.name";
    public static final String SPRING_BOOT_GITHUB_URL = "https://github.com/spring-projects/spring-boot";

    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart(WebServerInitializedEvent event) {
        WebServerApplicationContext context = event.getApplicationContext();
        Environment environment = context.getEnvironment();
        String appName = Optional.ofNullable(environment.getProperty(APPLICATION_NAME)).orElse("default");
        int localPort = event.getWebServer().getPort();
        String profile = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
        String host = Optional.ofNullable(NetUtil.getLocalhostStr()).orElse("localhost");
        String startInfo = String.format("[%s]启动完成，当前使用的地址:[%s],端口:[%d],环境变量:[%s]", appName, host, localPort, profile);
        if (log.isInfoEnabled()) {
            log.info(buildBannerText(startInfo));
        }
    }

    private String buildBannerText(String startInfo) {
        return LINE_SEPARATOR +
                LINE_SEPARATOR +
                " :: Spring Boot (v" + SpringBootVersion.getVersion() + ") : " +
                SPRING_BOOT_GITHUB_URL +
                LINE_SEPARATOR +
                " :: 启动信息 : (" + startInfo + ")" +
                LINE_SEPARATOR;
    }
}
