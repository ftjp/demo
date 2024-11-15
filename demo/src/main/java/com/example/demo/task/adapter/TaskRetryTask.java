/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.task.adapter;


import com.example.demo.task.application.TaskExecutorApplicationService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * description: 任务重试 定时任务
 *
 * @author: LJP
 * @date: 2024/9/27 15:32
 */
@Slf4j
@Component
public class TaskRetryTask {
    @Resource
    private TaskExecutorApplicationService taskExecutorApplicationService;

    @XxlJob("taskRetryTask")
    public void taskRetryTask() {
        try {
            log.info("taskRetryTask start {} ", XxlJobHelper.getJobParam());
            taskExecutorApplicationService.retry();
            log.info("taskRetryTask end");
        } catch (Exception e) {
            log.error("taskRetryTask error", e);
        }
    }
}
