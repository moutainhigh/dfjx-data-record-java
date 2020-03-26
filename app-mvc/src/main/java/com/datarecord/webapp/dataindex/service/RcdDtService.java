package com.datarecord.webapp.dataindex.service;

import com.datarecord.webapp.dataindex.bean.RcdDtFld;
import com.datarecord.webapp.dataindex.bean.RcdDtFldCtAssign;
import com.datarecord.webapp.dataindex.bean.RcddtCatg;
import com.datarecord.webapp.dataindex.bean.Rcddtproj;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.webapp.support.page.PageResult;

import java.util.List;

public interface RcdDtService {

    void insertrcddtproj(String proj_name, String is_actived,String user_id,String originid);

    PageResult selectrcddtproj(int currPage, int pageSize,Origin userOrigin,String user_id);

    void updatercddtproj(String proj_name, String is_actived, String proj_id);

    PageResult selecttixircddtproj(int currPage, int pageSize, String catg_id,String user_id );

    void insertrcddtfld(ReportFldConfig reportFldConfig);

    void insertrcddtfldctassign(String fld_id, String dict_contentid);

    void updatercddtdict(String dict_content_id);

    void deletercddtfldctassign(String fld_id);

    void updatercddtfld(ReportFldConfig reportFldConfig);



    List<Rcddtproj> leftrcddtprojjblx(String  user_id);

    List<RcddtCatg> leftrcddtcatglx(String proj_id,String user_id);

    List<RcdDtFld> leftrcddtfld(String catg_id,String user_id);

    PageResult selecttixircddtprojer(int currPage, int pageSize, String proj_id,Origin userOrigin,String user_id);

    int selectmax();

    void inserttixircddtprojer(String catg_name, String proj_id,String user_id,String originid);

    void updatetixircddtprojer(String catg_id, String catg_name, String proj_id);

    List<RcdDtFldCtAssign> updatehuixianrcddtfldctassign(String fld_id);

    void deletercddtproj(String proj_id);

    void deletrcddtcatg(String catg_id);

    void deletercddtfld(String fld_id);

    int selectcount(String fld_id);

    List<String> selectrcddtcatgproj(String proj_id);

    List<String> selectrcddtcatg(String catg_id);


    void deleteererrcddtfldI(String catg_id);
}
