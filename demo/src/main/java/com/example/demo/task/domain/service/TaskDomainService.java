/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.task.domain.service;

import cn.hutool.core.collection.CollUtil;
import com.example.demo.infruastructure.constant.NumberConstant;
import com.example.demo.infruastructure.enums.BaseResultEnum;
import com.example.demo.infruastructure.enums.TaskStatusEnum;
import com.example.demo.infruastructure.enums.TaskTypeEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.example.demo.infruastructure.util.DataRedisOptionUtil;
import com.example.demo.infruastructure.util.IdGenerator;
import com.example.demo.task.domain.entity.RetryTask;
import com.example.demo.task.domain.handler.TaskRetryHandler;
import com.example.demo.task.domain.helper.RetryTaskQueryHelper;
import com.example.demo.task.domain.mapper.RetryTaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TaskDomainService {

    /**
     * 拉取任务，防止重复拉取，添加分布式锁
     */
    public static final String LOAD_TASK_RETRY_KEY = "marketing:task:retry:lock";
    @Resource
    private RetryTaskMapper retryTaskMapper;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private RetryTaskQueryHelper retryTaskQueryHelper;
    @Resource
    private DataRedisOptionUtil dataRedisOptionUtil;
    @Resource
    private TaskRetryHandler taskRetryHandler;

    /**
     * description: 任务报错后存储记录
     *
     * @author: LJP
     * @date: 2024/9/27 9:57
     */
    public void addTaskAfterError(String taskInfo, TaskTypeEnum taskType, String errorMessage) {
        String description = errorMessage.length() <= 225 ? errorMessage : errorMessage.substring(0, 225);
        RetryTask retryTask = new RetryTask();
        retryTask.setId(idGenerator.nextId());
        retryTask.setTaskType(taskType.getCode());
        retryTask.setRetryCount(NumberConstant.Number_0);
        retryTask.setStatus(TaskStatusEnum.READY.getCode());
        retryTask.setTaskInfo(taskInfo);
        retryTask.setCreateTime(LocalDateTime.now());
        retryTask.setUpdateTime(retryTask.getCreateTime());
        retryTask.setVersion(NumberConstant.Number_0);
        retryTask.setDescription(description);
        retryTaskMapper.save(retryTask);
    }


    public void retry() {
        // 抓取任务
        List<RetryTask> retryTasks = loadTasks();
        if (CollectionUtils.isEmpty(retryTasks)) {
            return;
        }
        // 重试任务
        List<RetryTask> forUpdatedList = new ArrayList<>();
        for (RetryTask retryTask : retryTasks) {
            try {
                taskRetryHandler.retry(retryTask);
                retryTask.setStatus(TaskStatusEnum.FINISHED.getCode());
            } catch (Exception e) {
                log.error(String.format("批任务重试失败，任务ID:【%s】,error:[%s]", retryTask.getId(), e.getMessage()));
                // 防止异常过长，截取前225位
                retryTask.setDescription(e.getMessage().length() <= 225 ? e.getMessage() : e.getMessage().substring(0, 225));
                // 重试次数达到3次，则直接挂起
                retryTask.setStatus(retryTask.getRetryCount() >= NumberConstant.Number_2 ? TaskStatusEnum.PENDING.getCode() : TaskStatusEnum.READY.getCode());
            }
            forUpdatedList.add(retryTask);
        }
        // 更新任务状态
        retryTaskMapper.batchUpdateAfterRetry(forUpdatedList);
    }

    /**
     * description: 查询待重试的任务
     *
     * @author: LJP
     * @date: 2024/9/27 11:40
     */
    private List<RetryTask> loadTasks() {
        return dataRedisOptionUtil.tryLockKey(LOAD_TASK_RETRY_KEY, () -> {
            // 分页抓取任务
            List<RetryTask> retryTasks = retryTaskQueryHelper.getTasks(null, TaskStatusEnum.READY.getCode(), NumberConstant.Number_100);
            if (CollUtil.isEmpty(retryTasks)) {
                return retryTasks;
            }
            List<Long> taskIds = retryTasks.stream().map(RetryTask::getId).collect(Collectors.toList());
            updateTaskStatus(taskIds, TaskStatusEnum.PROCESSING.getCode());
            retryTasks.forEach(task -> task.setVersion(task.getVersion() + 1));
            return retryTasks;
        });
    }

    /**
     * 批量更新任务表的状态
     *
     * @param taskIds 任务ID
     * @param status  新的任务状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskStatus(List<Long> taskIds, int status) {
        int updatedCount = retryTaskMapper.updateStatusByIds(taskIds, status);
        if (updatedCount != taskIds.size()) {
            throw new BaseCustomException(BaseResultEnum.SYSTEM_ERROR.getCode(), BaseResultEnum.SYSTEM_ERROR.getMsg());
        }
    }


}
