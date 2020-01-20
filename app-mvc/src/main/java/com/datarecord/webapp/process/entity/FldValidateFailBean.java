package com.datarecord.webapp.process.entity;

public class FldValidateFailBean {
    private Integer fld_id;
    private String fail_reason;

    public Integer getFld_id() {
        return fld_id;
    }

    public void setFld_id(Integer fld_id) {
        this.fld_id = fld_id;
    }

    public String getFail_reason() {
        return fail_reason;
    }

    public void setFail_reason(String fail_reason) {
        this.fail_reason = fail_reason;
    }
}
