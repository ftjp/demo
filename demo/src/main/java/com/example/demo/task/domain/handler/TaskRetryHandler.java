package com.example.demo.task.domain.handler;


import com.example.demo.infruastructure.enums.TaskTypeEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.example.demo.task.domain.entity.RetryTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LJP
 * @create 2024/9/9 15:05
 */
@Component
@Slf4j
public class TaskRetryHandler {

    private final Map<TaskTypeEnum, TaskRetryService> serviceMap;

    /**
     * description: 自动注入所有实现 TaskRetryService 接口的方法
     *
     * @author: LJP
     * @date: 2024/9/9 15:43
     */
    public TaskRetryHandler(List<TaskRetryService> serviceList) {
        this.serviceMap = serviceList.stream().collect(Collectors.toMap(TaskRetryService::getTaskTypeEnum,
                service -> service, (v1, v2) -> v2));
    }

    public TaskRetryService getService(TaskTypeEnum taskTypeEnum) {
        return serviceMap.get(taskTypeEnum);
    }

    public void retry(RetryTask retryTask) {
        // 根据任务类型获取对应的重试实现类
        TaskRetryService service = getService(TaskTypeEnum.fromCode(retryTask.getTaskType()));
        if (service == null) {
            throw new BaseCustomException(String.format("未实现任务类型枚举类为[%s]的重试实现方法", TaskTypeEnum.fromCode(retryTask.getTaskType()).getName()));
        }
        service.retry(retryTask);
    }


}
