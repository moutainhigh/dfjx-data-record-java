package com.datarecord.webapp.fillinatask.controller;


import com.datarecord.webapp.fillinatask.bean.Jobstatus;
import com.datarecord.webapp.fillinatask.bean.Lieming;
import com.datarecord.webapp.fillinatask.service.FillinataskService;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileOutputStream;
import java.util.List;


@Controller("/simpleExcelWrite")
public class SimpleExcelWrite {

    @Autowired
    private FillinataskService fillinataskService;

    //生成excel
    @RequestMapping("/generateexcel")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public String generateexcel(
            @RequestParam("jobid")int jobid,
            @RequestParam("reportid")int reportid,
            @RequestParam("unitList")List<Jobstatus> unitList
    ) {
        PageResult pageResult = null;
        String jsonResult = "";
        try {
        //第一步创建workbook
        HSSFWorkbook wb = new HSSFWorkbook();
       String jobname =  fillinataskService.selectrcdjobconfig(jobid);           //查询出此任务名称   excel名称
        HSSFSheet sheet = null;    //第二步创建sheet
        for (int aa=0 ; aa<unitList.size();aa++){
           String job_unit_name = fillinataskService.selectrcdjobunitconfig(unitList.get(aa).getUnitId());//查出任务组名称 sheet页名称
            wb.createSheet(job_unit_name);     //创建sheet页
            //第三步创建行row:添加表头0行
            HSSFRow row = sheet.createRow(0);
            HSSFCellStyle style = wb.createCellStyle();
            String fldids = "";       //拼接需要字段id
            for (String fldlie : unitList.get(aa).getFldList()){
                fldids += ",";
                fldids += fldlie;
            }
            List<Lieming>  LS = fillinataskService.selectrcdreportdatajob(jobid,reportid,unitList.get(aa).getUnitId(),fldids);
            HSSFCell cell =null;
              for (int j = 0 ;j<LS.size();j++){
                  cell = row.createCell(j);
                  cell.setCellValue(LS.get(j).getFld_name());      //第四步创建单元格
                  cell.setCellStyle(style);
                  row = sheet.createRow(j + 1);  //创建行
                  row.createCell(j).setCellValue(LS.get(j).getRecord_data());     //创建单元格并且添加数据 // 第五步插入数据
              }
        }
            //第六步将生成excel文件保存到指定路径下
            FileOutputStream fout = new FileOutputStream("D:\\excel\\"+jobname+".xls");
            wb.write(fout);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "生成excel文件失败", null, "error");
        }
        return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "生成excel文件成功", null, "success");
    }
}
