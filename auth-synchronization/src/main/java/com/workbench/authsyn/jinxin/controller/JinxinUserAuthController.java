package com.workbench.authsyn.jinxin.controller;

import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.workbench.auth.menu.entity.Menu;
import com.workbench.auth.user.entity.User;
import com.workbench.authsyn.jinxin.service.JinxinAuthSynService;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("jinxinAuth")
public class JinxinUserAuthController {

    @Autowired
    private JinxinAuthSynService jinxinAuthSynService;

    @RequestMapping("getUserAuths")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getUserAuths() throws IOException, URISyntaxException {
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        List<Menu> authMenu  = jinxinAuthSynService.getUserAuths(user.getUser_id());
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null, authMenu);
        return jsonResult;
    }

}
