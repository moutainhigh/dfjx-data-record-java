package com.datarecord.webapp.fillinatask.dao;

import com.datarecord.webapp.fillinatask.bean.Fillinatask;
import com.datarecord.webapp.fillinatask.bean.RcdJobUnitConfig;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface FillinataskDao {

    @Select("<script> select * from  rcd_job_config where 1=1 " +
            "<if test = \"job_name != null and job_name != ''\"> AND job_name like concat('%',#{job_name},'%') </if> "+
            "<if test = \"job_status != null and job_status != ''\"> AND job_status =#{job_status} </if> "+
            "</script> ")
    Page<Fillinatask> rcdjobconfiglist(@Param("currPage") int currPage, @Param("pageSize") int pageSize, @Param("job_name") String job_name, @Param("job_status") String job_status);

    @Insert("INSERT INTO rcd_job_config(job_name,job_start_dt,job_end_dt)  VALUES(#{job_name},#{job_start_dt},#{job_end_dt})")
    void insertrcdjobconfig(@Param("job_name") String job_name, @Param("job_start_dt") String job_start_dt, @Param("job_end_dt") String job_end_dt);

    @Insert("INSERT INTO rcd_job_person_assign(user_id,job_id)  VALUES(#{s},#{job_id})")
    void insertrcdjobpersonassign(@Param("job_id") String job_id, @Param("s") String s);

    @Delete("delete from rcd_job_person_assign where  job_id = #{job_id}")
    void deletercdjobpersonassign(@Param("job_id") String job_id);

    @Update("update rcd_job_config set job_name = #{job_name}, job_start_dt= #{job_start_dt}, job_end_dt =#{job_end_dt} where  job_id = #{job_id}")
    void updatercdjobconfig(@Param("job_id") String job_id, @Param("job_name") String job_name, @Param("job_start_dt") String job_start_dt, @Param("job_end_dt") String job_end_dt);

    @Select("select  job_unit_id,job_unit_name from rcd_job_unit_config where job_id = #{job_id} and job_unit_active = 0")
    List<RcdJobUnitConfig> selectRcdJobUnitConfig(@Param("job_id") String job_id);

    @Select("select  job_unit_id,job_unit_name from rcd_job_unit_config where job_id = #{job_id} and  job_unit_active = 1")
    List<RcdJobUnitConfig> selectRcdJobUnitConfigyi(@Param("job_id") String job_id);

    @Update("update  rcd_job_unit_config set  job_unit_active = 1 where job_unit_id = #{s} and job_id = #{job_id} ")
    void updateRcdJobUnitConfigyi(@Param("s") String s, @Param("job_id") String job_id);

    @Update("update  rcd_job_unit_config set  job_unit_active = 0 where  job_id = #{job_id} ")
    void updateRcdJobUnitConfigsuo(@Param("job_id") String job_id);
}
