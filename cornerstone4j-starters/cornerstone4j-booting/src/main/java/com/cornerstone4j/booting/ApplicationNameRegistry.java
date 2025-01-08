package com.cornerstone4j.booting;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static cn.hutool.core.text.CharSequenceUtil.isBlank;

@Configuration(
        proxyBeanMethods = false
)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ApplicationNameRegistry implements EnvironmentAware {

    @Value("${spring.application.name:default}")
    private String applicationName;

    @Override
    public void setEnvironment(Environment environment) {
        String projectName = "project.name";
        if (isBlank(System.getProperty(projectName))) {
            System.setProperty(projectName, applicationName);
        }
    }

}
