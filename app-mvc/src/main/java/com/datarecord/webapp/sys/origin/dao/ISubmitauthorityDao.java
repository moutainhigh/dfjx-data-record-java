package com.datarecord.webapp.sys.origin.dao;

import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.entity.RecordOrigin;
import com.datarecord.webapp.sys.origin.tree.EntityTree;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ISubmitauthorityDao {

    @Select("SELECT  " +
            " origin_id id,  " +
            " origin_name label,  " +
            " origin_type originType,  " +
            " parent_origin_id parentId  " +
            "  " +
            "FROM  " +
            " sys_origin where origin_status!=3 ")
    List<EntityTree> listAllSubmitauthority();

    @Insert("INSERT INTO sys_origin (  " +
            " origin_name,  " +
            " parent_origin_id,  " +
            " origin_status,  " +
            " origin_type,  " +
            " create_date,  " +
            " create_user  " +
            ")  " +
            "VALUES  " +
            " (  " +
            " #{ origin_name },#{ parent_origin_id },#{ origin_status },#{origin_type},sysdate(),#{ create_user }  " +
            " )")
    void addSubmitauthority(RecordOrigin recordOrigin);

    @Update("<script>update sys_origin <set>" +
            " origin_status = 3" +
            "</set>" +
            "where origin_id in"+
            "<foreach item='item' index='index' collection='listId' open='(' separator=',' close=')'> " +
            " #{item} " +
            "</foreach></script>")
    void deleteByListId(@Param("listId") List listId);

    @Update("<script>delete from sys_origin <set>" +
            "</set>" +
            "where origin_id in"+
            "<foreach item='item' index='index' collection='listId' open='(' separator=',' close=')'> " +
            " #{item} " +
            "</foreach></script>")
    void delOriginById(@Param("originId") List listId);

    @Update("<script>update sys_origin <set>"
            +"<if test='origin_name!=null'>"
            +"origin_name=#{origin_name} ,"
            +"</if>"
            +"<if test='parent_origin_id!=null'>"
            +"parent_origin_id=#{parent_origin_id} ,"
            +"</if>"
            +"<if test='origin_status!=null'>"
            +"origin_status=#{origin_status} ,"
            +"</if>"
            +"<if test='origin_type!=null'>"
            +"origin_type=#{origin_type} ,"
            +"</if>"
            +"<if test='create_user!=null'>"
            +"create_user=#{create_user} ,"
            +"</if>"
            +"create_date=sysdate() "
            +"</set>where origin_id = #{origin_id}</script>")
    void updateSubmitauthority(RecordOrigin recordOrigin);

    @Select("select origin_id,origin_name,parent_origin_id,origin_status,create_date,create_user,origin_type from sys_origin where  origin_status!=3")
    Page<RecordOrigin> listSubmitauthority(@Param("currPage") int currPage, @Param("pageSize") int pageSize);

    @Select("select * from sys_origin where origin_id = #{origin_id} and origin_status!=3")
    @Results(value={
            @Result(property = "origin_id",column = "origin_id"),
            @Result(property = "childrens",column = "origin_id" ,javaType= List.class, many=@Many(select="getSonOrigins"))})
    Map<String,Object> getOriginById(String origin_id);

    @Select("select * from sys_origin where parent_origin_id=#{parent_id} and origin_status!=3")
    @Results(value={
            @Result(property = "origin_id",column = "origin_id"),
            @Result(property = "childrens",column = "origin_id" ,javaType= List.class, many=@Many(select="getSonOrigins"))})
    List<Map<String,Object>> getSonOrigins(Integer parent_id);

    /**
     * 直接从sys_origin 表和user_origin_assign 表获取用户所对应的origin
     */
    @Select("select distinct so.* from sys_origin so ,user_origin_assign uoa where so.origin_id = uoa.origin_id " +
            "and uoa.user_id = #{userId} and origin_status!=3")
    Origin getOriginByUserId(Integer userId);

    /**
     * 从user_organizations_assign 表获取用户对应的行政机构organization_id ,再取得行政机构所关联的origin_id列表
     * @param userId
     * @return
     */
    @Select("SELECT  " +
            " ooa.origin_id  " +
            "FROM  " +
            " organization_origin_assign ooa  " +
            "WHERE  " +
            " ooa.organization_id IN (  " +
            "  SELECT  " +
            "   uos.organization_id  " +
            "  FROM  " +
            "   user_organizations_assign uos  " +
            "  WHERE  " +
            "   uos.user_id = #{userId}  " +
            " )")
    List<String> getOriginIdListByUserId(@Param("userId") Integer userId);

    @Select("")
    Map<String,Object> getReportSubmitInfo(String reportDefinedId);
}
