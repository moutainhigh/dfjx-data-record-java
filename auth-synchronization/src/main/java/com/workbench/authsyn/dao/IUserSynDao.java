package com.workbench.authsyn.dao;

import com.workbench.authsyn.jinxin.entity.JinxinOrganization;
import com.workbench.authsyn.jinxin.entity.JinxinUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public interface IUserSynDao {

    @Delete("delete from user ")
    void trancatUserTable();

    @Delete("delete from sys_origin ")
    void trancatOriginTable();

    @Delete("delete from user_origin_assign ")
    void trancatUserOrigins();

    @Insert("<script>" +
            "insert into user (user_id," +
            "user_name," +
            "user_type," +
            "user_status," +
            "user_pwd," +
            "user_name_cn," +
            "office_phone," +
            "mobile_phone," +
            "email) " +
            "values (" +
            "#{userId}," +
            "#{username}," +
            "<if test='systemManager==true'>" +
            " '3', " +
            "</if>" +
            "<if test='systemManager==false'>" +
            " '1', " +
            "</if>" +
            "<if test='enabled==true'>" +
            " '0', " +
            "</if>" +
            "<if test='enabled==false'>" +
            " '1', " +
            "</if>" +
            "#{password}," +
            "#{name}," +
            "#{phoneNumber}," +
            "#{phoneNumber}," +
            "#{email}" +
            ")" +
            "</script>")
    void saveUser(JinxinUser jinxinUser);

    @Insert("insert into sys_origin " +
            "(origin_id," +
            "origin_name," +
            "parent_origin_id," +
            "origin_status," +
            "origin_address)" +
            "values " +
            "(" +
            "#{organizationId}," +
            "#{organizationName}," +
            "#{parentId}," +
            "'0'," +
            "#{organizationAddress}" +
            ")")
    void saveOrigins(JinxinOrganization jinxinOrganization);

    @Insert("insert into user_origin_assign (user_id,origin_id) values (#{userId},#{originId})")
    void saveUserOrigin(@Param("userId") BigInteger userId,@Param("originId") BigInteger originId);
}
