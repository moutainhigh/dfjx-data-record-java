package com.datarecord.webapp.rcduser.controller;

import com.datarecord.webapp.rcduser.bean.Originss;
import com.datarecord.webapp.rcduser.bean.RecordUser;
import com.datarecord.webapp.rcduser.bean.Useroriginassign;
import com.datarecord.webapp.rcduser.service.RecordUserService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.sys.origin.service.RecordOriginService;
import com.datarecord.webapp.sys.origin.tree.EntityTree;
import com.datarecord.webapp.sys.origin.tree.TreeUtil;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 填报人Controller
 */
@Controller
@RequestMapping("/reporting")
public class RecordUserController {


    @Autowired
    private RecordUserService recordUserService;

    @Autowired
    private RecordOriginService recordOriginService;

    @Autowired
    private OriginService originService;

    //组织机构
    @RequestMapping("/getOriginDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String getOriginDatas(
            @RequestParam("orgId") String orgId
    ){
        String topId = orgId;
        List<EntityTree> list =  recordOriginService.listAllRecordOrigin();
        List<EntityTree> tree = TreeUtil.getTreeList(topId, list);
        String jsonpResponse = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取机构成功", null, tree);
        return jsonpResponse;
    }


    //组织机构
    @RequestMapping("/getOriginDatasorgId")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String getOriginDatasorgId(
            @RequestParam("orgId") String orgId
    ){
        //判断用户是否登录然后查出用户所在组织机构id
        /*HashMap<Object, Object> reslltMap = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String orgId = recordUserService.selectOrgId(user.getUser_id());
     */
      //  String orgId = "0";
      //  Map<String, Object> returnmap = new HashMap<>();
      //  MenuTreeUtil menuTree = new MenuTreeUtil();
        List<Originss> list =  recordUserService.listOrgData(orgId);
      //  List<Object> menuList = menuTree.menuList(list,orgId);
     //   returnmap.put("list", menuList);
        String jsonpResponse = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取成功", null, list);
        return jsonpResponse;
    }


    //填报人列表rcd_person_config
    @RequestMapping("/rcdpersonconfiglist")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String rcdpersonconfiglist(
            @RequestParam("currPage") int currPage,
            @RequestParam("pageSize")int pageSize,
            @RequestParam("user_name")String user_name
    ){
        PageResult pageResult = null;
        try{
          /*  User user = WorkbenchShiroUtils.checkUserFromShiroContext();
            Origin userOrigin = originService.getOriginByUser(user.getUser_id());
            pageResult = recordUserService.rcdpersonconfiglist(currPage,pageSize,user_name,userOrigin);*/
            pageResult = recordUserService.rcdpersonconfiglist(currPage,pageSize,user_name);
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取填报人列表失败", null, "error");
        }
        return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取填报人列表成功", null, pageResult);
    }

    //填报人列表rcd_person_config （无分页）
    @RequestMapping("/rcdpersonconfiglistwu")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String rcdpersonconfiglistwu(
    ){
        List<RecordUser> pageResult = null;
        try{
          /*  User user = WorkbenchShiroUtils.checkUserFromShiroContext();
            Origin userOrigin = originService.getOriginByUser(user.getUser_id());
            pageResult  = recordUserService.rcdpersonconfiglistwufenye(userOrigin);*/
            pageResult  = recordUserService.rcdpersonconfiglistwufenye();
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取填报人列表失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取填报人列表成功", null, pageResult);
    }



    //新增弹框列表
    @RequestMapping("/useroriginassignlist")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String useroriginassignlist(
            @RequestParam("origin_id")String origin_id
    ){
      List<Object>  ll = null;
        try{
            ll = recordUserService.useroriginassignlist(origin_id);
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取弹框列表失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取弹框列表成功", null, ll);
    }

    //填报任务填报人维护（获取机构下用户）
    @RequestMapping("/useroriginassignlistsysorigin")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String useroriginassignlistsysorigin(
            @RequestParam("origin_id")String origin_id
    ){
        //List<Useroriginassign>  ll = new ArrayList<Useroriginassign>();
        List<Object>  ll = null;
        try{
            ll = recordUserService.useroriginassignlistsysorigin(origin_id);
        }catch(Exception e){
            e.printStackTrace();
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报任务中填报人维护获取弹框列表失败", null, "error");
        }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报任务中填报人维护获取弹框列表成功", null, ll);
    }



    //新增确定
    @RequestMapping("/insertrcdpersonconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcdpersonconfig(
            @RequestParam("origin_id")String origin_id,
            @RequestParam("userid")String userid
    ){
        try{
            recordUserService.insertrcdpersonconfig(origin_id,userid);   //新增
        }catch(Exception e){
            e.printStackTrace();
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "成功", null, "success");
    }

    //修改确定
    @RequestMapping("/updatercdpersonconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatercdpersonconfig(
            @RequestParam("origin_id")String origin_id,
            @RequestParam("userid")String userid
    ){
        try{
            recordUserService.updaterRdpersonconfig(origin_id,userid);   //xiugai
        }catch(Exception e){
            e.printStackTrace();
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "失败", null, "error");
        }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "成功", null, "success");
    }


    //修改查询机构对应用户idlist
    @RequestMapping("/selectrcdpersonconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectrcdpersonconfig(
            @RequestParam("origin_id")String origin_id
    ){
        List<Useroriginassign>  ll = null;
        try{
            ll = recordUserService.selectrcdpersonconfig(origin_id);
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取弹框列表失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取弹框列表成功", null, ll);
    }


    //删除
    @RequestMapping("/deletercdpersonconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercdpersonconfig(
            @RequestParam("user_id")String user_id
    ){
        try{
            recordUserService.deletercdpersonconfigbyuserid(user_id);   //根据用户id删除
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除成功", null, "success");
    }


    @RequestMapping("unCheckOriginUser")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult unCheckOriginUser(
            String currPage,
            String pageSize,
            String jobId,
            String originId){
        PageResult recordUserPage = recordUserService.unCheckOriginUser(currPage,pageSize,jobId,originId);
        return JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "删除成功", null, recordUserPage);
    }

    @RequestMapping("checkedOriginUser")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult checkedOriginUser(
            String currPage,
            String pageSize,
            String jobId,
            String originId){
        PageResult recordUserPage = recordUserService.checkedOriginUser(currPage,pageSize,jobId,originId);
        return JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "删除成功", null, recordUserPage);
    }
}
