package com.cornerstone4j.aop.autoconfigure;


import cn.hutool.core.collection.CollUtil;
import com.cornerstone4j.aop.advice.ControllerLogAdvice;
import com.cornerstone4j.aop.advice.FacadeLogAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(
        proxyBeanMethods = false
)
@ComponentScan(
        basePackages = {"com.cornerstone4j.aop"}
)
@EnableConfigurationProperties({ControllerAdviceConfig.class, FacadeAdviceConfig.class})
public class LogAdviceAutoConfig {

    @ConditionalOnProperty(prefix = ControllerAdviceConfig.PREFIX, name = "enable", havingValue = "true")
    @Bean("controllerAdviceComponent")
    public AspectJExpressionPointcutAdvisor registrationControllerAdvice(ControllerAdviceConfig config) {
        ControllerLogAdvice advice = new ControllerLogAdvice();
        advice.setConfig(config);
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(getExpression(config.getPointcut(),config));
        advisor.setAdvice(advice);
        if (config.getOrder() != null) {
            advisor.setOrder(config.getOrder());
        }
        return advisor;
    }

    @ConditionalOnProperty(prefix = FacadeAdviceConfig.PREFIX, name = "enable", havingValue = "true")
    @Bean("facadeAdviceComponent")
    public AspectJExpressionPointcutAdvisor registrationFacadeAdvice(FacadeAdviceConfig config) {
        FacadeLogAdvice advice = new FacadeLogAdvice();
        advice.setConfig(config);
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(getExpression(config.getPointcut(),config));
        advisor.setAdvice(advice);
        if (config.getOrder() != null) {
            advisor.setOrder(config.getOrder());
        }
        return advisor;
    }

    private String getExpression(String execExpression, AbstractAdviceConfig config) {
        if (CollUtil.isEmpty(config.getExclude())) {
            return execExpression;
        }
        return execExpression + CollUtil.join(config.getExclude(), "");
    }
}