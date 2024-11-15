package com.example.demo.task.domain.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * description: 任务表
 *
 * @author: LJP
 * @date: 2024/10/31 9:49
 */
@Accessors(chain = true)
@Table(name = "t_sm_task")
@Entity
@Data
public class RetryTask {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 任务类型：1-mq消息发送失败，2-mq消息消费失败，3-远程接口调用失败，4-独立事件事务失败
     */
    private Integer taskType;

    /**
     * 任务信息：JSON格式
     */
    private String taskInfo;

    /**
     * 任务状态：0-待处理，1-处理中，2-处理完成，3-挂起
     */
    private Integer status;

    /**
     * 重试次数，默认为0
     */
    private Integer retryCount;

    /**
     * 版本号，默认为0
     */
    private Integer version;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 详细描述
     */
    private String description;
}
