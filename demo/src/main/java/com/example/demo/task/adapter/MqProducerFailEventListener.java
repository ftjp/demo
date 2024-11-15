package com.example.demo.task.adapter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.enums.TaskTypeEnum;
import com.example.demo.infruastructure.producer.MqProducerFailEvent;
import com.example.demo.task.application.TaskExecutorApplicationService;
import com.example.demo.task.domain.vo.SendMqTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author caili
 * @create 2024/11/8 15:04
 */
@Component
@Slf4j
public class MqProducerFailEventListener implements ApplicationListener<MqProducerFailEvent> {

    @Resource
    private TaskExecutorApplicationService taskExecutorApplicationService;

    /**
     * description: 消息发送失败，则记录重试任务
     *
     * @author: LJP
     * @date: 2024/9/27 10:47
     */
    @Override
    public void onApplicationEvent(MqProducerFailEvent event) {
        String message = event.getMessage();
        log.info("mq发送失败，失败事件监听：message = {}。开始记录重试任务", message);
        JSONObject entries = JSONUtil.parseObj(message);
        SendMqTaskInfo taskInfo = new SendMqTaskInfo();
        if (ObjectUtil.isNotNull(entries.get("topic"))) {
            taskInfo.setTopic(entries.get("topic").toString());
        }
        if (ObjectUtil.isNotNull(entries.get("tag"))) {
            taskInfo.setTag(entries.get("tag").toString());
        }
        if (ObjectUtil.isNotNull(entries.get("msg"))) {
            taskInfo.setMsg(entries.get("msg").toString());
        }
        taskExecutorApplicationService.addTaskAfterError(JSONUtil.toJsonStr(taskInfo), TaskTypeEnum.SEND_MQ, entries.get("errorMessage").toString());
    }


}
