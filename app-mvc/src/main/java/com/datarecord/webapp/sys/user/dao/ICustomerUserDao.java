package com.datarecord.webapp.sys.user.dao;


import com.datarecord.webapp.sys.origin.entity.ChinaAreaCode;
import com.datarecord.webapp.sys.origin.entity.OriginNature;
import com.datarecord.webapp.sys.user.entity.CustomerUser;
import com.datarecord.webapp.sys.user.entity.UserForgetPwdRecord;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface ICustomerUserDao {
    @Select("<script>" +
            " select u.*,so.origin_id,so.origin_name  from user u,user_origin_assign uoa,sys_origin so " +
            " where u.user_id =uoa.user_id and uoa.origin_id=so.origin_id " +
            " <if test='user_type!=null'> and u.user_type = #{user_type} </if>" +
            " <if test='user_type=null'> and (u.user_type='1' or u.user_type='0')  </if>" +
            " <if test='user_name_cn!=null'> and u.user_name_cn like concat('%',#{user_name_cn},'%') </if>" +
            " <if test='seachOrigins!=null'> and so.origin_id in  " +
            "  <foreach item=\"item\" index=\"index\" collection=\"seachOrigins\" open=\"(\" separator=\",\" close=\")\">" +
            "  #{item}" +
            "  </foreach>"+
            " </if>" +
            "</script>" )
    Page<CustomerUser> pageCqnyUser(@Param("currPage") Integer currPage,
                                    @Param("pageSize") Integer pageSize,
                                    @Param("user_name_cn") String user_name_cn,
                                    @Param("user_type") String user_type,
                                    @Param("seachOrigins") List<BigInteger> seachOrigins);

    @Select("select id," +
            "user_id," +
            "validate_code," +
            "sms_validate_code," +
            "validate_code_time," +
            "sms_validate_code_time from user_forget_pwd_record where user_id=#{userId}")
    UserForgetPwdRecord getUserForgetPwdRecord(@Param("userId") BigInteger userId);


    @Update("update user_forget_pwd_record set validate_code=#{valiteCode},validate_code_time=#{date} where user_id = #{user_id}")
    void updateValidateCode(@Param("user_id") BigInteger user_id, @Param("valiteCode") String valiteCode, @Param("date") Date date);

    @Insert("insert into user_forget_pwd_record " +
            "(validate_code,validate_code_time,user_id) values (#{valiteCode},#{date},#{user_id})" )
    void newValidateCode(@Param("user_id") BigInteger user_id, @Param("valiteCode") String valiteCode, @Param("date") Date date);

    @Update("update user_forget_pwd_record set sms_validate_code=#{validateCode},sms_validate_code_time =#{date} where user_id = #{userId}")
    void updateSmsValidateCode(@Param("userId") BigInteger userId, @Param("validateCode") String validateCode, @Param("date") Date date);

    @Select("<script>" +
            "SELECT id," +
            "area_code," +
            "super_area_code," +
            "area_name," +
            "area_level FROM china_area_code where " +
            "<choose>" +
            "<when test='parentId!=null and parentId!=\"\"'> " +
            "super_area_code = #{parentId}" +
            "</when> " +
            "<otherwise>" +
            "area_level = (select min(area_level) from china_area_code)" +
            "</otherwise>" +
            "</choose>" +
            "</script>")
    List<ChinaAreaCode> getAreaCodeList(@Param("parentId") String parentId);

    @Select("select id ,origin_nature_code , origin_nature_name from sys_origin_nature")
    List<OriginNature> getOriginNature();
}
