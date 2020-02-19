package com.datarecord.webapp.submittedBy.dao;

import com.datarecord.webapp.submittedBy.bean.Origin;
import com.datarecord.webapp.submittedBy.bean.SubmittedBy;
import com.datarecord.webapp.submittedBy.bean.Useroriginassign;
import com.datarecord.webapp.utils.EntityTree;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SubmittedByDao {

 /*   @Select("SELECT origin_id FROM user_origin_assign WHERE user_id = #{user_id}")
    @Options(useCache = false)
    String getOrgId(@Param("user_id") int user_id);//通过用户查询机构id*/



    @Select("SELECT origin_id AS  id,origin_name AS  label,parent_origin_id AS parentId\n" +
            "FROM sys_origin  where origin_status!=3  AND  parent_origin_id = #{orgId} ")
    @Options(useCache = false)
    List<Origin> listOrgData(@Param("orgId") String orgId);


    @Select("<script>SELECT \n" +
            "           a.*, b.user_name_cn,\n" +
            "            c.origin_name\n" +
            "            FROM \n" +
            "            rcd_person_config a\n" +
            "            LEFT JOIN data_record.user b ON a.user_id = b.user_id\n" +
            "            LEFT JOIN sys_origin c ON a.origin_id = c.origin_id \n" +
            "             WHERE  1=1 \n" +
            "<if test = \"user_name != null and user_name != ''\"> AND b.user_name_cn like concat('%',#{user_name},'%') </if> </script>")
    Page<SubmittedBy> rcdpersonconfiglist(@Param("currPage") int currPage, @Param("pageSize") int pageSize, @Param("user_name") String user_name);

    @Select("<script> SELECT\n" +
            "\ta.user_id,\n" +
            "\tb.user_name\n" +
            "FROM\n" +
            "\tuser_origin_assign a\n" +
            "inner JOIN data_record.user b ON a.user_id = b.user_id\n" +
            "WHERE\n" +
            "\t1 = 1\n" +
            "<if test = \"origin_id != null and origin_id != ''\"> AND a.origin_id =#{origin_id} </if> </script>")
    List<Useroriginassign> useroriginassignlist(@Param("origin_id") String origin_id);


    @Insert("insert into rcd_person_config (user_id,origin_id) values(#{user_id},#{origin_id})")
    void insertrcdpersonconfig(@Param("origin_id") String origin_id, @Param("user_id") String user_id);


    @Select("SELECT user_id from rcd_person_config where origin_id = #{origin_id} ")
    List<Useroriginassign> selectrcdpersonconfig(@Param("origin_id") String origin_id);


    @Delete("DELETE FROM rcd_person_config WHERE origin_id = #{origin_id}")
    void deletercdpersonconfig(@Param("origin_id") String origin_id);

    @Delete("DELETE FROM rcd_person_config WHERE user_id = #{user_id}")
    void deletercdpersonconfigbyuserid(@Param("user_id") String user_id);

    @Select("SELECT\n" +
            "\ta.origin_id AS id,\n" +
            "\ta.origin_name AS NAME,\n" +
            "\ta.parent_origin_id AS pId,\n" +
            "\tc.user_id,\n" +
            "\tc.user_name_cn\n" +
            "FROM\n" +
            "\tsys_origin a\n" +
            "INNER JOIN user_origin_assign b ON a.origin_id = b.origin_id\n" +
            "INNER JOIN data_record.user c ON b.user_id = c.user_id  where c.user_name_cn != ''  ")
    @Options(useCache = false)
    List<EntityTree> listOrgDatauser();

    @Select("select \n" +
            " d.origin_id AS id,\n" +
            "\td.origin_name AS NAME,\n" +
            "\td.parent_origin_id AS pId,\n" +
            "  b.user_id,\t\n" +
            "\tb.user_name_cn\n" +
            " from rcd_person_config  a  \n" +
            " INNER join data_record.user  b ON a.user_id  = b.user_id\n" +
            " INNER join user_origin_assign c on c.user_id = b.user_id\n" +
            " INNER JOIN sys_origin d ON c.origin_id = d.origin_id")
    @Options(useCache = false)
    List<EntityTree> useroriginassignlistsysorigin();
}
