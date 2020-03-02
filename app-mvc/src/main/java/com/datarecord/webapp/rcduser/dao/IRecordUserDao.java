package com.datarecord.webapp.rcduser.dao;

import com.datarecord.webapp.rcduser.bean.Originss;
import com.datarecord.webapp.rcduser.bean.RecordUser;
import com.datarecord.webapp.rcduser.bean.Useroriginassign;
import com.datarecord.webapp.utils.EntityTree;
import com.github.pagehelper.Page;
import com.workbench.auth.user.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IRecordUserDao {

 /*   @Select("SELECT origin_id FROM user_origin_assign WHERE user_id = #{user_id}")
    @Options(useCache = false)
    String getOrgId(@Param("user_id") int user_id);//通过用户查询机构id*/



    @Select("SELECT origin_id AS  id,origin_name AS  label,parent_origin_id AS parentId " +
            " FROM sys_origin  where origin_status != 3   AND  parent_origin_id = #{orgId} ")
    List<Originss> listOrgData(@Param("orgId") String orgId);


    @Select("<script>SELECT " +
            "           a.*, b.user_name_cn," +
            "            c.origin_name" +
            "            FROM " +
            "            rcd_person_config a" +
            "            inner JOIN user b ON a.user_id = b.user_id" +
            "            inner JOIN sys_origin c ON a.origin_id = c.origin_id " +
            "             WHERE  1=1    " +
            " <if test='originIds!=null'> and c.origin_id  in  " +
            "  <foreach item=\"item\" index=\"index\" collection=\"originIds\" open=\"(\" separator=\",\" close=\")\">" +
            "  #{item}" +
            "  </foreach>"+
            " </if>" +
            " <if test = \"user_name != null and user_name != ''\"> AND b.user_name_cn like concat('%',#{user_name},'%') </if> </script>")
    Page<RecordUser> rcdpersonconfiglist(@Param("currPage") int currPage, @Param("pageSize") int pageSize, @Param("user_name") String user_name, @Param("originIds") List<Integer> originIds    );


    @Select("<script>SELECT " +
            "           a.*, b.user_name_cn," +
            "            c.origin_name" +
            "            FROM " +
            "            rcd_person_config a" +
            "            inner JOIN user b ON a.user_id = b.user_id" +
            "            inner JOIN sys_origin c ON a.origin_id = c.origin_id " +
            "             WHERE  1=1   and b.user_name_cn != ''   " +
            " <if test = \"user_name != null and user_name != ''\"> AND b.user_name_cn like concat('%',#{user_name},'%') </if> </script>")
    Page<RecordUser> rcdpersonconfiglistByid(@Param("currPage") int currPage, @Param("pageSize") int pageSize, @Param("user_name") String user_name);





    @Select("<script> SELECT" +
            " a.user_id," +
            " b.user_name " +
            " FROM" +
            " user_origin_assign a " +
            " inner JOIN user b ON a.user_id = b.user_id " +
            " WHERE " +
            " 1 = 1 " +
            " <if test = \"origin_id != null and origin_id != ''\"> AND a.origin_id =#{origin_id} </if> </script>")
    List<Useroriginassign> useroriginassignlist(@Param("origin_id") String origin_id);


    @Insert("insert into rcd_person_config (user_id,origin_id) values(#{user_id},#{orgid})")
    void insertrcdpersonconfig(@Param("orgid") String orgid, @Param("user_id") String user_id);


    @Select("SELECT user_id from rcd_person_config where origin_id = #{origin_id} ")
    List<Useroriginassign> selectrcdpersonconfig(@Param("origin_id") String origin_id);


    @Delete("DELETE FROM rcd_person_config WHERE origin_id = #{origin_id}")
    void deletercdpersonconfig(@Param("origin_id") String origin_id);

    @Delete("DELETE FROM rcd_person_config WHERE user_id = #{user_id}")
    void deletercdpersonconfigbyuserid(@Param("user_id") String user_id);

    @Select("<script>SELECT" +
            " a.origin_id AS id," +
            " a.origin_name AS name," +
            " a.parent_origin_id AS pId," +
            " c.user_id," +
            " c.user_name_cn" +
            " FROM" +
            " sys_origin a " +
            " INNER JOIN user_origin_assign b ON a.origin_id = b.origin_id " +
            " INNER JOIN user c ON b.user_id = c.user_id    where  c.user_name_cn   != ''   and   a.origin_id  IN  " +
            "  <foreach item=\"item\" index=\"index\" collection=\"originIds\" open=\"(\" separator=\",\" close=\")\">" +
            "  #{item}" +
            "  </foreach>"+
            "  </script>")
    List<EntityTree> listOrgDatauser(@Param("originIds")List<Integer> originIds);

    @Select("<script>SELECT" +
            " a.origin_id AS id," +
            " a.origin_name AS name," +
            " a.parent_origin_id AS pId," +
            " c.user_id," +
            " c.user_name_cn" +
            " FROM" +
            " sys_origin a " +
            " INNER JOIN user_origin_assign b ON a.origin_id = b.origin_id " +
            " INNER JOIN user c ON b.user_id = c.user_id    where  c.user_name_cn   != ''   and   a.origin_id  = #{origin_id}  " +
            "  </script>")
    List<EntityTree> listOrgDatauserByid(@Param("origin_id")String origin_id);

    @Select("<script>select  " +
            " d.origin_id AS id, " +
            " d.origin_name AS name, " +
            " d.parent_origin_id AS pId, " +
            "  b.user_id,  " +
            " b.user_name_cn " +
            " from rcd_person_config  a   " +
            " INNER join user  b ON a.user_id  = b.user_id " +
            " INNER join user_origin_assign c on c.user_id = b.user_id " +
            " INNER JOIN sys_origin d ON c.origin_id = d.origin_id   where 1= 1    and    d.origin_id  IN " +
            "  <foreach item=\"item\" index=\"index\" collection=\"originIds\" open=\"(\" separator=\",\" close=\")\">" +
            "  #{item}" +
            "  </foreach>"+
            "</script>  ")
    List<EntityTree> useroriginassignlistsysorigin(@Param("originIds")List<Integer> originIds);


    @Select("select origin_id from user_origin_assign where user_id =#{user_id}")
    List<String> selectuserid(@Param("user_id") String user_id);


    @Select("<script>SELECT  " +
            "           a.*, b.user_name_cn, " +
            "            c.origin_name " +
            "            FROM  " +
            "            rcd_person_config a " +
            "            LEFT JOIN user b ON a.user_id = b.user_id " +
            "            LEFT JOIN sys_origin c ON a.origin_id = c.origin_id " +
            " <if test='originIds!=null'> and c.origin_id  in  " +
            "  <foreach item=\"item\" index=\"index\" collection=\"originIds\" open=\"(\" separator=\",\" close=\")\">" +
            "  #{item}" +
            "  </foreach>"+
            " </if>" +
            " </script>" )
    List<RecordUser> rcdpersonconfiglistwufenye(@Param("originIds") List<Integer> originIds);


    @Select("<script>SELECT  " +
            "           a.*, b.user_name_cn, " +
            "            c.origin_name " +
            "            FROM  " +
            "            rcd_person_config a " +
            "            LEFT JOIN user b ON a.user_id = b.user_id " +
            "            LEFT JOIN sys_origin c ON a.origin_id = c.origin_id " +
            "  where 1=1  and b.user_name_cn != ''  " +
            " </script>" )
    List<RecordUser> rcdpersonconfiglistwufeByid();

    @Select("SELECT  " +
            "u.user_id, " +
            "u.user_name_cn " +
            "FROM rcd_person_config rpc  " +
            "left join user u on  " +
            "rpc.user_id = u.user_id " +
            "left join (select user_id,job_id from rcd_job_person_assign where job_id=#{jobId}) rjpa on " +
            "rpc.user_id = rjpa.user_id " +
            "where rpc.origin_id=#{originId} and rjpa.job_id is null" )
    Page<User> getOriginRecordUser(
            @Param("currPage") String currPage,
            @Param("pageSize") String pageSize,
            @Param("jobId") String jobId,
            @Param("originId") String originId);

    @Select("select u.user_id, u.user_name_cn,so.origin_name FROM rcd_job_person_assign rjpa " +
            "left join user u on " +
            "rjpa.user_id = u.user_id " +
            "left join user_origin_assign uoa on " +
            "u.user_id = uoa.user_id " +
            "left join sys_origin so on " +
            "uoa.origin_id = so.origin_id " +
            "where rjpa.job_id = #{jobId} ")
    Page<User> checkedOriginUser( @Param("currPage") String currPage,
                                  @Param("pageSize") String pageSize,
                                  @Param("jobId") String jobId,
                                  @Param("originId") String originId);
}
