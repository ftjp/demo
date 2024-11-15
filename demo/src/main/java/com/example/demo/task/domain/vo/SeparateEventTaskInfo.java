package com.example.demo.task.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: 独立事件任务信息
 *
 * @author: LJP
 * @date: 2024/10/31 9:49
 */
@Accessors(chain = true)
@Data
public class SeparateEventTaskInfo {


    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类名
     */
    private List<String> parametersClassName;

    /**
     * 参数(这里如果是list，参数为null，用json转会丢失数据)
     */
    private String parameters;


}
