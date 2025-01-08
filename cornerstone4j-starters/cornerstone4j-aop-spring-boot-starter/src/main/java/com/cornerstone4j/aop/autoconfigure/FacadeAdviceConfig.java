package com.cornerstone4j.aop.autoconfigure;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author maxy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = FacadeAdviceConfig.PREFIX)
public class FacadeAdviceConfig extends AbstractAdviceConfig {
    /**
     * 配置前缀
     */
    public static final String PREFIX = "cornerstone4j.aop.log.facade";
    /**
     * 切入点表达式
     */
    private String pointcut = "execution(* com.cornerstone4j..*FacadeImpl.*(..))";
}
