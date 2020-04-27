package com.datarecord.webapp.process.dao;

import com.datarecord.webapp.process.entity.RecordImportFldInfo;
import com.datarecord.webapp.process.entity.RecordImportJobInfo;
import com.datarecord.webapp.process.entity.RecordImportUnitInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRecordProcessImportDao {

    @Select("select job_id,template_file_path,template_create_date from rcd_imp_template_log where job_id = #{job_id}")
    RecordImportJobInfo getRecordImportJobInfo(@Param("job_id") String job_id);

    @Delete("delete from rcd_imp_template_log where job_id = #{job_id}")
    void deleteRecordImportJobInfo(@Param("job_id") String job_id);

    @Select("select job_id,unit_id,unit_order from rcd_imp_template_unit_log where job_id = #{job_id} order by unit_order")
    List<RecordImportUnitInfo> getRecordImportUnitInfos (@Param("job_id") String job_id);

    @Select("select job_id,unit_id,fld_id,fld_order from rcd_imp_template_fld_log where unit_id=#{unit_id} order by fld_order")
    List<RecordImportFldInfo> getRecordImportFLdInfos (@Param("job_id") String job_id,@Param("unit_id") String unit_id);

    @Insert("insert into rcd_imp_template_log (job_id,template_file_path,template_create_date) values (#{job_id},#{template_file_path},#{template_create_date})")
    void recordJobTemplateInfo(RecordImportJobInfo recordImportJobInfo);

    @Insert("insert into rcd_imp_template_unit_log " +
            "(job_id,unit_id,unit_order) " +
            "values " +
            "(#{job_id},#{unit_id},#{unit_order}) ")
    void recordUnitTemplateInfo(RecordImportUnitInfo recordImportUnitInfo);

    @Insert("insert into rcd_imp_template_fld_log " +
            "(job_id,unit_id,fld_id,fld_order) " +
            "values " +
            "(#{job_id},#{unit_id},#{fld_id},#{fld_order}) ")
    void recordFldTemplateInfo(RecordImportFldInfo recordImportFldInfo);
}
