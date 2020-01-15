package submittedBy.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import submittedBy.bean.SubmittedBy;
import submittedBy.bean.Useroriginassign;
import utils.EntityTree;

import java.util.List;

public interface SubmittedByDao {

    @Select("SELECT origin_id FROM user_origin_assign WHERE user_id = #{userId}")
    @Options(useCache = false)
    String getOrgId(@Param("user_id") int user_id);//通过用户查询机构id



    @Select("SELECT origin_id AS  VALUE,origin_name AS  title,parent_origin_id AS parentId\n" +
            "FROM sys_origin \n" +
            "WHERE  FIND_IN_SET(parent_origin_id, GET_CHILD_NODE(#{orgId}))\n")
    @Options(useCache = false)
    List<EntityTree> listOrgData(@Param("orgId") String orgId);


    @Select("SELECT\n" +
            "\ta.*, b.user_name,\n" +
            "\tc.origin_name\n" +
            "FROM\n" +
            "\trcd_person_config a\n" +
            "LEFT JOIN USER b ON a.user_id = b.user_id\n" +
            "LEFT JOIN sys_origin c ON a.origin_id = c.origin_id" +
            " WHERE  1=1  " +
            "<if test = \"user_name != null and user_name != ''\"> AND b.user_name like concat('%',#{user_name},'%') </if>")
    Page<SubmittedBy> rcdpersonconfiglist(@Param("currPage")int currPage,@Param("pageSize") int pageSize, @Param("user_name")String user_name);

    @Select("SELECT\n" +
            "\ta.user_id,\n" +
            "\tb.user_name\n" +
            "FROM\n" +
            "\tuser_origin_assign a\n" +
            "LEFT JOIN USER b ON a.user_id = b.user_id\n" +
            "WHERE\n" +
            "\t1 = 1\n" +
            "<if test = \"origin_id != null and origin_id != ''\"> AND a.origin_id =#{origin_id} </if>")
    List<Useroriginassign> useroriginassignlist(@Param("origin_id")String origin_id);


    @Insert("insert into rcd_person_config (user_id,origin_id) values(#{s},#{origin_id})")
    void insertrcdpersonconfig(@Param("origin_id")String origin_id, @Param("s")String s);


    @Select("SELECT user_id from rcd_person_config where origin_id = #{origin_id} ")
    List<Useroriginassign> selectrcdpersonconfig(@Param("origin_id") String origin_id);


    @Delete("DELETE FROM rcd_person_config WHERE origin_id = #{origin_id}")
    void deletercdpersonconfig(@Param("origin_id")String origin_id);

    @Delete("DELETE FROM rcd_person_config WHERE user_id = #{user_id}")
    void deletercdpersonconfigbyuserid(@Param("user_id")String user_id);
}
