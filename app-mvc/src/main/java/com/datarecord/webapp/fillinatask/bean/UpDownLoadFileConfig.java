package com.datarecord.webapp.fillinatask.bean;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:fileConfig.properties")
public class UpDownLoadFileConfig {

    @Value("${export.filepath}")
    private  String exportFilePath;

    @Value("${upload.filepath}")
    private String uploadFilePath;

    public String getExportFilePath() {
        return exportFilePath;
    }

    public void setExportFilePath(String exportFilePath) {
        this.exportFilePath = exportFilePath;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }
}
