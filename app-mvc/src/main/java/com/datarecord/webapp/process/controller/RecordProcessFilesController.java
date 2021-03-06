package com.datarecord.webapp.process.controller;

import com.datarecord.webapp.process.service.RecordProcessFilesService;
import com.datarecord.webapp.utils.HttpServletSupport;
import com.google.common.base.Strings;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

@Controller
@RequestMapping("record/files")
public class RecordProcessFilesController {

    @Autowired
    private RecordProcessFilesService recordProcessFilesService;

    @RequestMapping("getImportTemplate")
    @CrossOrigin(allowCredentials = "true")
    public void getImportTemplate(String jobId, HttpServletResponse response){
        try {
            String filePath = recordProcessFilesService.getImportTemplate(jobId);
            HttpServletSupport.getInstance().exportFile(filePath,response);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * reportId:报表编号
     * jobId:报表定义编号
     * importType:导入方式 OVERRIDE覆盖旧数据 EXTEND追加数据
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("importRecordData")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult importRecordData(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> allImportFileNames = request.getFileNames();
        String fileName = null;
        while (allImportFileNames.hasNext()){
            fileName = allImportFileNames.next();
        }
        URL classPath = this.getClass().getClassLoader().getResource("/");
        String classPathStr = classPath.toString();

        String reportId = request.getParameter("reportId");
        String jobId = request.getParameter("jobId");
        String importType = request.getParameter("importType");

        MultipartFile multiUploadFile = request.getFile(fileName);
        String oriqinalFileName = multiUploadFile.getOriginalFilename();

        File importFile = new File(classPathStr+"/"+oriqinalFileName);
        importFile.deleteOnExit();
        importFile.mkdirs();
        multiUploadFile.transferTo(importFile);
        try{
            if(Strings.isNullOrEmpty(importType))
                importType = "OVERRIDE";
            if("OVERRIDE".equals(importType)){
                recordProcessFilesService.overrideImport(jobId,reportId,importFile);
            }else if("EXTEND".equals(importType)){
                recordProcessFilesService.extendImport(jobId,reportId,importFile);
            }
        }catch (Exception e){
            e.printStackTrace();
            JsonResult faildResult = JsonSupport.makeJsonpResult(
                    JsonResult.RESULT.FAILD, "导入异常", null, "导入异常");
            return faildResult;
        }


        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "导入完成", null, "导入完成");
        return successResult;
    }

}
