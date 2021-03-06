package com.datarecord.webapp.fillinatask.controller;


import com.datarecord.webapp.fillinatask.bean.UpDownLoadFileConfig;
import com.datarecord.webapp.fillinatask.bean.Lieming;
import com.datarecord.webapp.fillinatask.service.JobConfigService;
import com.google.gson.internal.LinkedTreeMap;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/simpleExcelWrite")
public class SimpleExcelWrite {

    @Autowired
    private JobConfigService jobConfigService;

    @Autowired
    private UpDownLoadFileConfig upDownLoadFileConfig;

    //生成excel
    @RequestMapping("/generateExcel")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public String generateExcel(
            @RequestBody Map<String, Object> request
    ) {
        try {
            String jobids = String.valueOf(request.get("jobid").toString());
            Integer jobid = Integer.valueOf(jobids).intValue();
            String reportids = request.get("reportid").toString();
            Integer reportid = Integer.valueOf(reportids).intValue();

            List<Object>  unitList = (List<Object>) request.get("unitList");


            // List<Jobstatus> unitList = (List<Jobstatus>) request.get("unitList");


            //第一步创建workbook
            HSSFWorkbook wb = new HSSFWorkbook();
            String jobname = jobConfigService.selectrcdjobconfig(jobid);           //查询出此任务名称   excel名称
            //第二步创建sheet
            for (int aa = 0; aa < unitList.size(); aa++) {
                LinkedTreeMap<String, Object> DS = (LinkedTreeMap<String, Object>) unitList.get(aa);
                //   String UNI = unitList.
                String job_unit_name = jobConfigService.selectrcdjobunitconfig(DS.get("unitId").toString());//查出任务组名称 sheet页名称
                HSSFSheet sheet = wb.createSheet(job_unit_name);     //创建sheet页
                //第三步创建行row:添加表头0行
                HSSFRow row = sheet.createRow(0);
                HSSFCellStyle style = wb.createCellStyle();
                List fldids = new ArrayList();       //需要字段id
                List<String> lsls = (List<String>) DS.get("fldList");
                for (Object fldlie : lsls) {
                    fldids.add(fldlie);
                }
                List<Lieming> LS = jobConfigService.selectrcdreportdatajob(jobid, reportid, DS.get("unitId").toString(), fldids);
                HSSFCell cell = null;
                for (int j = 0; j < LS.size(); j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(LS.get(j).getFld_name());      //第四步创建单元格
                    cell.setCellStyle(style);
                    row = sheet.createRow(j + 1);  //创建行
                    row.createCell(j).setCellValue(LS.get(j).getRecord_data());     //创建单元格并且添加数据 // 第五步插入数据
                }
            }
            //利用反射加载properties文件
        /*   String url = ExcelP.class.getResource("/excelpath.properties").toURI().getExportFilePath();
            InputStream inStream;
            inStream  = new FileInputStream(url);
            Properties prop = new Properties();
            prop.load(inStream);
            url = prop.getProperty("url");*/
            //第六步将生成excel文件保存到指定路径下

            FileOutputStream fout = new FileOutputStream("" + upDownLoadFileConfig.getExportFilePath() + "" + jobname + ".xls");
            wb.write(fout);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "生成excel文件失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "生成excel文件成功", null, "success");
    }

}
