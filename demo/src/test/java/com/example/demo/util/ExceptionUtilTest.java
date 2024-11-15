package com.example.demo.util;


import com.example.demo.infruastructure.enums.BaseResultEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.example.demo.infruastructure.util.ExceptionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author LJP
 * @create 2024/9/19 16:38
 */
public class ExceptionUtilTest {

    @Test
    void throwBaseCustomExceptionTest() {

        try {
            ExceptionUtil.throwIfTrue(true, "msg");
        } catch (BaseCustomException e) {
            Assertions.assertEquals("99", e.getCode());
            Assertions.assertEquals("msg", e.getMsg());
        } catch (Exception e) {
            Assertions.fail();
        }

        try {
            ExceptionUtil.throwIfTrue(true, "100", "msg");
        } catch (BaseCustomException e) {
            Assertions.assertEquals("100", e.getCode());
            Assertions.assertEquals("msg", e.getMsg());
        } catch (Exception e) {
            Assertions.fail();
        }

        try {
            ExceptionUtil.throwIfTrue(true, "100", "msg", "data");
        } catch (BaseCustomException e) {
            Assertions.assertEquals("100", e.getCode());
            Assertions.assertEquals("msg", e.getMsg());
            Assertions.assertEquals("data", e.getData());
        } catch (Exception e) {
            Assertions.fail();
        }

        try {
            ExceptionUtil.throwIfTrue(true, BaseResultEnum.SYSTEM_ERROR);
        } catch (BaseCustomException e) {
            Assertions.assertEquals(BaseResultEnum.SYSTEM_ERROR.getCode(), e.getCode());
            Assertions.assertEquals(BaseResultEnum.SYSTEM_ERROR.getMsg(), e.getMsg());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void notThrowBaseCustomExceptionTest() {
        try {
            ExceptionUtil.throwIfTrue(false, "msg");
            ExceptionUtil.throwIfTrue(false, "100", "msg");
            ExceptionUtil.throwIfTrue(false, "100", "msg", "data");
            ExceptionUtil.throwIfTrue(false, BaseResultEnum.SYSTEM_ERROR);
        } catch (BaseCustomException e) {
            Assertions.assertEquals("99", e.getCode());
            Assertions.assertEquals("msg", e.getMsg());
        } catch (Exception e) {
            Assertions.fail();
        }
    }


}
