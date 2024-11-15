/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.infruastructure.enums;

import lombok.Getter;

/**
 * 任务类型枚举类
 *
 * @author ZhongYuan
 * @since 2023-12-26
 */
@Getter
public enum TaskTypeEnum {


    CONSUMER_MQ(1, "消费mq消息"),
    SEND_MQ(2, "发送mq消息"),
    FEIGN(3, "远程接口调用"),
    SEPARATE_EVENT(4, "独立事件事务"),


    ;

    /**
     * 编码
     */
    private final Integer code;
    /**
     * 名称
     */
    private final String name;

    TaskTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TaskTypeEnum fromCode(int code) {
        for (TaskTypeEnum type : TaskTypeEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException(TaskTypeEnum.class.getSimpleName() + ":code找不到对应的枚举: " + code);
    }

}