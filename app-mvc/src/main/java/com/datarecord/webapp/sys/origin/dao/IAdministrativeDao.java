package com.datarecord.webapp.sys.origin.dao;

import com.datarecord.webapp.sys.origin.entity.Administrative;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IAdministrativeDao {

    @Insert("INSERT INTO organizations (\n" +
            "\torganization_name,\n" +
            "\tcreate_time,\n" +
            "\tcreate_user,\n" +
            "\torigin_id\n" +
            ")\n" +
            "VALUES\n" +
            "\t(\n" +
            "\t#{ organization_name },sysdate(),#{ create_user },#{ origin_id }\n" +
            "\t)")
    void addAdministrative(Administrative administrative);

    @Delete("delete from organizations where organization_id = #{organizationId}")
    void deleteById(@Param("organizationId") String organization_id);

    @Update("<script>update organizations <set>"
            +"<if test='origin_id!=null'>"
            +"origin_id=#{origin_id} ,"
            +"</if>"
            +"<if test='organization_name!=null'>"
            +"organization_name=#{organization_name} ,"
            +"</if>"
            +"<if test='create_user!=null'>"
            +"create_user=#{create_user} ,"
            +"</if>"
            +"create_time=sysdate() "
            +"</set>where organization_id = #{organization_id}</script>")
    void updateAdministrative(Administrative administrative);

    @Select("SELECT\n" +
            "\ta.organization_id,\n" +
            "\ta.organization_name,\n" +
            "\ta.origin_id,\n" +
            "\ta.create_time,\n" +
            "\ta.create_user,\n" +
            "\tb.origin_name,\n" +
            "\tc.user_name\n" +
            "FROM\n" +
            "\torganizations a\n" +
            "LEFT JOIN sys_origin b ON a.origin_id = b.origin_id\n" +
            "LEFT JOIN `user` c ON a.create_user=c.user_id")
    Page<Administrative> listAdministrative(@Param("currPage") int currPage, @Param("pageSize") int pageSize);

    @Delete("delete from user_organizations_assign where user_id = #{userId}")
    void removeUserOrganization(@Param("userId") Integer userId);

    @Insert("insert into user_organizations_assign (organization_id,user_id) values (#{organizationId},#{userId})")
    void userOrganizationSave(@Param("organizationId") Integer organizationId, @Param("userId") Integer userId);

    @Select("select distinct o.* from organizations o,user_organizations_assign uoa where o.organization_id = uoa.organization_id " +
            "and uoa.user_id = #{userId}")
    Administrative getOrganizationByUser(Integer userId);

    @Insert("<script>INSERT INTO organization_origin_assign (organization_id,origin_id)\n" +
            "VALUES " +
            "<foreach item=\"item\" index=\"index\" collection=\"originIds\" separator=\",\">" +
            "(#{organizationId},#{item})" +
            "</foreach></script>")
    void saveOrganizationAndOriginAssign(@Param("originIds") String[] originIds, @Param("organizationId") String organizationId);

    @Select("SELECT\n" +
            "\ta.origin_id\n" +
            "FROM\n" +
            "\torganization_origin_assign a\n" +
            "WHERE\n" +
            "\t1 = 1" +
            " and organization_id= #{organizationId}")
    List<String> getOrganizationAndOriginAssignById(@Param("organizationId") String organizationId);

    @Delete("delete from organization_origin_assign where organization_id = #{organizationId}")
    void delOrganizationAndOriginAssign(@Param("organizationId") String organizationId);

    @Select("select so.* from sys_origin so ,organization_origin_assign ooa,user_organizations_assign uoa where\n" +
            "so.origin_id = ooa.origin_id and ooa.organization_id=uoa.organization_id and \n" +
            "uoa.user_id = #{userId}")
    List<Origin> listAllOriginForOrganization(Integer userId);
}
