package com.datarecord.webapp.reportinggroup.bean;

public class RcdJobUnitFlow {
    private Integer job_id;
    private Integer unit_id;
    private Integer edit_after_sub = 1;
    private Integer edit_reviewer = 0;

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public Integer getEdit_after_sub() {
        return edit_after_sub;
    }

    public void setEdit_after_sub(Integer edit_after_sub) {
        this.edit_after_sub = edit_after_sub;
    }

    public Integer getEdit_reviewer() {
        return edit_reviewer;
    }

    public void setEdit_reviewer(Integer edit_reviewer) {
        this.edit_reviewer = edit_reviewer;
    }
}
