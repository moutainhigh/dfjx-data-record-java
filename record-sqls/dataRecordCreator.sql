-- MySQL dump 10.13  Distrib 8.0.20, for macos10.15 (x86_64)
--
-- Host: localhost    Database: data_record_initt
-- ------------------------------------------------------
-- Server version	5.7.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `app_module`
--

DROP TABLE IF EXISTS `app_module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_module` (
  `module_id` int(11) NOT NULL,
  `super_module_id` int(11) DEFAULT NULL,
  `module_name` varchar(40) DEFAULT NULL,
  `module_url` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`module_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job_unit_record_interval`
--

DROP TABLE IF EXISTS `job_unit_record_interval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_unit_record_interval` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_unit_id` int(11) NOT NULL,
  `job_unit_start` varchar(45) NOT NULL COMMENT '填报区间起始日期/时间/周几',
  `job_unit_end` varchar(45) NOT NULL COMMENT '填报区间截止日期/时间/周几',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_dt_catg`
--

DROP TABLE IF EXISTS `rcd_dt_catg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_dt_catg` (
  `catg_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类型编码',
  `catg_name` varchar(45) NOT NULL COMMENT '类型名称',
  `proj_id` int(11) NOT NULL COMMENT '基本类型编码',
  `is_actived` int(11) DEFAULT '0' COMMENT '是否有效\n0:有效\n1:无效',
  `catg_creater` bigint(20) DEFAULT NULL,
  `catg_creater_origin` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`catg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_dt_dict`
--

DROP TABLE IF EXISTS `rcd_dt_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_dt_dict` (
  `dict_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据字典编码',
  `dict_name` varchar(40) NOT NULL COMMENT '数据字典名称',
  `dict_creater` bigint(20) DEFAULT NULL,
  `dict_creater_origin` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_dt_dict_content`
--

DROP TABLE IF EXISTS `rcd_dt_dict_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_dt_dict_content` (
  `dict_content_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据字典内容编码',
  `dict_id` int(11) NOT NULL COMMENT '数据字典编码',
  `dict_content_name` varchar(40) NOT NULL COMMENT '数据字典内容名称',
  `dict_content_value` varchar(40) NOT NULL COMMENT '数据字典内容值',
  PRIMARY KEY (`dict_content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_dt_fld`
--

DROP TABLE IF EXISTS `rcd_dt_fld`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_dt_fld` (
  `catg_id` int(11) NOT NULL COMMENT '关联 rcd_dt_catg.catg_id',
  `fld_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '指标id',
  `fld_name` varchar(45) NOT NULL COMMENT '指标名称',
  `fld_point` varchar(45) DEFAULT NULL COMMENT '指标单位',
  `fld_data_type` varchar(45) NOT NULL COMMENT '指标数据类型\n0:字符串\r\n1:数字\r\n2:日期\r\n3:数据字典\n4.坐标\n5.图片\n6.附件',
  `fld_type` int(11) NOT NULL COMMENT '指标类型\n0:通用指标\r\n1:突发指标',
  `fld_is_null` int(11) NOT NULL DEFAULT '0' COMMENT '0:可以\r\n1:不可以',
  `is_actived` int(11) NOT NULL DEFAULT '0' COMMENT '0:不启用\n1:启用',
  `fld_range` int(11) NOT NULL DEFAULT '0' COMMENT '取值范围：0-所有、1-移动端、2-PC端',
  `fld_visible` int(11) NOT NULL DEFAULT '0' COMMENT '可见范围：0-全部、1-移动端可见、2-PC端可见',
  `fld_status` varchar(45) DEFAULT '0' COMMENT '指标状态：\n0：待审批\n1：审批通过\n2：审批驳回\n3：作废',
  `fld_creater` bigint(20) NOT NULL,
  `fld_creater_origin` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`fld_id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_dt_fld_ct_assign`
--

DROP TABLE IF EXISTS `rcd_dt_fld_ct_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_dt_fld_ct_assign` (
  `dict_content_id` int(11) NOT NULL,
  `fld_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_dt_proj`
--

DROP TABLE IF EXISTS `rcd_dt_proj`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_dt_proj` (
  `proj_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类型编码',
  `proj_name` varchar(40) DEFAULT NULL COMMENT '类型名称',
  `is_actived` int(11) NOT NULL DEFAULT '0',
  `proj_creater` bigint(20) DEFAULT NULL,
  `proj_creater_origin` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`proj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_imp_template_fld_log`
--

DROP TABLE IF EXISTS `rcd_imp_template_fld_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_imp_template_fld_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `unit_id` int(11) NOT NULL,
  `fld_id` int(11) NOT NULL,
  `fld_order` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_imp_template_log`
--

DROP TABLE IF EXISTS `rcd_imp_template_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_imp_template_log` (
  `job_id` int(11) NOT NULL,
  `template_file_path` varchar(500) NOT NULL,
  `template_create_date` date NOT NULL,
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_imp_template_unit_log`
--

DROP TABLE IF EXISTS `rcd_imp_template_unit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_imp_template_unit_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `unit_id` int(11) NOT NULL,
  `unit_order` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_job_config`
--

DROP TABLE IF EXISTS `rcd_job_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_job_config` (
  `job_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(40) NOT NULL,
  `job_status` int(11) NOT NULL DEFAULT '0' COMMENT ' * 0:正常\n * 1:失效\n * 2:锁定\n * 3:软删除\n * 4:已发布\n * 5:发布中\n * 6:待审批\n * 7:审批通过\n * 8:审批驳回',
  `job_start_dt` date DEFAULT NULL,
  `job_end_dt` date DEFAULT NULL,
  `job_creater` bigint(20) NOT NULL COMMENT '任务创建人',
  `job_creater_origin` bigint(20) NOT NULL,
  `job_cycle` int(11) NOT NULL COMMENT '填报周期:\n0.日报\n1.周报\n2.月报\n3.季报\n4.年报',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_job_flowlog`
--

DROP TABLE IF EXISTS `rcd_job_flowlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_job_flowlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) DEFAULT NULL,
  `job_flow_status` int(11) DEFAULT NULL COMMENT '审批状态:通过还是驳回还是其他,值对应\nrcd_job_config.job_status',
  `job_flow_comment` varchar(400) DEFAULT NULL COMMENT '审批处理备注',
  `job_flow_user` bigint(20) DEFAULT NULL COMMENT '审批人',
  `job_flow_date` timestamp NULL DEFAULT NULL COMMENT '审批时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_job_interval`
--

DROP TABLE IF EXISTS `rcd_job_interval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_job_interval` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `job_interval_start` varchar(45) NOT NULL,
  `job_interval_end` varchar(45) NOT NULL COMMENT '任务填报周期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_job_person_assign`
--

DROP TABLE IF EXISTS `rcd_job_person_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_job_person_assign` (
  `user_id` bigint(20) NOT NULL,
  `job_id` int(11) NOT NULL,
  `assign_status` int(11) NOT NULL DEFAULT '0' COMMENT '0:有效\n1:无效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_job_person_group_log`
--

DROP TABLE IF EXISTS `rcd_job_person_group_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_job_person_group_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `group_name` varchar(200) DEFAULT NULL,
  `job_make_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_job_unit_config`
--

DROP TABLE IF EXISTS `rcd_job_unit_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_job_unit_config` (
  `job_unit_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_unit_name` varchar(40) NOT NULL,
  `job_id` int(11) NOT NULL,
  `job_unit_active` int(11) NOT NULL DEFAULT '0' COMMENT '0:未启用\r\n1:启用\r\n默认不启用',
  `job_unit_type` int(11) NOT NULL DEFAULT '0' COMMENT '0:表格\r\n1:单项\r\n默认值：0',
  `job_unit_cycle` int(11) DEFAULT NULL COMMENT '填报周期:\n0.日报\n1.周报\n2.月报\n3.季报\n4.年报',
  PRIMARY KEY (`job_unit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_job_unit_fld`
--

DROP TABLE IF EXISTS `rcd_job_unit_fld`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_job_unit_fld` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_unit_id` int(11) NOT NULL,
  `fld_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=196 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_job_unit_flow`
--

DROP TABLE IF EXISTS `rcd_job_unit_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_job_unit_flow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `unit_id` int(11) NOT NULL,
  `edit_after_sub` int(11) NOT NULL DEFAULT '1' COMMENT '填报数据提交后该填报组是否可由填报人继续修改\n0：可修改\n1：不可修改',
  `edit_reviewer` int(11) NOT NULL DEFAULT '0' COMMENT '报送数据是否可由审批人修改\n0：可修改\n1：不可修改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_job_unit_interval`
--

DROP TABLE IF EXISTS `rcd_job_unit_interval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_job_unit_interval` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `job_unit_id` int(11) NOT NULL,
  `job_unit_start` varchar(45) NOT NULL COMMENT '填报区间起始日期/时间/周几',
  `job_unit_end` varchar(45) NOT NULL COMMENT '填报区间截止日期/时间/周几',
  `interval_status` int(11) NOT NULL DEFAULT '0' COMMENT '0:有效\n1:无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_person_config`
--

DROP TABLE IF EXISTS `rcd_person_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_person_config` (
  `user_id` bigint(20) NOT NULL,
  `origin_id` bigint(20) DEFAULT NULL,
  `config_status` int(11) DEFAULT '0' COMMENT '0:有效\n1:无效',
  `group_id` int(11) DEFAULT NULL COMMENT '所属填报组id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_person_group`
--

DROP TABLE IF EXISTS `rcd_person_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_person_group` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '填报组id',
  `group_name` varchar(200) NOT NULL COMMENT '填报用户组名称',
  `group_active` int(11) NOT NULL COMMENT '填报组是否启用',
  `group_creater` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_report_job`
--

DROP TABLE IF EXISTS `rcd_report_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_report_job` (
  `report_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `record_user_id` bigint(20) DEFAULT NULL,
  `record_origin_id` bigint(20) DEFAULT NULL,
  `record_status` int(11) DEFAULT NULL,
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_reportfile_log`
--

DROP TABLE IF EXISTS `rcd_reportfile_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_reportfile_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `report_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `log_status` int(11) NOT NULL COMMENT '0:生成中\n1:生成完毕\n2:生成失败',
  `log_user` bigint(20) NOT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `file_path` varchar(200) DEFAULT NULL,
  `log_type` int(11) NOT NULL DEFAULT '1' COMMENT '0:单独填报任务中填报组合导出\n1:任选填报任务中部分填报指标导出\n2:整个任务数据导出\n3:用户上传文件',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rcd_sms_templates`
--

DROP TABLE IF EXISTS `rcd_sms_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rcd_sms_templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_id` varchar(45) DEFAULT NULL,
  `template_name` varchar(45) DEFAULT NULL,
  `template_content` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_sms_config`
--

DROP TABLE IF EXISTS `report_sms_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report_sms_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_name` varchar(100) DEFAULT NULL COMMENT '配置名称',
  `report_defined_id` int(11) DEFAULT NULL COMMENT '填报任务定义id',
  `sms_template_id` varchar(40) DEFAULT NULL COMMENT '短信模板ID',
  `send_date` date DEFAULT NULL COMMENT '发送日期',
  `send_time` timestamp NULL DEFAULT NULL COMMENT '发送时间',
  `distance_days` int(11) DEFAULT NULL,
  `cross_holiday` char(1) DEFAULT NULL COMMENT 'Y:略过假期 N:不略过',
  `send_status` char(1) DEFAULT '0' COMMENT '0:待发送\n1:发送中\n2:已发送\n3:发送失败',
  `distance_type` int(11) DEFAULT NULL,
  `config_status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `send_sms_record`
--

DROP TABLE IF EXISTS `send_sms_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `send_sms_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sms_config_id` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `cust_name` varchar(45) DEFAULT NULL,
  `send_result` varchar(45) DEFAULT NULL,
  `faild_reason` varchar(300) DEFAULT NULL,
  `send_context` varchar(600) DEFAULT NULL,
  `sms_config_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5645 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_origin`
--

DROP TABLE IF EXISTS `sys_origin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_origin` (
  `origin_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '机构编号',
  `origin_name` varchar(45) DEFAULT NULL COMMENT '机构名称',
  `parent_origin_id` bigint(20) DEFAULT NULL COMMENT '上级机构编号',
  `origin_status` char(1) DEFAULT NULL COMMENT '0:启用 1:停用',
  `create_date` date DEFAULT NULL COMMENT '创建日期',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `origin_smp_name` varchar(45) DEFAULT NULL,
  `origin_type` char(1) NOT NULL DEFAULT '0' COMMENT '1:燃气企业\n2:管输企业\n0:全部\n9:未设定',
  `origin_address` varchar(200) DEFAULT NULL,
  `origin_address_detail` varchar(200) DEFAULT NULL,
  `origin_address_province` varchar(45) DEFAULT NULL,
  `origin_address_city` varchar(45) DEFAULT NULL,
  `origin_address_area` varchar(45) DEFAULT NULL,
  `origin_address_street` varchar(45) DEFAULT NULL,
  `origin_nature` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`origin_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=221685306619990017 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  `user_type` int(11) DEFAULT '1' COMMENT '1：填报用户\n2：监管用户\n0：审核用户\n3：系统用户',
  `reg_date` date DEFAULT NULL,
  `user_status` int(11) DEFAULT NULL COMMENT '(0,"正常")\n(1,"锁定")\n(2,"软删除")\n(3,"密码过期")\n(4,"停用")\n(5,"从未登陆")\n(6,"非正常标记")',
  `last_login_time` datetime DEFAULT NULL,
  `user_pwd` varchar(100) DEFAULT NULL,
  `user_name_cn` varchar(200) DEFAULT NULL,
  `office_phone` varchar(45) DEFAULT NULL COMMENT '办公电话',
  `mobile_phone` varchar(45) DEFAULT NULL COMMENT '手机号',
  `email` varchar(45) DEFAULT NULL COMMENT 'email地址',
  `social_code` varchar(45) DEFAULT NULL COMMENT '统一社会代码',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_organizations_assign`
--

DROP TABLE IF EXISTS `user_organizations_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_organizations_assign` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `organization_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_origin_assign`
--

DROP TABLE IF EXISTS `user_origin_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_origin_assign` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `origin_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=453458 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_role_name` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role_assign`
--

DROP TABLE IF EXISTS `user_role_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role_assign` (
  `user_id` bigint(20) NOT NULL,
  `user_role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`user_role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role_privilege`
--

DROP TABLE IF EXISTS `user_role_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role_privilege` (
  `user_role_id` int(11) NOT NULL,
  `module_id` int(11) NOT NULL,
  `access_privilege` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`user_role_id`,`module_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-31 15:18:36
