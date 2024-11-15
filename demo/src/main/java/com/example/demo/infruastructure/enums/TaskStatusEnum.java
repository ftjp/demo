/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.infruastructure.enums;

import lombok.Getter;

/**
 * 任务状态枚举类
 *
 * @author ZhongYuan
 * @since 2023-12-26
 */
@Getter
public enum TaskStatusEnum {
    /**
     * 待处理
     */
    READY(0, "待处理"),
    PROCESSING(1, "处理中"),
    FINISHED(2, "处理完成"),
    PENDING(3, "挂起"),
    ;

    /**
     * 编码
     */
    private final Integer code;
    /**
     * 名称
     */
    private final String name;

    TaskStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}