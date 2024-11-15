package com.example.demo.infruastructure.util;


/**
 * description:
 *
 * @author: LJP
 * @date: 2024/10/18 15:45
 */
public class ExceptionUtil {

    /**
     * description: 如果条件成立，则抛出异常
     *
     * @param throwAble 判断条件结果
     * @param message   抛出业务异常信息
     * @author: LJP
     * @date: 2024/10/21 9:30
     */
    public static void throwIfTrue(boolean throwAble, String message) {
        if (throwAble) {
            throw new RuntimeException(message);
        }
    }
//
//    public static void throwIfTrue(boolean throwAble, String code, String msg) {
//        if (throwAble) {
//            throw new BaseCustomException(code, msg);
//        }
//    }
//
//    public static void throwIfTrue(boolean throwAble, String code, String msg, String data) {
//        if (throwAble) {
//            throw new BaseCustomException(code, msg, data);
//        }
//    }
//
//    public static void throwIfTrue(boolean throwAble, BaseResultEnum baseResultEnum) {
//        if (throwAble) {
//            throw new BaseCustomException(baseResultEnum);
//        }
//    }


}
