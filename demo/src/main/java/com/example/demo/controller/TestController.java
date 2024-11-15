package com.example.demo.controller;

import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.infruastructure.enums.BaseResultEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/demo/test")
@Api(tags = "test")
@Slf4j
public class TestController {


    @ApiOperation(value = "test接口", notes = "")
    @GetMapping("/test")
    public BaseResult<String> test() {
        log.info("test接口");
        BaseResult<String> result = new BaseResult<>();
        result.setSuccess();
        try {

            System.out.println("*********************************");

            result.setData("test");

        } catch (BaseCustomException e) {
            log.error("test接口: ", e);
            result.setCodeAndMsg(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.error("test接口: ", e);
            result.setCodeAndMsg(BaseResultEnum.SYSTEM_ERROR);
        }
        return result;
    }


}
