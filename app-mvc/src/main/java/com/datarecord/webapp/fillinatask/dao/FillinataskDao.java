package com.datarecord.webapp.fillinatask.dao;

import com.datarecord.webapp.fillinatask.bean.Fillinatask;
import com.datarecord.webapp.fillinatask.bean.RcdJobPersonAssign;
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

    @Insert("INSERT INTO rcd_job_person_assign(user_id,job_id)  VALUES(#{user_id},#{job_id})")
    void insertrcdjobpersonassign(@Param("job_id") String job_id, @Param("user_id") String user_id);

    @Delete("delete from rcd_job_person_assign where  job_id = #{job_id}")
    void deletercdjobpersonassign(@Param("job_id") String job_id);

    @Update("update rcd_job_config set job_name = #{job_name}, job_start_dt= #{job_start_dt}, job_end_dt =#{job_end_dt} where  job_id = #{job_id}")
    void updatercdjobconfig(@Param("job_id") String job_id, @Param("job_name") String job_name, @Param("job_start_dt") String job_start_dt, @Param("job_end_dt") String job_end_dt);

    @Select("select  job_unit_id,job_unit_name from rcd_job_unit_config where job_id = #{job_id} and job_unit_active = 0")
    List<RcdJobUnitConfig> selectRcdJobUnitConfig(@Param("job_id") String job_id);

    @Select("select  job_unit_id,job_unit_name from rcd_job_unit_config where job_id = #{job_id} and  job_unit_active = 1")
    List<RcdJobUnitConfig> selectRcdJobUnitConfigyi(@Param("job_id") String job_id);

    @Update("update  rcd_job_unit_config set  job_unit_active = 1 where job_unit_id = #{job_unit_id} and job_id = #{job_id} ")
    void updateRcdJobUnitConfigyi(@Param("job_unit_id") String job_unit_id, @Param("job_id") String job_id);

    @Update("update  rcd_job_unit_config set  job_unit_active = 0 where  job_id = #{job_id} ")
    void updateRcdJobUnitConfigsuo(@Param("job_id") String job_id);

    @Select("select u.user_id,u.user_name_cn,so.origin_id from rcd_job_person_assign rjpa left join user u\n" +
            "on rjpa.user_id = u.user_id left join user_origin_assign uoa \n" +
            "on u.user_id = uoa.user_id left join sys_origin so\n" +
            "on uoa.origin_id = so.origin_id  where rjpa.job_id = #{job_id}")
    List<RcdJobPersonAssign> huixianrcdjobpersonassign(@Param("job_id")String job_id);

    @Delete("delete from rcd_job_config where job_id = #{job_id} ")
    void deletercdjobconfig(@Param("job_id")String job_id);

    @Delete("delete from rcd_job_unit_config where job_id = #{job_id} ")
    void deleteRcdJobUnitConfigsuo(@Param("job_id")String job_id);
}
