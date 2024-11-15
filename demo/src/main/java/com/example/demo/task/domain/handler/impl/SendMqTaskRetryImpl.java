package com.example.demo.task.domain.handler.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.enums.TaskTypeEnum;
import com.example.demo.infruastructure.producer.MqProducerHandler;
import com.example.demo.task.domain.entity.RetryTask;
import com.example.demo.task.domain.handler.TaskRetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * description: 发送mq消息 失败，重试策略实现
 *
 * @author: LJP
 * @date: 2024/9/27 15:50
 */
@Component
@Slf4j
public class SendMqTaskRetryImpl implements TaskRetryService {


    @Resource
    @Lazy
    private MqProducerHandler mqProducerHandler;

    @Override
    public TaskTypeEnum getTaskTypeEnum() {
        return TaskTypeEnum.SEND_MQ;
    }

    @Override
    public void retry(RetryTask retryTask) {
        // 根据不同的主题，执行不同的重试逻辑
        JSONObject jsonObject = JSONUtil.parseObj(retryTask.getTaskInfo());
        String msg = (String) jsonObject.get("msg");
        String topic = (String) jsonObject.get("topic");
        String tag = (String) jsonObject.get("tag");
        mqProducerHandler.reSendMq(topic, tag, msg);
    }


}
