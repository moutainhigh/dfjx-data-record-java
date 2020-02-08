package com.datarecord.webapp.sys.origin.controller;

import com.datarecord.webapp.sys.origin.entity.RecordOrigin;
import com.datarecord.webapp.sys.origin.service.RecordOriginService;
import com.datarecord.webapp.sys.origin.tree.EntityTree;
import com.datarecord.webapp.sys.origin.tree.TreeUtil;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.spring.aop.annotation.JsonpCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 报送机构的新增 修改 删除 查询
 */
@Controller
@RequestMapping("sys/recordOrigin")
public class RecordOriginController {

    @Autowired
    private RecordOriginService recordOriginService;

    /**
     * 列表查询展示
     * @param currPage
     * @param pageSize
     * @return
     */
    @RequestMapping("listRecordOrigin")
    @ResponseBody
    @JsonpCallback
    @CrossOrigin(allowCredentials="true")
    public String listRecordOrigin(int currPage, int pageSize){
        PageResult originList = recordOriginService.listRecordOrigin(currPage, pageSize);
        String jsonpResponse = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取成功", null, originList);
        return jsonpResponse;
    }

    /**
     * 新增机构
     * @param recordOrigin
     * @return
     */
    @RequestMapping("addRecordOrigin")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult addRecordOrigin(@RequestBody RecordOrigin recordOrigin){
        recordOriginService.addRecordOrigin(recordOrigin);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null,null);
        return jsonResult;
    }


    /**
     * 删除机构
     * @param originId
     * @return
     */
    @RequestMapping("delById")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult deleteById(String originId){
        recordOriginService.deleteById(originId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "删除成功", null,null);
        return jsonResult;
    }

    /**
     * 机构的树结构
     * @return
     */
    @RequestMapping("listAllRecordOrigin")
    @ResponseBody
    @JsonpCallback
    @CrossOrigin(allowCredentials="true")
    public String listAllRecordOrigin(){
        //List<EntityTree> originList = recordOriginService.listAllRecordOrigin();
        List<EntityTree> list = recordOriginService.listAllRecordOrigin();//返回的最终集合，可根据自己返回类型更改。
        List<EntityTree> tree = TreeUtil.getTreeList("0", list);//调用工具类，第一个参数是默认传入的顶级id，和查询出来的数据
        //EntityTree result = new EntityTree();//我在已经生成好的树形结构外面有包了一层。视情况而定，可以不用写
        //result.setId("0");
        //result.setName("请选择部门机构");
        String jsonpResponse = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取成功", null, tree);
        return jsonpResponse;
    }

}
