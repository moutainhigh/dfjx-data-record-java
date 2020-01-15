package reportinggroup.dao;

        import com.github.pagehelper.Page;
        import datadictionary.bean.DataDictionary;
        import org.apache.ibatis.annotations.Delete;
        import org.apache.ibatis.annotations.Insert;
        import org.apache.ibatis.annotations.Param;
        import org.apache.ibatis.annotations.Select;
        import reportinggroup.bean.RcdJobUnitFld;
        import reportinggroup.bean.ReportingGroup;
        import reportinggroup.bean.rcdJobConfig;

        import java.util.List;

public interface ReportingGroupDao {

    @Select("SELECT job_id,job_name FROM rcd_job_config")
    List<rcdJobConfig> leftrcdjobconfig();

    @Select("SELECT job_unit_id,job_unit_name,job_unit_active FROM rcd_job_unit_config where job_id =#{job_id}")
    Page<ReportingGroup> rcdjobunitconfiglist(@Param("currPage") int currPage, @Param("pageSize")int pageSize, @Param("job_id")String job_id);

    @Delete("DELETE FROM rcd_job_unit_config WHERE  job_unit_id =#{job_unit_id}")
    void deletercdjobunitconfig(@Param("job_unit_id") String job_unit_id);

    @Insert("INSERT  INTO rcd_job_unit_fld(job_unit_id,fld_id) VALUES(#{s},#{fld_id})")
    void rcdjobunitfld(@Param("fld_id")String fld_id, @Param("s")String s);

    @Delete("DELETE FROM rcd_job_unit_fld WHERE  fld_id =#{fld_id}")
    void rcdjobunitflddelete(@Param("fld_id")String fld_id);

    @Select("SELECT fld_id FROM rcd_job_unit_fld")
    List<RcdJobUnitFld> selectrcdjobunitfld();
}
