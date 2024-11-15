package com.example.demo.util;


import com.example.demo.infruastructure.enums.TaskStatusEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.example.demo.infruastructure.util.ParamCheckUtil;
import com.example.demo.task.domain.entity.RetryTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author LJP
 * @create 2024/9/19 16:38
 */
public class ParamCheckUtilTest {

    @Test
    void testAllParam() {
        // 整数
        int a = 0;
        Integer b = 2;
        // 字符串类型
        String string1 = "2";
        // 布尔类型
        boolean aBoolean0 = true;
        Boolean aBoolean1 = Boolean.FALSE;
        // 枚举类型
        TaskStatusEnum enum1 = TaskStatusEnum.READY;
        // 自定义类
        RetryTask retryTask = new RetryTask();
        Object object = new Object();
        // list 参数类型
        List<Integer> list = new ArrayList<>();
        list.add(1);
        // set 参数类型
        Set<Integer> set = new HashSet<>();
        set.add(1);
        // map 参数类型
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        // 数组参数类型
        String[] strings = {"1"};
        int[] ints = {1};
        Integer[] integers = {1};
        // 不同参数检验
        try {
            ParamCheckUtil.checkParamNotNull("a", a, "b", b, "string1", string1, "aBoolean0", aBoolean0, "aBoolean1", aBoolean1
                    , "enum1", enum1, "retryTask", retryTask, "object", object
                    , "list", list, "set", set, "map", map
                    , "strings", strings, "ints", ints, "integers", integers);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("防止方法不报错", exception.getMsg());
        }
    }

    @Test
    void testParam() {
        // 整数
        int a = 0;
        Integer b = 2;
        Integer c = null;
        try {
            ParamCheckUtil.checkParamNotNull("a", a, "b", b, "c", c);
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[c]是null", exception.getMsg());
        }
        // 字符串类型
        String string1 = "2";
        String string2 = "";
        String string3 = null;
        try {
            ParamCheckUtil.checkParamNotNull("string1", string1, "string2", string2, "string3", string3);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[string2]是空字符串", exception.getMsg());
        }
        try {
            // 允许空字符串
            ParamCheckUtil.checkParamNotNull(true, "string1", string1, "string2", string2, "string3", string3);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[string3]是null", exception.getMsg());
        }
        // 布尔类型
        boolean aBoolean0 = true;
        Boolean aBoolean1 = Boolean.FALSE;
        Boolean aBoolean2 = null;
        try {
            ParamCheckUtil.checkParamNotNull("aBoolean0", aBoolean0, "aBoolean1", aBoolean1, "aBoolean2", aBoolean2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[aBoolean2]是null", exception.getMsg());
        }
        // 枚举类型
        TaskStatusEnum enum1 = TaskStatusEnum.READY;
        TaskStatusEnum enum2 = null;
        try {
            ParamCheckUtil.checkParamNotNull("enum1", enum1, "enum2", enum2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[enum2]是null", exception.getMsg());
        }
        // 自定义类
        RetryTask retryTask1 = new RetryTask();
        RetryTask retryTask2 = null;
        try {
            ParamCheckUtil.checkParamNotNull("retryTask1", retryTask1, "retryTask2", retryTask2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[retryTask2]是null", exception.getMsg());
        }

        Object object = new Object();
        Object object1 = null;
        try {
            ParamCheckUtil.checkParamNotNull("object", object, "object1", object1);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[object1]是null", exception.getMsg());
        }

        // 不同参数检验
        try {
            ParamCheckUtil.checkParamNotNull("a", a, "string1", string1, "enum1", enum1, "appointConfig1", retryTask1);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("防止方法不报错", exception.getMsg());
        }
    }

    @Test
    void testCollectionParam() {
        // list 参数类型
        List<Integer> list = new ArrayList<>();
        list.add(1);
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = null;
        try {
            ParamCheckUtil.checkParamNotNull("list", list, "list1", list1, "list2", list2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[list1]是空集合", exception.getMsg());
        }
        try {
            ParamCheckUtil.checkParamNotNull(true, "list", list, "list1", list1, "list2", list2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[list2]是null", exception.getMsg());
        }
        // set 参数类型
        Set<Integer> set = new HashSet<>();
        set.add(1);
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = null;
        try {
            ParamCheckUtil.checkParamNotNull("set", set, "set1", set1, "set2", set2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[set1]是空集合", exception.getMsg());
        }
        try {
            ParamCheckUtil.checkParamNotNull(true, "set", set, "set1", set1, "set2", set2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[set2]是null", exception.getMsg());
        }
        // map 参数类型
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = null;
        try {
            ParamCheckUtil.checkParamNotNull("map", map, "map1", map1, "map2", map2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[map1]是空Map", exception.getMsg());
        }
        try {
            ParamCheckUtil.checkParamNotNull(true, "map", map, "map1", map1, "map2", map2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[map2]是null", exception.getMsg());
        }
        // 不同参数检验
        try {
            ParamCheckUtil.checkParamNotNull("list", list, "set", set, "map", map);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("防止方法不报错", exception.getMsg());
        }
    }

    @Test
    void testArrayParam() {

        String[] strings = {"1"};
        String[] strings1 = {};
        String[] strings2 = null;
        try {
            ParamCheckUtil.checkParamNotNull("strings", strings, "strings1", strings1, "strings2", strings2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[strings1]是空数组", exception.getMsg());
        }
        try {
            ParamCheckUtil.checkParamNotNull(true, "strings", strings, "strings1", strings1, "strings2", strings2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[strings2]是null", exception.getMsg());
        }

        int[] ints = {1};
        int[] ints1 = {};
        int[] ints2 = null;
        try {
            ParamCheckUtil.checkParamNotNull("ints", ints, "ints1", ints1, "ints2", ints2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[ints1]是空数组", exception.getMsg());
        }
        try {
            ParamCheckUtil.checkParamNotNull(true, "ints", ints, "ints1", ints1, "ints2", ints2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[ints2]是null", exception.getMsg());
        }

        Integer[] integers = {1};
        Integer[] integers1 = {};
        Integer[] integers2 = null;
        try {
            ParamCheckUtil.checkParamNotNull("integers", integers, "integers1", integers1, "integers2", integers2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[integers1]是空数组", exception.getMsg());
        }
        try {
            ParamCheckUtil.checkParamNotNull(true, "integers", integers, "integers1", integers1, "integers2", integers2);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("[integers2]是null", exception.getMsg());
        }

        // 不同参数检验
        try {
            ParamCheckUtil.checkParamNotNull("strings", strings, "ints", ints, "integers", integers);
            throw new BaseCustomException("防止方法不报错");
        } catch (BaseCustomException exception) {
            Assertions.assertEquals("防止方法不报错", exception.getMsg());
        }

    }


}
