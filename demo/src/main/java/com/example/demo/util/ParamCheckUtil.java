package com.example.demo.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * description:redis操作工具类
 *
 * @author: LJP
 * @date: 2024/8/16 16:12
 */
@Slf4j
@Component
public class ParamCheckUtil {

    /**
     * description: 校验参数
     *
     * @param params 参数名，值（默认不允许为空 字符串/集合/数组）
     * @author: LJP
     * @date: 2024/9/19 15:34
     */
    public static void checkParamNotNull(Object... params) {
        checkParamNotNull(false, params);
    }

    /**
     * description: 校验参数
     *
     * @param allowEmpty 是否允许为空 字符串/集合/数组
     * @param params     参数名，值
     * @author: LJP
     * @date: 2024/9/19 15:34
     */
    public static void checkParamNotNull(Boolean allowEmpty, Object... params) {
        for (int i = 0; i < params.length; i += 2) {
            String name = params[i].toString();
            Object obj = params[i + 1];
            if (obj == null) {
                throw new RuntimeException(String.format("[%s]是null", name));
            }
            // 是否允许为空 字符串/集合/数组
            if (allowEmpty) {
                continue;
            }
            if (obj instanceof String && CharSequenceUtil.isBlank(((String) obj))) {
                throw new RuntimeException(String.format("[%s]是空字符串", name));
            }
            if (obj instanceof Collection && CollUtil.isEmpty((Collection) obj)) {
                throw new RuntimeException(String.format("[%s]是空集合", name));
            }
            if (obj instanceof Map && CollUtil.isEmpty((Map) obj)) {
                throw new RuntimeException(String.format("[%s]是空Map", name));
            }
            if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
                throw new RuntimeException(String.format("[%s]是空数组", name));
            }
        }
    }

}
