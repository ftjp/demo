package com.example.demo.controller.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@Slf4j
public class TestPageViewController {

    // ******************* static 静态资源(可以直接使用路径访问) **********************
    @GetMapping(name = "static-test页面", path = "/view/demo/test/test_view")
    public ModelAndView testViewExample() {
        return new ModelAndView("/test.html");
    }

    @GetMapping(name = "static-test页面", path = "/view/demo/test/test_view1")
    public String testViewExample1() {
        return "/test.html";
    }
    // ******************* static 静态资源 **********************


    // ******************* thymeleaf （只能使用controller调用） **********************
    @GetMapping(name = "thymeleafTest页面", path = "/view/demo/test/thymeleafTest")
    public String thymeleafTest(Model model) {
        model.addAttribute("message", "Hello, this is a dynamic message!");
        return "thymeleafTest";
    }

    @GetMapping(name = "test页面", path = "/view/demo/test/not_found_view1")
    public String notFoundView1() {
        return "/pages/test";
    }

    @GetMapping(name = "404页面", path = "/view/demo/test/not_found_view2")
    public String notFoundView2() {
        return "/pages/404";
    }
    // ******************* thymeleaf **********************


    @GetMapping(name = "重定向", path = "/view/demo/test/redirect/not_found_view1")
    public String redirectExample1() {
        return "redirect:/view/demo/test/not_found_view1";
    }

    @GetMapping(name = "转发", path = "/view/demo/test/forward/not_found_view2")
    public String forwardExample1() {
        return "forward:/view/demo/test/not_found_view2";
    }


}
