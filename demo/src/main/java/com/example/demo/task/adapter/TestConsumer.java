/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.task.adapter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.enums.TaskTypeEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.example.demo.infruastructure.util.ParamCheckUtil;
import com.example.demo.task.application.TaskExecutorApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
@RocketMQMessageListener(topic = "testTopic", selectorExpression = "*", consumerGroup = "test_topic_group")
public class TestConsumer implements RocketMQListener<String> {

    @Lazy
    @Resource
    private TaskExecutorApplicationService taskExecutorApplicationService;

    @Override
    public void onMessage(String message) {
        log.info("TestConsumer MQ:{},message{}", "testTopic", message);
        try {
            JSONObject jsonObject = JSONUtil.parseObj(message);
            Long longParam = jsonObject.getLong("longParam");
            Integer intParam = jsonObject.getInt("intParam");
            String stringParam = jsonObject.getStr("stringParam");
            ParamCheckUtil.checkParamNotNull("longParam", longParam, "intParam", intParam, "stringParam", stringParam);
            log.info("TestConsumer MQ:{},message{},消息消费成功", "testTopic", message);
        } catch (Exception e) {
            log.error("CancelTradeOrderSuccessConsumer error, param=" + message, e);
            addRetryTask(message, e.getMessage());
        }
    }

    /**
     * description: 消息消费失败，则记录重试任务
     *
     * @author: LJP
     * @date: 2024/9/27 10:47
     */
    private void addRetryTask(String message, String errorMessage) {
        JSONObject jsonObject = JSONUtil.parseObj(message);
        // 带有topic，说明是重试消息,不需要再保存记录
        if (StrUtil.isNotBlank(jsonObject.getStr("topic"))) {
            throw new BaseCustomException(errorMessage);
        }
        jsonObject.set("topic", "testTopic");
        // 防止异常过长，截取前225位
        errorMessage = errorMessage.length() <= 225 ? errorMessage : errorMessage.substring(0, 225);
        taskExecutorApplicationService.addTaskAfterError(JSONUtil.toJsonStr(jsonObject), TaskTypeEnum.CONSUMER_MQ, errorMessage);
    }
}
