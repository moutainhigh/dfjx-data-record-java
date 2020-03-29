package com;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={
        "classpath*:applicationContext.xml"
        ,
        "classpath*:data-source-context.xml",
        "classpath*:sqlMapConfig.xml"
})
public class AbstractTestService {
}