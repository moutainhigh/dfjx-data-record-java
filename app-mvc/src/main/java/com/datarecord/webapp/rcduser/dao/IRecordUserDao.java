package com.datarecord.webapp.rcduser.dao;

import com.datarecord.webapp.rcduser.bean.Originss;
import com.datarecord.webapp.rcduser.bean.RecordUser;
import com.datarecord.webapp.rcduser.bean.RecordUserGroup;
import com.datarecord.webapp.rcduser.bean.Useroriginassign;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.utils.EntityTree;
import com.github.pagehelper.Page;
import com.workbench.auth.user.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
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


    @Insert("insert into rcd_person_config (user_id,origin_id,group_id) values(#{user_id},#{origin_id},#{group_id})")
    void addRecordUser(RecordUser recordUser);

    @Insert("delete from rcd_person_config where user_id = #{user_id} and (group_id=#{group_id} or origin_id=#{origin_id})")
    void delRecordUser(RecordUser recordUser);

    @Select("<script>select count(1) from rcd_person_config  where 1=1  and  user_id  IN " +
            "  <foreach item='item' index='index' collection='users' open='(' separator=',' close=')'>" +
            "  #{item}" +
            "  </foreach>"+
            "</script>")
    int countRcdPersonConfig(@Param("users") List<String> users);


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
    List<EntityTree> useroriginassignlistsysorigin(@Param("originIds")List<BigInteger> originIds);


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
            "where rjpa.job_id is null and rpc.origin_id=#{originId} and u.user_id is not null " )
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


    @Select("select group_id,group_name,group_active from rcd_person_group")
    Page<RecordUserGroup> pageRecordUserGroup(String currPage, String pageNum);

    @Insert("insert into rcd_person_group (group_name,group_active) values (#{groupName},#{groupActive})")
    void saveUserGroup(@Param("groupName") String groupName,@Param("groupActive") String groupActive);

    @Update("update rcd_person_group set group_name = #{group_name},group_active=#{group_active} where group_id = #{group_id}")
    void updateUserGroup(RecordUserGroup recordUserGroup);

    @Update("<script>" +
            "update rcd_person_group set group_active=0 " +
            "<if test='groupId!=null'>" +
            " where group_id != #{groupId}" +
            "</if>" +
            "</script>")
    void disableGroups(@Param("groupId") String groupId);

    @Update("update rcd_person_group set group_active=1 where group_id = #{groupId}")
    void enableGroups(String groupId);

    @Select("<script>" +
            "SELECT u.user_id,u.user_name,so.origin_id,so.origin_name,rpcg.group_id,rpcg.group_name FROM user u " +
            "left join user_origin_assign uoa on u.user_id = uoa.user_id " +
            "left join sys_origin so on uoa.origin_id = so.origin_id " +
            "left join " +
            "(select rpc.user_id,rpc.group_id,rpg.group_name,rpg.group_active from rcd_person_config rpc,rcd_person_group rpg " +
            " where rpc.group_id = rpg.group_id and rpc.group_id = #{groupId}) rpcg on u.user_id = rpcg.user_id " +
            "where so.origin_id in " +
            "<foreach item='item' index='index' collection='originIds' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach> " +
            " order by so.origin_id" +
            "</script>")
    List<RecordUser> groupOriginUsers(@Param("groupId") String groupId,@Param("originIds") List<String> originIds);

    @Delete("delete from rcd_person_config where group_id = #{groupId}")
    void delGroupPerson(String groupId);

    @Delete("delete from rcd_person_group where group_id = #{groupId}")
    void delUserGroup(String groupId);

    @Select("SELECT " +
            "so.origin_id," +
            "so.origin_name, " +
            "so.parent_origin_id, " +
            "so.origin_status, " +
            "so.create_date, " +
            "so.create_user, " +
            "so.origin_type FROM rcd_person_group rpg left join rcd_person_config rpc " +
            "on rpg.group_id = rpc.group_id left join user_origin_assign uoa " +
            "on rpc.user_id = uoa.user_id left join sys_origin so " +
            "on uoa.origin_id = so.origin_id " +
            "where rpg.group_id = #{groupId} and so.origin_id is not null " )
    List<Origin> groupOrigins(String groupId);

    @Select("select group_id,group_name,group_active from rcd_person_group where group_active ='1' ")
    RecordUserGroup getActiveUserGroup();

    @Select("SELECT " +
            "so.origin_id," +
            "so.origin_name," +
            "u.user_id," +
            "u.user_name," +
            "u.user_name_cn," +
            "u.user_type," +
            "u.reg_date," +
            "u.user_status," +
            "u.office_phone," +
            "u.mobile_phone," +
            "u.email," +
            "u.social_code," +
            "u.last_login_time from " +
            "rcd_person_config rpc left join user u on " +
            "rpc.user_id = u.user_id left join user_origin_assign uoa " +
            "u.user_id =uoa.user_id  left join sys_origin so on " +
            "uoa.origin_id = so.origin_id " +
            "where rpc.group_id = #{groupId}")
    List<User> groupUsers(String groupId);


    @Select("select " +
            "so.origin_id," +
            "so.origin_name, " +
            "so.parent_origin_id, " +
            "so.origin_status, " +
            "so.create_date, " +
            "so.create_user, " +
            "so.origin_type " +
            "from rcd_job_config rjc left join rcd_job_person_assign rjpa on " +
            "rjc.job_id = rjpa.job_id left join user_origin_assign uoa on " +
            "rjpa.user_id = uoa.user_id left join sys_origin so on " +
            "uoa.origin_id = so.origin_id " +
            "where rjc.job_id=#{jobId} and so.origin_id is not null")
    List<Origin> getJobOriginHis(String jobId);

    @Select("SELECT " +
            "rjpgl.group_id," +
            "rjpgl.group_name," +
            "rjpgl.job_make_date " +
            "FROM rcd_job_config rjc left join rcd_job_person_group_log rjpgl on " +
            "rjc.job_id = rjpgl.job_id " +
            "where rrj.report_id = #{reportId}")
    RecordUserGroup jobUserGroupHis(String jobId);
}
