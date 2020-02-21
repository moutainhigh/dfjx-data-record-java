-- MySQL dump 10.13  Distrib 5.7.29, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: data_record_clean
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
INSERT INTO `app_module` VALUES (1,0,'填报指标','/rcdconfig'),(2,0,'填报任务','/rcdjobconfig'),(3,0,'填报','/rcdjob'),(6,0,'权限管理','/sys'),(11,1,'填报指标体系','/rcdconfig/fldconfig'),(12,1,'数据字典','/rcdconfig/dictconfig'),(21,2,'填报任务维护','/rcdjobconfig/jobconfig'),(22,2,'填报组维护','/rcdjobconfig/unitconfig'),(23,2,'填报人维护','/rcdjobconfig/rcdusercg'),(24,2,'填报提醒维护','/rcdjobconfig/rcdnotice'),(31,3,'数据填报','/rcdjob/datareport'),(61,6,'功能查看','/sys/menu'),(62,6,'用户管理','/sys/user'),(63,6,'角色管理','/sys/role'),(64,6,'机构管理','/sys/record/recordOriginManager'),(65,6,'行政机构管理','/record/administrative'),(66,6,'监管用户管理','/sys/supervisionUser'),(67,6,'短信发送设置','/sms/smsMain');
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
  PRIMARY KEY (`catg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_catg`
--

LOCK TABLES `rcd_dt_catg` WRITE;
/*!40000 ALTER TABLE `rcd_dt_catg` DISABLE KEYS */;
INSERT INTO `rcd_dt_catg` VALUES (1,'基本属性',1,0),(2,'教育信息',1,0),(3,'资产信息',1,0),(4,'获奖信息',3,0),(5,'填报单位基本信息',4,0);
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
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_dict`
--

LOCK TABLES `rcd_dt_dict` WRITE;
/*!40000 ALTER TABLE `rcd_dt_dict` DISABLE KEYS */;
INSERT INTO `rcd_dt_dict` VALUES (1,'性别'),(2,'学历'),(3,'是否'),(4,'省份直辖市'),(5,'新增');
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
  `is_actived` int(11) NOT NULL DEFAULT '0',
  `fld_range` int(11) NOT NULL DEFAULT '0' COMMENT '取值范围：0-所有、1-移动端、2-PC端',
  `fld_visible` int(11) NOT NULL DEFAULT '0' COMMENT '可见范围：0-全部、1-移动端可见、2-PC端可见',
  PRIMARY KEY (`fld_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_fld`
--

LOCK TABLES `rcd_dt_fld` WRITE;
/*!40000 ALTER TABLE `rcd_dt_fld` DISABLE KEYS */;
INSERT INTO `rcd_dt_fld` VALUES (1,2,'姓名',NULL,'0',0,1,0,0,0),(1,3,'年龄','岁','1',0,1,0,0,0),(1,4,'性别',NULL,'3',0,1,0,0,0),(1,5,'籍贯',NULL,'3',0,1,0,0,0),(2,6,'教育程度',NULL,'3',0,1,0,0,0),(2,7,'就读学校',NULL,'0',0,0,0,0,0),(2,8,'是否三好学生',NULL,'3',0,0,0,0,0),(4,9,'发明奖项',NULL,'0',0,0,0,0,0),(1,10,'出生日期',NULL,'2',0,1,0,0,0),(1,11,'阿萨德撒多',NULL,'0',0,0,0,0,0),(5,12,'单位名称',NULL,'0',0,0,0,2,2),(5,13,'单位电话号码',NULL,'1',0,0,0,2,2),(5,14,'单位联系人',NULL,'0',0,0,0,2,2),(5,15,'单位联系人手机号',NULL,'1',0,0,0,2,2);
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
INSERT INTO `rcd_dt_fld_ct_assign` VALUES (1,4),(2,4),(3,4),(4,6),(5,6),(6,6),(7,6),(8,6),(9,6),(10,6),(11,6),(12,6),(13,6),(16,5),(17,5),(18,5),(19,5),(20,5),(21,5),(22,5),(23,5),(24,5),(25,5),(26,5),(27,5),(28,5),(29,5),(30,5),(31,5),(32,5),(33,5),(34,5),(14,8),(15,8);
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
  PRIMARY KEY (`proj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_proj`
--

LOCK TABLES `rcd_dt_proj` WRITE;
/*!40000 ALTER TABLE `rcd_dt_proj` DISABLE KEYS */;
INSERT INTO `rcd_dt_proj` VALUES (1,'人',0),(2,'地',0),(3,'物',0),(4,'组',0);
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
  `job_status` int(11) NOT NULL DEFAULT '0' COMMENT ' * 0:正常\n * 1:失效\n * 2:锁定\n * 3:软删除\n * 4:已发布\n * 5:发布中',
  `job_start_dt` date DEFAULT NULL,
  `job_end_dt` date DEFAULT NULL,
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_config`
--

LOCK TABLES `rcd_job_config` WRITE;
/*!40000 ALTER TABLE `rcd_job_config` DISABLE KEYS */;
INSERT INTO `rcd_job_config` VALUES (1,'开发填报任务',4,'2020-01-01','2021-01-01'),(2,'新增填报任务',0,'2020-02-12','2021-02-03'),(3,'单组任务',4,'2020-02-04','2021-02-17'),(4,'疫情信息',4,'2020-02-01','2021-02-28');
/*!40000 ALTER TABLE `rcd_job_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_job_person_assign`
--

DROP TABLE IF EXISTS `rcd_job_person_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_job_person_assign` (
  `user_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_person_assign`
--

LOCK TABLES `rcd_job_person_assign` WRITE;
/*!40000 ALTER TABLE `rcd_job_person_assign` DISABLE KEYS */;
INSERT INTO `rcd_job_person_assign` VALUES (20,1),(20,3),(20,4);
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
  PRIMARY KEY (`job_unit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_unit_config`
--

LOCK TABLES `rcd_job_unit_config` WRITE;
/*!40000 ALTER TABLE `rcd_job_unit_config` DISABLE KEYS */;
INSERT INTO `rcd_job_unit_config` VALUES (1,'开发填报任务1组',1,0,0),(2,'开发填报任务2组',1,0,0),(3,'测试组1',2,1,0),(5,'测试组2',2,0,0),(6,'单组',3,1,0),(7,'人口信息采集',4,1,0),(8,'填报人信息',4,1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_job_unit_fld`
--

LOCK TABLES `rcd_job_unit_fld` WRITE;
/*!40000 ALTER TABLE `rcd_job_unit_fld` DISABLE KEYS */;
INSERT INTO `rcd_job_unit_fld` VALUES (1,1,2),(2,1,3),(3,1,4),(4,1,5),(5,2,6),(6,2,7),(7,2,9),(13,3,2),(14,3,3),(15,3,4),(16,3,10),(17,5,6),(18,5,7),(19,5,8),(20,7,2),(21,7,3),(22,7,4),(23,7,10),(24,8,12),(25,8,13),(26,8,14),(27,8,15);
/*!40000 ALTER TABLE `rcd_job_unit_fld` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_person_config`
--

DROP TABLE IF EXISTS `rcd_person_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_person_config` (
  `user_id` int(11) NOT NULL,
  `origin_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_person_config`
--

LOCK TABLES `rcd_person_config` WRITE;
/*!40000 ALTER TABLE `rcd_person_config` DISABLE KEYS */;
INSERT INTO `rcd_person_config` VALUES (55643376,1101001),(20,6590009);
/*!40000 ALTER TABLE `rcd_person_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_report_data_job1`
--

DROP TABLE IF EXISTS `rcd_report_data_job1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_report_data_job1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `report_id` int(11) DEFAULT NULL,
  `unit_id` int(11) DEFAULT NULL,
  `colum_id` int(11) DEFAULT NULL,
  `fld_id` int(11) DEFAULT NULL,
  `record_data` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_report_data_job1`
--

LOCK TABLES `rcd_report_data_job1` WRITE;
/*!40000 ALTER TABLE `rcd_report_data_job1` DISABLE KEYS */;
INSERT INTO `rcd_report_data_job1` VALUES (1,2,1,0,2,''),(2,2,1,0,3,''),(3,2,1,0,4,''),(4,2,1,0,5,''),(5,2,2,0,6,''),(6,2,2,0,7,''),(7,2,2,0,9,''),(8,1,1,0,2,'ssss'),(9,1,1,0,3,'35'),(10,1,1,0,4,'1'),(11,1,1,0,5,'3');
/*!40000 ALTER TABLE `rcd_report_data_job1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rcd_report_data_job4`
--

DROP TABLE IF EXISTS `rcd_report_data_job4`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rcd_report_data_job4` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `report_id` int(11) DEFAULT NULL,
  `unit_id` int(11) DEFAULT NULL,
  `colum_id` int(11) DEFAULT NULL,
  `fld_id` int(11) DEFAULT NULL,
  `record_data` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_report_data_job4`
--

LOCK TABLES `rcd_report_data_job4` WRITE;
/*!40000 ALTER TABLE `rcd_report_data_job4` DISABLE KEYS */;
INSERT INTO `rcd_report_data_job4` VALUES (5,5,8,0,12,'asdasd'),(6,5,8,1,13,'13010012002'),(7,5,8,2,14,'123123'),(8,5,8,3,15,'12312312'),(89,5,7,0,2,'scq2'),(90,5,7,0,3,'32'),(91,5,7,0,4,'0'),(92,5,7,0,10,'2020-02-12T16:00:00.000Z'),(93,5,7,1,2,'scq3'),(94,5,7,1,3,'33'),(95,5,7,1,4,'1'),(96,5,7,1,10,'2020-02-11T16:00:00.000Z'),(97,5,7,2,2,'scq'),(98,5,7,2,3,'22'),(99,5,7,2,4,'0'),(100,5,7,2,10,'2020-02-09T16:00:00.000Z');
/*!40000 ALTER TABLE `rcd_report_data_job4` ENABLE KEYS */;
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
  `record_user_id` int(11) DEFAULT NULL,
  `record_origin_id` int(11) DEFAULT NULL,
  `record_status` int(11) DEFAULT NULL,
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_report_job`
--

LOCK TABLES `rcd_report_job` WRITE;
/*!40000 ALTER TABLE `rcd_report_job` DISABLE KEYS */;
INSERT INTO `rcd_report_job` VALUES (1,1,20,NULL,0),(2,1,20,NULL,0),(3,3,20,6590009,0),(5,4,20,6590009,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=6590012 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_origin`
--

LOCK TABLES `sys_origin` WRITE;
/*!40000 ALTER TABLE `sys_origin` DISABLE KEYS */;
INSERT INTO `sys_origin` VALUES (1,'全国',0,'0','2019-04-29',NULL,'全国','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(1100000,'北京市',1,'0','2019-04-29',NULL,'北京市','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(1101000,'北京',1100000,'0','2019-05-14',NULL,'北京','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6590010,'测试新增机构',1101000,'1','2020-02-20',NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6590011,'asdsad',1,'3','2020-02-20',NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_origin_assign`
--

LOCK TABLES `user_origin_assign` WRITE;
/*!40000 ALTER TABLE `user_origin_assign` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_origin_assign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_role_id` int(11) NOT NULL,
  `user_role_name` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (0,'填报岗'),(1,'超级管理员');
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
INSERT INTO `user_role_privilege` VALUES (1,1,NULL),(1,2,NULL),(1,6,NULL),(1,11,NULL),(1,12,NULL),(1,21,NULL),(1,22,NULL),(1,23,NULL),(1,24,NULL),(1,61,NULL),(1,62,NULL),(1,63,NULL),(1,64,NULL);
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

-- Dump completed on 2020-02-21 11:37:39
