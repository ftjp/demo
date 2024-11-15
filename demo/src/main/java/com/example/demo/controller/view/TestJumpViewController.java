package com.example.demo.controller.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@Slf4j
public class TestJumpViewController {


    // ***************** 重定向 *****************
    @GetMapping(name = "重定向", path = "/view/demo/test/redirect")
    public ModelAndView redirectExample() {
        return new ModelAndView("redirect:https://www.baidu.com/");
    }

    @GetMapping(name = "重定向", path = "/view/demo/test/redirect1")
    public String redirectExample1() {
        return "redirect:https://www.baidu.com/";
    }

    @GetMapping(name = "重定向", path = "/view/demo/test/redirect2")
    public String redirectExample3() {
        return "redirect:/demo/test/test";
    }

    // ***************** 转发（只能转发本地url） *****************
    @GetMapping(name = "转发", path = "/view/demo/test/forward")
    public ModelAndView forwardExample() {
        return new ModelAndView("forward:/view/demo/test/redirect");
    }

    @GetMapping(name = "转发", path = "/view/demo/test/forward1")
    public String forwardExample2() {
        return "forward:/view/demo/test/redirect";
    }

}
