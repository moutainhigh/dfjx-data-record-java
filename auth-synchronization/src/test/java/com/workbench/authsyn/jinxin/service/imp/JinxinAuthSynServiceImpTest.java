package com.workbench.authsyn.jinxin.service.imp;


import com.AbstractTestService;
import com.workbench.authsyn.jinxin.service.JinxinAuthSynService;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;

public class JinxinAuthSynServiceImpTest extends AbstractTestService {

    @Resource
    private JinxinAuthSynService jinxinAuthSynService;

    @Test
    public void getUsersListFromJinxinUserCenter() {
        try {
            jinxinAuthSynService.getUsersListFromJinxinUserCenter();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserAuths() throws IOException, URISyntaxException {
        jinxinAuthSynService.getUserAuths(new BigInteger("1"));
    }
}