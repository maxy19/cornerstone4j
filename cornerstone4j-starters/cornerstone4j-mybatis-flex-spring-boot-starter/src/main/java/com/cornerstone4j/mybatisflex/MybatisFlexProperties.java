package com.cornerstone4j.mybatisflex;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基于{@link com.mybatisflex.spring.boot.MybatisFlexProperties}原生注解的扩展属性
 */
@Data
@ConfigurationProperties(prefix = MybatisFlexProperties.MYBATIS_FLEX_PREFIX)
public class MybatisFlexProperties {


    public static final String MYBATIS_FLEX_PREFIX = "mybatis-flex";

    /**
     * 自动填充时间（创建时间，修改时间），默认开启
     */
    private Boolean autoSetTime = Boolean.TRUE;

    /**
     * sql打印，默认关闭
     */
    private Boolean sqlPrint = Boolean.FALSE;

}
