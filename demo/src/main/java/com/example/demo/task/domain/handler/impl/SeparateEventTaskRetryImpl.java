package com.example.demo.task.domain.handler.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.enums.TaskTypeEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.example.demo.infruastructure.util.SpringContextUtil;
import com.example.demo.task.domain.entity.RetryTask;
import com.example.demo.task.domain.handler.TaskRetryService;
import com.example.demo.task.domain.vo.SeparateEventTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * description: 独立事件 事务失败，重试策略实现
 *
 * @author: LJP
 * @date: 2024/9/27 15:50
 */
@Component
@Slf4j
public class SeparateEventTaskRetryImpl implements TaskRetryService {


    private static final ThreadLocal<Boolean> retryThreadLocal = new ThreadLocal<>();

    @Override
    public TaskTypeEnum getTaskTypeEnum() {
        return TaskTypeEnum.SEPARATE_EVENT;
    }

    @Override
    public void retry(RetryTask retryTask) {
        SeparateEventTaskInfo taskInfo = JSONUtil.toBean(retryTask.getTaskInfo(), SeparateEventTaskInfo.class);
        try {
            // 设置当前为重试任务线程，防止死循环（重试失败又新增重试任务）
            setRetryThreadLocal(true);
            invokeTask(taskInfo);
        } finally {
            retryThreadLocal.remove();
        }
    }

    private void setRetryThreadLocal(boolean flag) {
        retryThreadLocal.set(flag);
    }

    public boolean getRetryThreadLoca() {
        return Boolean.TRUE.equals(retryThreadLocal.get());
    }

    public void invokeTask(SeparateEventTaskInfo taskInfo) {
        // 获取目标对象
        Object targetObject = SpringContextUtil.getBean(taskInfo.getClassName());
        // 获取目标类的Class对象
        Class<?> targetClass = targetObject.getClass();
        try {
            // 获取Method对象
            List<Object> parameters = JSONUtil.toList(taskInfo.getParameters(), Object.class);
            Class<?>[] parameterTypes = getParameterTypes(taskInfo.getParametersClassName());
            // 调用方法
            Object[] parameterValues = new Object[parameters.size()];
            for (int i = 0; i < parameters.size(); i++) {
                Object paramValue = parameters.get(i);
                Class<?> paramClass = parameterTypes[i];
                parameterValues[i] = convertParam(paramValue, paramClass);
            }
            Method method = targetClass.getMethod(taskInfo.getMethodName(), parameterTypes);
            method.invoke(targetObject, parameterValues);
        } catch (Exception e) {
            throw new BaseCustomException(String.format("重试独立事件失败：taskInfo=%s；error=%s", JSONUtil.toJsonStr(taskInfo), e.getMessage()));
        }
    }

    public Class<?>[] getParameterTypes(List<String> parameterTypeNames) {
        Class<?>[] parameterTypes = new Class<?>[parameterTypeNames.size()];
        for (int i = 0; i < parameterTypeNames.size(); i++) {
            String typeName = parameterTypeNames.get(i);
            parameterTypes[i] = getPrimitiveClass(typeName);
        }
        return parameterTypes;
    }


    private Object convertParam(Object paramValue, Class<?> paramClass) {
        if (paramValue == null) {
            return null;
        }
        if (paramClass.isAssignableFrom(paramValue.getClass())) {
            return paramValue;
        }
        if (paramClass.equals(String.class)) {
            return paramValue.toString();
        }
        if (paramClass.equals(Integer.class) || paramClass.equals(int.class)) {
            return Integer.parseInt(paramValue.toString());
        }
        if (paramClass.equals(Long.class) || paramClass.equals(long.class)) {
            return Long.parseLong(paramValue.toString());
        }
        if (paramClass.equals(Double.class) || paramClass.equals(double.class)) {
            return Double.parseDouble(paramValue.toString());
        }
        if (paramClass.equals(Boolean.class) || paramClass.equals(boolean.class)) {
            return Boolean.parseBoolean(paramValue.toString());
        }
        if (paramValue.getClass().equals(JSONObject.class)) {
            return JSONUtil.toBean((JSONObject) paramValue, paramClass);
        }
        // 可以根据需要添加更多的类型转换
        throw new IllegalArgumentException("Unsupported parameter type: " + paramClass.getName());
    }

    private Class<?> getPrimitiveClass(String typeName) {
        switch (typeName) {
            case "int":
                return int.class;
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "char":
                return char.class;
            case "double":
                return double.class;
            case "float":
                return float.class;
            case "long":
                return long.class;
            case "short":
                return short.class;
            // ... 其他基本数据类型 ...
            default:
                try {
                    return Class.forName(typeName);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Unknown type: " + typeName);
                }
        }
    }


}
