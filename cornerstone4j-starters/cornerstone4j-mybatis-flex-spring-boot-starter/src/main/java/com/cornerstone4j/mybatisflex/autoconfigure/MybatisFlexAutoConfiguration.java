package com.cornerstone4j.mybatisflex.autoconfigure;


import com.cornerstone4j.mybatisflex.MybatisFlexProperties;
import com.cornerstone4j.mybatisflex.listener.Cornerstone4jAutoSetTimeListener;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.ConsoleMessageCollector;
import com.mybatisflex.core.audit.MessageCollector;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(
    proxyBeanMethods = false
)
@ComponentScan(
    basePackages = {"com.cornerstone4j.mybatisflex"}
)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, MapperFactoryBean.class})
@EnableConfigurationProperties(MybatisFlexProperties.class)
public class MybatisFlexAutoConfiguration implements MyBatisFlexCustomizer {

    @Resource
    private MybatisFlexProperties properties;


    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        if (properties.getAutoSetTime()) {
            globalConfig.registerInsertListener(Cornerstone4jAutoSetTimeListener.getInstance(), Object.class);
            globalConfig.registerUpdateListener(Cornerstone4jAutoSetTimeListener.getInstance(), Object.class);
        }

        if (properties.getSqlPrint()) {
            //开启审计功能
            AuditManager.setAuditEnable(true);

            //设置 SQL 审计收集器
            MessageCollector collector = new ConsoleMessageCollector();
            AuditManager.setMessageCollector(collector);
        }

    }

}

