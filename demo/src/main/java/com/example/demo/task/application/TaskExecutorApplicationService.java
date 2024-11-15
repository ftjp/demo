/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.task.application;

import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.enums.TaskTypeEnum;
import com.example.demo.task.domain.service.TaskDomainService;
import com.example.demo.task.domain.vo.SendMqTaskInfo;
import com.example.demo.task.domain.vo.SeparateEventTaskInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * description: 重试任务 操作入口
 *
 * @author: LJP
 * @date: 2024/9/27 10:10
 */
@Service
public class TaskExecutorApplicationService {
    @Resource
    private TaskDomainService taskDomainService;

    /**
     * description: 任务报错后存储记录
     *
     * @author: LJP
     * @date: 2024/9/27 9:57
     */
    public void addTaskAfterError(String taskInfo, TaskTypeEnum taskType, String errorMessage) {
        taskDomainService.addTaskAfterError(taskInfo, taskType, errorMessage);
    }

    /**
     * description: mq发送消息任务
     *
     * @author: LJP
     * @date: 2024/10/31 14:25
     */
    public void addSendMqTaskAfterError(String topic, String tag, String msg, String errorMessage) {
        SendMqTaskInfo taskInfo = new SendMqTaskInfo();
        taskInfo.setTag(tag);
        taskInfo.setTopic(topic);
        taskInfo.setMsg(msg);
        taskDomainService.addTaskAfterError(JSONUtil.toJsonStr(taskInfo), TaskTypeEnum.SEND_MQ, errorMessage);
    }

    /**
     * description: 独立事件任务
     *
     * @author: LJP
     * @date: 2024/10/31 14:25
     */
    public void addSeparateEventTaskAfterError(String className, String methodName, List<String> parametersClass, String parameters, String errorMessage) {
        SeparateEventTaskInfo taskInfo = new SeparateEventTaskInfo();
        taskInfo.setClassName(className);
        taskInfo.setMethodName(methodName);
        taskInfo.setParametersClassName(parametersClass);
        taskInfo.setParameters(parameters);
        taskDomainService.addTaskAfterError(JSONUtil.toJsonStr(taskInfo), TaskTypeEnum.SEPARATE_EVENT, errorMessage);
    }

    /**
     * description: 任务重试
     *
     * @author: LJP
     * @date: 2024/9/27 15:34
     */
    public void retry() {
        taskDomainService.retry();
    }

}
