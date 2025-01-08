package com.cornerstone4j.mybatisflex.listener;


import cn.hutool.core.util.ReflectUtil;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import com.mybatisflex.core.table.TableInfoFactory;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

public class Cornerstone4jAutoSetTimeListener implements InsertListener, UpdateListener {

    private static final String CREATE_TIME = "createTime";

    private static final String UPDATE_TIME = "updateTime";


    /**
     * 获取实例
     *
     * @return {@link Cornerstone4jAutoSetTimeListener}
     */
    public static Cornerstone4jAutoSetTimeListener getInstance() {
        return Holder.INSTANCE;
    }


    @Override
    public void onInsert(Object entity) {
        List<Field> entityFields = TableInfoFactory.getColumnFields(entity.getClass());

        for (Field field : entityFields) {
            if (CREATE_TIME.equals(field.getName()) || UPDATE_TIME.equals(field.getName())) {
                autoSetTimeValue(entity, field);
            }
        }
    }

    @Override
    public void onUpdate(Object entity) {
        List<Field> entityFields = TableInfoFactory.getColumnFields(entity.getClass());

        for (Field field : entityFields) {
            if (UPDATE_TIME.equals(field.getName())) {
                autoSetTimeValue(entity, field);
            }
        }
    }

    /**
     * 自动时间赋值
     *
     * @param entity 实体
     * @param field  字段
     */
    private void autoSetTimeValue(Object entity, Field field) {
        long currentTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        Date currentDate = new Date();

        if (field.getType().equals(Long.class)) {
            ReflectUtil.setFieldValue(entity, field, currentTime);
        }
        if (field.getType().equals(LocalDateTime.class)) {
            ReflectUtil.setFieldValue(entity, field, currentLocalDateTime);
        }
        if (field.getType().equals(Date.class)) {
            ReflectUtil.setFieldValue(entity, field, currentDate);
        }
    }

    private static class Holder {
        private static final Cornerstone4jAutoSetTimeListener INSTANCE = new Cornerstone4jAutoSetTimeListener();
    }

}
