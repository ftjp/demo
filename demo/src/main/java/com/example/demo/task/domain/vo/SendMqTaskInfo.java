package com.example.demo.task.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 发送mq任务信息
 *
 * @author: LJP
 * @date: 2024/10/31 9:49
 */
@Accessors(chain = true)
@Data
public class SendMqTaskInfo {


    /**
     * 消息
     */
    private String msg;

    /**
     * topic
     */
    private String topic;

    /**
     * tag
     */
    private String tag;


}
