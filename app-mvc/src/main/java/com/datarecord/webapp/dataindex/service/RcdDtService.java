package com.datarecord.webapp.dataindex.service;

import com.datarecord.webapp.dataindex.bean.RcdDtFld;
import com.datarecord.webapp.dataindex.bean.RcddtCatg;
import com.datarecord.webapp.dataindex.bean.Rcddtproj;
import com.webapp.support.page.PageResult;

import java.util.List;

public interface RcdDtService {

    void insertrcddtproj(String proj_name, String is_actived);

    PageResult selectrcddtproj(int currPage, int pageSize);

    void updatercddtproj(String proj_name, String is_actived, String proj_id);

    PageResult selecttixircddtproj(int currPage, int pageSize, String catg_id);

    void insertrcddtfld(String catg_id, String fld_name, String fld_point, String fld_type, String fld_data_type, String fld_is_null);

    void insertrcddtfldctassign(String fld_id, String dict_content_id);

    void updatercddtdict(String dict_content_id);

    void deletercddtfldctassign(String fld_id);

    void updatercddtfld(String fld_id, String catg_id, String fld_name, String fld_point, String fld_type, String fld_data_type, String fld_is_null);



    List<Rcddtproj> leftrcddtprojjblx();

    List<RcddtCatg> leftrcddtcatglx(String proj_id);

    List<RcdDtFld> leftrcddtfld(String catg_id);

    PageResult selecttixircddtprojer(int currPage, int pageSize, String proj_id);
}
