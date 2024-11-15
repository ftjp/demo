package com.example.demo.task.domain.handler;


import com.example.demo.infruastructure.enums.TaskTypeEnum;
import com.example.demo.task.domain.entity.RetryTask;
import org.springframework.stereotype.Service;

/**
 * description: 任务重试策略
 *
 * @author: LJP
 * @date: 2024/9/27 14:37
 */
@Service
public interface TaskRetryService {

    TaskTypeEnum getTaskTypeEnum();

    void retry(RetryTask retryTask);


}
