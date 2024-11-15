package com.example.demo.task.aop;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.example.demo.task.application.TaskExecutorApplicationService;
import com.example.demo.task.domain.handler.impl.SeparateEventTaskRetryImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LJP
 * @create 2024/9/26 15:57
 */
@Aspect
@Component
@Slf4j
public class SeparateEventAspect {

    @Resource
    private TaskExecutorApplicationService taskExecutorApplicationService;

    @Resource
    private SeparateEventTaskRetryImpl separateEventTaskRetry;


    @Around("@annotation(separateEvent)")
    public void addSeparateEventTaskAfterError(ProceedingJoinPoint joinPoint, SeparateEvent separateEvent) throws Throwable {
        try {
            // 执行被拦截的方法
            joinPoint.proceed();
        } catch (Exception e) {
            // 获取被拦截方法所在的类
            Object target = joinPoint.getTarget();
            String simpleName = target.getClass().getSimpleName();
            simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
            // 获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取参数
            String parameters = JSONUtil.toJsonStr(joinPoint.getArgs(), JSONConfig.create().setIgnoreNullValue(false));
            // 参数类型
            Class<?>[] parameterTypes = signature.getParameterTypes();
            List<String> parametersClassName = new ArrayList<>();
            for (int i = 0; i < parameterTypes.length; i++) {
                parametersClassName.add(parameterTypes[i].getName());
            }

            log.error(String.format("[%s] error, [args]=%s, errorMsg = %s", signature.getName(), parameters, e.getMessage()));
            // 当前是重试任务线程，只抛出异常，不重复保存任务
            if (separateEventTaskRetry.getRetryThreadLoca()) {
                throw new BaseCustomException(e.getMessage());
            }
            taskExecutorApplicationService.addSeparateEventTaskAfterError(simpleName, signature.getName(), parametersClassName, parameters, e.getMessage());
        }
    }

}
