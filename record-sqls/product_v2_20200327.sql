-- MySQL dump 10.13  Distrib 5.7.29, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: data_record_syn
-- ------------------------------------------------------
-- Server version	5.7.29-0ubuntu0.18.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_module` (
  `module_id` int(11) NOT NULL,
  `super_module_id` int(11) DEFAULT NULL,
  `module_name` varchar(40) DEFAULT NULL,
  `module_url` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`module_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_module`
--

LOCK TABLES `app_module` WRITE;
/*!40000 ALTER TABLE `app_module` DISABLE KEYS */;
INSERT INTO `app_module` VALUES (1,0,'填报指标','/rcdconfig'),(2,0,'填报任务','/rcdjobconfig'),(3,0,'填报','/rcdjob'),(4,0,'审批管理','/rcdflow'),(6,0,'权限管理','/sys'),(11,1,'填报指标体系','/rcdconfig/fldconfig'),(12,1,'数据字典','/rcdconfig/dictconfig'),(21,2,'填报任务维护','/rcdjobconfig/jobconfig'),(22,2,'填报组维护','/rcdjobconfig/unitconfig'),(23,2,'填报人维护','/rcdjobconfig/rcdusercg'),(24,2,'填报提醒维护','/rcdjobconfig/rcdnotice'),(31,3,'数据填报','/rcdjob/datareport'),(42,4,'任务定义审批','/rcdflow/jobConfigReview'),(43,4,'填报数据审核','/rcdflow/dtRpReview'),(61,6,'功能查看','/sys/menu'),(62,6,'用户管理','/sys/user'),(63,6,'角色管理','/sys/role'),(64,6,'机构管理','/sys/record/recordOriginManager');
/*!40000 ALTER TABLE `app_module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_dt_catg`
--

DROP TABLE IF EXISTS `rcd_dt_catg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_dt_catg` (
  `catg_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类型编码',
  `catg_name` varchar(45) NOT NULL COMMENT '类型名称',
  `proj_id` int(11) NOT NULL COMMENT '基本类型编码',
  `is_actived` int(11) DEFAULT '0' COMMENT '是否有效\n0:有效\n1:无效',
  `catg_creater` bigint(20) DEFAULT NULL,
  `catg_creater_origin` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`catg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_catg`
--

