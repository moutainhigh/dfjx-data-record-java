package com.datarecord.webapp.process.controller;

import com.datarecord.webapp.process.dao.ReportFileLog;
import com.datarecord.webapp.process.entity.ExportParams;
import com.datarecord.webapp.process.entity.ReportJobData;
import com.datarecord.webapp.process.service.ReportSumService;
import com.datarecord.webapp.utils.HttpServletSupport;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static java.lang.System.in;

@Controller
@RequestMapping("reportSum")
public class ReportSumController {

    @Autowired
    private ReportSumService reportSumService;

    @RequestMapping("recordDataByFlds")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult recordDataByFlds(@RequestBody ExportParams exportParams){
        Map<Integer, List<ReportJobData>> groupDatas = reportSumService.recordDataByFlds(exportParams);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "数据获取完毕", null, groupDatas);
        return successResult;
    }

    @RequestMapping("jobDataByFlds")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult jobDataByFlds(@RequestBody ExportParams exportParams){
        Map<Integer, List<ReportJobData>> groupDatas = reportSumService.recordDataByFlds(exportParams);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "数据获取完毕", null, groupDatas);
        return successResult;
    }

    @RequestMapping("exportGroup")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public void exportGroup(String reportId, String jobId, String groupId, HttpServletResponse response) throws IOException {
        String fullFilePath = reportSumService.exportGroup(reportId,jobId,groupId);
        HttpServletSupport.getInstance().exportFile(fullFilePath,response);
    }

    @RequestMapping("createReportFile")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getJobFlowLogs(@RequestBody ExportParams exportParams){
        reportSumService.exportRecordFldsData(exportParams);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "已经开始生成文件,请稍后下载", null, "已经开始生成文件,请稍后下载");
        return successResult;
    }

    @RequestMapping("createJobFile")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult createJobFile(@RequestBody ExportParams exportParams){
        reportSumService.exportJobFldsData(exportParams);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "已经开始生成文件,请稍后下载", null, "已经开始生成文件,请稍后下载");
        return successResult;
    }

    @RequestMapping("downLoadReportFile")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult downLoadReportFile(String logId, HttpServletResponse response) throws IOException {
//        recordProcessFlowService.exportRecordFldsData(exportParams);
        ReportFileLog reportFileLog = reportSumService.getReportFile(logId);
        if(reportFileLog==null){
            JsonResult successResult = JsonSupport.makeJsonpResult(
                    JsonResult.RESULT.FAILD, "找不到文件", null, "找不到文件");
            return successResult;
        }
        String filePath = reportFileLog.getFile_path();
        HttpServletSupport.getInstance().exportFile(filePath,response);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "无意义", null, "无意义");
        return successResult;
    }

    @RequestMapping("sumJobFiles")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult sumJobFiles(String jobId){
        reportSumService.sumJobFiles(jobId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "无意义", null, "无意义");
        return successResult;
    }

    @RequestMapping("getSumJobFileList")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getSumJobFileList(String jobId){
        List<ReportFileLog> fileList = reportSumService.getSumJobFileList(jobId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, fileList);
        return successResult;
    }

    @RequestMapping("getJobFldFileList")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getJobFldFileList(String jobId){
        List<ReportFileLog> fileList = reportSumService.getSumJobFldFileList(jobId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, fileList);
        return successResult;
    }
}
