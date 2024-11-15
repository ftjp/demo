/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.infruastructure.producer;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.exception.BaseCustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送MQ消息
 *
 * @author ZhongYuan
 * @since 2024-07-17
 */
@Slf4j
@Component
public class MqProducerHandler {

    @Resource
    private RocketMQTemplate rocketMqTemplate;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 发送MQ消息
     *
     * @param topic topic
     * @param tag   tag
     * @param msg   消息内容
     */
    public void sendMq(String topic, String tag, String msg) {
        log.info("开始发送mq topic:{}, tag:{}, msg:{}", topic, tag, msg);
        String topicAndTag = topic;
        if (CharSequenceUtil.isNotBlank(tag)) {
            topicAndTag += ":" + tag;
        }
        try {
            SendResult sendResult = rocketMqTemplate.syncSend(topicAndTag, msg);
            log.info("发送mq结果:{}", JSONUtil.toJsonStr(sendResult));
            if (!sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("发送MQ失败!");
                publishForAddRetryTask(topic, tag, msg, "发送MQ失败!");
            }
        } catch (Exception e) {
            log.error("发送mq异常", e);
            publishForAddRetryTask(topic, tag, msg, "发送MQ失败!");
        }
    }

    /**
     * description: 消息发送失败，则记录重试任务
     *
     * @author: LJP
     * @date: 2024/9/27 10:47
     */
    public void publishForAddRetryTask(String topic, String tag, String msg, String errorMessage) {
        JSONObject object = new JSONObject();
        object.set("topic", topic);
        object.set("tag", tag);
        object.set("msg", msg);
        object.set("errorMessage", errorMessage);
        MqProducerFailEvent failEvent = new MqProducerFailEvent(this, JSONUtil.toJsonStr(object));
        eventPublisher.publishEvent(failEvent);
        log.info("mq发送失败，发布事件：message = {}。开始记录重试任务", failEvent.getMessage());
    }

    public void reSendMq(String topic, String tag, String msg) {
        log.info("重试任务：发送mq topic:{}, tag:{}, msg:{}", topic, tag, msg);
        String topicAndTag = topic;
        if (StrUtil.isNotBlank(tag)) {
            topicAndTag += ":" + tag;
        }
        try {
            SendResult sendResult = rocketMqTemplate.syncSend(topicAndTag, msg);
            log.info("重试任务：发送mq结果:{}", JSONUtil.toJsonStr(sendResult));
            if (!sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                throw new BaseCustomException("重试任务：发送MQ失败!");
            }
        } catch (Exception e) {
            log.error("重试任务：发送mq异常", e);
            throw new BaseCustomException("重试任务：发送MQ失败!");
        }
    }
}