LOCK TABLES `rcd_dt_catg` WRITE;
/*!40000 ALTER TABLE `rcd_dt_catg` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_dt_catg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_dt_dict`
--

DROP TABLE IF EXISTS `rcd_dt_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_dt_dict` (
  `dict_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据字典编码',
  `dict_name` varchar(40) NOT NULL COMMENT '数据字典名称',
  `dict_creater` bigint(20) DEFAULT NULL,
  `dict_creater_origin` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_dict`
--

LOCK TABLES `rcd_dt_dict` WRITE;
/*!40000 ALTER TABLE `rcd_dt_dict` DISABLE KEYS */;
INSERT INTO `rcd_dt_dict` VALUES (1,'性别',NULL,NULL),(2,'学历',NULL,NULL),(3,'是否',NULL,NULL),(4,'省份直辖市',NULL,NULL),(5,'新增',NULL,NULL);
/*!40000 ALTER TABLE `rcd_dt_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_dt_dict_content`
--

DROP TABLE IF EXISTS `rcd_dt_dict_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_dt_dict_content` (
  `dict_content_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据字典内容编码',
  `dict_id` int(11) NOT NULL COMMENT '数据字典编码',
  `dict_content_name` varchar(40) NOT NULL COMMENT '数据字典内容名称',
  `dict_content_value` varchar(40) NOT NULL COMMENT '数据字典内容值',
  PRIMARY KEY (`dict_content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_dict_content`
--

LOCK TABLES `rcd_dt_dict_content` WRITE;
/*!40000 ALTER TABLE `rcd_dt_dict_content` DISABLE KEYS */;
INSERT INTO `rcd_dt_dict_content` VALUES (1,1,'男性','1'),(2,1,'女性','2'),(3,1,'未知','0'),(4,2,'小学','0'),(5,2,'初中','1'),(6,2,'中专','2'),(7,2,'高中','3'),(8,2,'大专','4'),(9,2,'自考本科','5'),(10,2,'本科','6'),(11,2,'研究生','7'),(12,2,'博士','8'),(13,2,'博士后','9'),(14,3,'是','0'),(15,3,'否','1'),(16,4,'北京','0'),(17,4,'上海','1'),(18,4,'黑龙江','2'),(19,4,'吉林','3'),(20,4,'辽宁','4'),(21,4,'河北','5'),(22,4,'内蒙古','6'),(23,4,'山东','7'),(24,4,'山西','8'),(25,4,'江苏','9'),(26,4,'浙江','10'),(27,4,'四川','11'),(28,4,'重庆','12'),(29,4,'甘肃','13'),(30,4,'陕西','14'),(31,4,'新疆','15'),(32,4,'西藏','16'),(33,4,'广东','17'),(34,4,'海南','18'),(35,1,'新增','2'),(36,5,'新增个内容','1');
/*!40000 ALTER TABLE `rcd_dt_dict_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_dt_fld`
--

DROP TABLE IF EXISTS `rcd_dt_fld`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_dt_fld` (
  `catg_id` int(11) NOT NULL COMMENT '关联 rcd_dt_catg.catg_id',
  `fld_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '指标id',
  `fld_name` varchar(45) NOT NULL COMMENT '指标名称',
  `fld_point` varchar(45) DEFAULT NULL COMMENT '指标单位',
  `fld_data_type` varchar(45) NOT NULL COMMENT '指标数据类型\n0:字符串\r\n1:数字\r\n2:日期\r\n3:数据字典',
  `fld_type` int(11) NOT NULL COMMENT '指标类型\n0:通用指标\r\n1:突发指标',
  `fld_is_null` int(11) NOT NULL DEFAULT '0' COMMENT '0:可以\r\n1:不可以',
  `is_actived` int(11) NOT NULL DEFAULT '0' COMMENT '0:不启用\n1:启用',
  `fld_range` int(11) NOT NULL DEFAULT '0' COMMENT '取值范围：0-所有、1-移动端、2-PC端',
  `fld_visible` int(11) NOT NULL DEFAULT '0' COMMENT '可见范围：0-全部、1-移动端可见、2-PC端可见',
  `fld_status` varchar(45) DEFAULT '0' COMMENT '指标状态：\n0：待审批\n1：审批通过\n2：审批驳回\n3：作废',
  `fld_creater` bigint(20) NOT NULL,
  `fld_creater_origin` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`fld_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_fld`
--

LOCK TABLES `rcd_dt_fld` WRITE;
/*!40000 ALTER TABLE `rcd_dt_fld` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_dt_fld` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_dt_fld_ct_assign`
--

DROP TABLE IF EXISTS `rcd_dt_fld_ct_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_dt_fld_ct_assign` (
  `dict_content_id` int(11) NOT NULL,
  `fld_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_fld_ct_assign`
--

LOCK TABLES `rcd_dt_fld_ct_assign` WRITE;
/*!40000 ALTER TABLE `rcd_dt_fld_ct_assign` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_dt_fld_ct_assign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_dt_proj`
--

DROP TABLE IF EXISTS `rcd_dt_proj`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_dt_proj` (
  `proj_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类型编码',
  `proj_name` varchar(40) DEFAULT NULL COMMENT '类型名称',
  `is_actived` int(11) NOT NULL DEFAULT '0',
  `proj_creater` bigint(20) DEFAULT NULL,
  `proj_creater_origin` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`proj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_proj`
--

LOCK TABLES `rcd_dt_proj` WRITE;
/*!40000 ALTER TABLE `rcd_dt_proj` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_dt_proj` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_job_config`
--

DROP TABLE IF EXISTS `rcd_job_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_config`
--

LOCK TABLES `rcd_job_config` WRITE;
/*!40000 ALTER TABLE `rcd_job_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_job_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_job_flowlog`
--

DROP TABLE IF EXISTS `rcd_job_flowlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_job_flowlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) DEFAULT NULL,
  `job_flow_status` int(11) DEFAULT NULL COMMENT '审批状态:通过还是驳回还是其他,值对应\nrcd_job_config.job_status',
  `job_flow_comment` varchar(400) DEFAULT NULL COMMENT '审批处理备注',
  `job_flow_user` int(11) DEFAULT NULL COMMENT '审批人',
  `job_flow_date` timestamp NULL DEFAULT NULL COMMENT '审批时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_flowlog`
--

LOCK TABLES `rcd_job_flowlog` WRITE;
/*!40000 ALTER TABLE `rcd_job_flowlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_job_flowlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_job_interval`
--

DROP TABLE IF EXISTS `rcd_job_interval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_job_interval` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `job_interval_start` varchar(45) NOT NULL,
  `job_interval_end` varchar(45) NOT NULL COMMENT '任务填报周期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_interval`
--

LOCK TABLES `rcd_job_interval` WRITE;
/*!40000 ALTER TABLE `rcd_job_interval` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_job_interval` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_job_person_assign`
--

DROP TABLE IF EXISTS `rcd_job_person_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_job_person_assign` (
  `user_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  `assign_status` int(11) NOT NULL DEFAULT '0' COMMENT '0:有效\n1:无效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_person_assign`
--

LOCK TABLES `rcd_job_person_assign` WRITE;
/*!40000 ALTER TABLE `rcd_job_person_assign` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_job_person_assign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_job_unit_config`
--

DROP TABLE IF EXISTS `rcd_job_unit_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_job_unit_config` (
  `job_unit_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_unit_name` varchar(40) NOT NULL,
  `job_id` int(11) NOT NULL,
  `job_unit_active` int(11) NOT NULL DEFAULT '0' COMMENT '0:未启用\r\n1:启用\r\n默认不启用',
  `job_unit_type` int(11) NOT NULL DEFAULT '0' COMMENT '0:表格\r\n1:单项\r\n默认值：0',
  `job_unit_cycle` int(11) DEFAULT NULL COMMENT '填报周期:\n0.日报\n1.周报\n2.月报\n3.季报\n4.年报',
  PRIMARY KEY (`job_unit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_unit_config`
--

LOCK TABLES `rcd_job_unit_config` WRITE;
/*!40000 ALTER TABLE `rcd_job_unit_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_job_unit_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_job_unit_fld`
--

DROP TABLE IF EXISTS `rcd_job_unit_fld`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_job_unit_fld` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_unit_id` int(11) NOT NULL,
  `fld_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_unit_fld`
--

LOCK TABLES `rcd_job_unit_fld` WRITE;
/*!40000 ALTER TABLE `rcd_job_unit_fld` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_job_unit_fld` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_job_unit_flow`
--

DROP TABLE IF EXISTS `rcd_job_unit_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_job_unit_flow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `unit_id` int(11) NOT NULL,
  `edit_after_sub` int(11) NOT NULL DEFAULT '1' COMMENT '填报数据提交后该填报组是否可由填报人继续修改\n0：可修改\n1：不可修改',
  `edit_reviewer` int(11) NOT NULL DEFAULT '0' COMMENT '报送数据是否可由审批人修改\n0：可修改\n1：不可修改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_unit_flow`
--

LOCK TABLES `rcd_job_unit_flow` WRITE;
/*!40000 ALTER TABLE `rcd_job_unit_flow` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_job_unit_flow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_job_unit_interval`
--

DROP TABLE IF EXISTS `rcd_job_unit_interval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
-- Dumping data for table `rcd_job_unit_interval`
--

LOCK TABLES `rcd_job_unit_interval` WRITE;
/*!40000 ALTER TABLE `rcd_job_unit_interval` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_job_unit_interval` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_person_config`
--

DROP TABLE IF EXISTS `rcd_person_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_person_config` (
  `user_id` bigint(20) NOT NULL,
  `origin_id` bigint(20) NOT NULL,
  `config_status` int(11) DEFAULT '0' COMMENT '0:有效\n1:无效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_person_config`
--

LOCK TABLES `rcd_person_config` WRITE;
/*!40000 ALTER TABLE `rcd_person_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_person_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_report_job`
--

DROP TABLE IF EXISTS `rcd_report_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_report_job` (
  `report_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `record_user_id` bigint(20) DEFAULT NULL,
  `record_origin_id` bigint(20) DEFAULT NULL,
  `record_status` int(11) DEFAULT NULL,
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_report_job`
--

LOCK TABLES `rcd_report_job` WRITE;
/*!40000 ALTER TABLE `rcd_report_job` DISABLE KEYS */;
/*!40000 ALTER TABLE `rcd_report_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_origin`
--

DROP TABLE IF EXISTS `sys_origin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_origin` (
  `origin_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '机构编号',
  `origin_name` varchar(45) DEFAULT NULL COMMENT '机构名称',
  `parent_origin_id` int(11) DEFAULT NULL COMMENT '上级机构编号',
  `origin_status` char(1) DEFAULT NULL COMMENT '0:启用 1:停用',
  `create_date` date DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) DEFAULT NULL COMMENT '创建人',
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
) ENGINE=InnoDB AUTO_INCREMENT=6590015 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_origin`
--

LOCK TABLES `sys_origin` WRITE;
/*!40000 ALTER TABLE `sys_origin` DISABLE KEYS */;
INSERT INTO `sys_origin` VALUES (1,'全国',0,'0','2019-04-29',NULL,'全国','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(1100000,'北京市',1,'0','2019-04-29',NULL,'北京市','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(1101000,'北京',1100000,'0','2019-05-14',NULL,'北京','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2100000,'河北省',1,'0','2019-04-29',NULL,'河北省','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6590010,'测试新增机构',1101000,'1','2020-02-20',NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6590011,'asdsad',1,'3','2020-02-20',NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6590012,'多级机构北京1',1101000,'1','2020-02-29',NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6590013,'多级机构北京2',6590012,'1','2020-02-29',NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6590014,'多级机构北京3',6590013,'1','2020-02-29',NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_origin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
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
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (0,'admin',1,'2020-02-04',0,NULL,'-1ef523c6b645a65441a91fa80df077c2','超级管理员',NULL,'18012341234','1234@123.com',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_organizations_assign`
--

DROP TABLE IF EXISTS `user_organizations_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_organizations_assign` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `organization_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_organizations_assign`
--

LOCK TABLES `user_organizations_assign` WRITE;
/*!40000 ALTER TABLE `user_organizations_assign` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_organizations_assign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_origin_assign`
--

DROP TABLE IF EXISTS `user_origin_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_origin_assign` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `origin_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_origin_assign`
--

LOCK TABLES `user_origin_assign` WRITE;
/*!40000 ALTER TABLE `user_origin_assign` DISABLE KEYS */;
INSERT INTO `user_origin_assign` VALUES (1,12575276,6590010),(2,509317,6590010),(3,8798936,6590010),(4,2179054,1101000),(5,20,6590010),(6,1217720,6590014),(7,0,1);
/*!40000 ALTER TABLE `user_origin_assign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_role_name` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,'超级管理员岗');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role_assign`
--

DROP TABLE IF EXISTS `user_role_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role_assign` (
  `user_id` int(11) NOT NULL,
  `user_role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`user_role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role_assign`
--

LOCK TABLES `user_role_assign` WRITE;
/*!40000 ALTER TABLE `user_role_assign` DISABLE KEYS */;
INSERT INTO `user_role_assign` VALUES (0,1);
/*!40000 ALTER TABLE `user_role_assign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role_privilege`
--

DROP TABLE IF EXISTS `user_role_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role_privilege` (
  `user_role_id` int(11) NOT NULL,
  `module_id` int(11) NOT NULL,
  `access_privilege` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`user_role_id`,`module_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role_privilege`
--

LOCK TABLES `user_role_privilege` WRITE;
/*!40000 ALTER TABLE `user_role_privilege` DISABLE KEYS */;
INSERT INTO `user_role_privilege` VALUES (1,1,NULL),(1,2,NULL),(1,3,NULL),(1,4,NULL),(1,6,NULL),(1,11,NULL),(1,12,NULL),(1,21,NULL),(1,22,NULL),(1,23,NULL),(1,24,NULL),(1,31,NULL),(1,42,NULL),(1,43,NULL),(1,61,NULL),(1,62,NULL),(1,63,NULL),(1,64,NULL);
/*!40000 ALTER TABLE `user_role_privilege` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-27 18:05:39
