package com.example.demo.infruastructure.enums;

import java.util.HashMap;
import java.util.Map;

public enum BaseResultEnum {
    SUCCESS("00", "成功"),
    SYSTEM_ERROR("01", "网络繁忙，请稍后再试"),
    PARAM_ERROR("02", "参数错误"),
    FIND_NULL_DATA("03", "未查到数据"),
    WEB_DEAL_ERROR("91", "特殊返回码，需要前端特殊处理"),
    AUTH_ERROR("7777", "权限验证失败，无效的请求"),
    SPECIAL_ERROR("98", "内部特殊处理逻辑"),
    CUSTOMIZE_ERROR("99", "模块内容异常,自己定义描述");

    private final String code;
    private final String msg;

    BaseResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static BaseResultEnum getByCode(String code) {
        BaseResultEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            BaseResultEnum avalue = var1[var3];
            if (avalue.getCode().equals(code)) {
                return avalue;
            }
        }

        return null;
    }

    public static String getMsgByCode(String code) {
        BaseResultEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            BaseResultEnum avalue = var1[var3];
            if (avalue.getCode().equals(code)) {
                return avalue.getMsg();
            }
        }

        return null;
    }

    public static Map<String, Object> getEnumsMap() {
        Map<String, Object> map = new HashMap();
        BaseResultEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            BaseResultEnum e = var1[var3];
            map.put(e.getCode(), e.getMsg());
        }

        return map;
    }

    public String toString() {
        return this.code + "|" + this.msg;
    }
}
