package com.example.demo;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(classes = DemoApplication.class)
@RunWith(SpringRunner.class)
@Transactional
public class SpringbootBaseRunner {

}
