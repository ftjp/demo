/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.task.domain.mapper;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.task.domain.entity.RetryTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务mapper
 *
 * @author ZhongYuan
 * @since 2023-12-26
 */
@Mapper
public interface RetryTaskMapper extends IService<RetryTask> {
    /**
     * 批量更新任务状态
     *
     * @param taskIds 任务ID
     * @param status  新的状态
     * @return 更新记录数
     */
    int updateStatusByIds(List<Long> taskIds, int status);

    /**
     * 批量更新任务
     *
     * @param retryTasks 任务表
     * @return 更新记录数
     */
    int batchUpdate(@Param("tasks") List<RetryTask> retryTasks);


    /**
     * description: 重试后更新任务
     *
     * @author: LJP
     * @date: 2024/9/27 15:30
     */
    int batchUpdateAfterRetry(@Param("tasks") List<RetryTask> retryTasks);

}
