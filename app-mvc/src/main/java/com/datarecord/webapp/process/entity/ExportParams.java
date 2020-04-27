package com.datarecord.webapp.process.entity;

public class ExportParams {

    private String report_id;
    private Boolean needExport;
    private JobConfig jobConfig;

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public JobConfig getJobConfig() {
        return jobConfig;
    }

    public void setJobConfig(JobConfig jobConfig) {
        this.jobConfig = jobConfig;
    }

    public Boolean getNeedExport() {
        return needExport;
    }

    public void setNeedExport(Boolean needExport) {
        this.needExport = needExport;
    }
}
