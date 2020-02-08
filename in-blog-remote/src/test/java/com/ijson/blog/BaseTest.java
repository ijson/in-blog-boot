package com.ijson.blog;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 8:05 PM
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-rest.xml")
public class BaseTest {
}
