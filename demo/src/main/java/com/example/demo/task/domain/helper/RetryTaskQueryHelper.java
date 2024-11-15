/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.task.domain.helper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.infruastructure.constant.NumberConstant;
import com.example.demo.task.domain.entity.RetryTask;
import com.example.demo.task.domain.mapper.RetryTaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务查询
 *
 * @author ZhongYuan
 * @since 2023/12/26 16:54
 **/
@Service
@Slf4j
public class RetryTaskQueryHelper {
    @Resource
    private RetryTaskMapper retryTaskMapper;

    /**
     * 查询任务列表
     *
     * @param taskType   任务类型
     * @param taskStatus 任务状态
     * @param limit      记录数
     * @return 任务列表
     */
    public List<RetryTask> getTasks(Integer taskType, Integer taskStatus, Integer limit) {
        return retryTaskMapper.list(new LambdaQueryWrapper<RetryTask>().eq(taskType != null, RetryTask::getTaskType, taskType)
                .eq(RetryTask::getStatus, taskStatus)
                .orderBy(true, true, RetryTask::getUpdateTime)
                .last("limit " + NumberConstant.Number_0 + "," + limit)
        );
    }

    public List<RetryTask> getListById(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return new ArrayList<>();
        }
        return retryTaskMapper.listByIds(idList);
    }

}

