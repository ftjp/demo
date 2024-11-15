package com.example.demo.task.adapter;

import com.alibaba.fastjson.JSON;
import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.infruastructure.enums.BaseResultEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.example.demo.task.domain.entity.RetryTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/task")
@Api(tags = "task")
@Slf4j
public class TaskController {


    @ApiOperation(value = "test接口", notes = "")
    @PostMapping("/test")
    public BaseResult<Void> test(@RequestBody RetryTask param) {
        log.info("test接口:{}", JSON.toJSONString(param));
        BaseResult<Void> result = new BaseResult<>();
        result.setSuccess();
        try {

            System.out.println("*********************************");

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
