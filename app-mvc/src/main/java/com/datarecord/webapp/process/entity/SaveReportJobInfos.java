package com.datarecord.webapp.process.entity;

import java.util.List;

public class SaveReportJobInfos {
        private Integer report_id;
        private Integer job_id;
        private List<ReportJobData> newReportJobInfos;
        private List<ReportJobData> delReportJobInfos;
        private List<ReportJobData> reportJobInfos;

        public Integer getReport_id() {
            return report_id;
        }

        public void setReport_id(Integer report_id) {
            this.report_id = report_id;
        }

        public Integer getJob_id() {
            return job_id;
        }

        public void setJob_id(Integer job_id) {
            this.job_id = job_id;
        }

        public List<ReportJobData> getNewReportJobInfos() {
            return newReportJobInfos;
        }

        public void setNewReportJobInfos(List<ReportJobData> newReportJobInfos) {
            this.newReportJobInfos = newReportJobInfos;
        }

        public List<ReportJobData> getDelReportJobInfos() {
            return delReportJobInfos;
        }

        public void setDelReportJobInfos(List<ReportJobData> delReportJobInfos) {
            this.delReportJobInfos = delReportJobInfos;
        }

        public List<ReportJobData> getReportJobInfos() {
            return reportJobInfos;
        }

        public void setReportJobInfos(List<ReportJobData> reportJobInfos) {
            this.reportJobInfos = reportJobInfos;
        }
    }
