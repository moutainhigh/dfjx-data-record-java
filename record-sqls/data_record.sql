-- MySQL dump 10.13  Distrib 5.7.28, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: data_record
-- ------------------------------------------------------
-- Server version	5.7.28-0ubuntu0.18.04.4

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
INSERT INTO `app_module` VALUES (1,0,'填报指标','/rcdconfig'),(2,0,'填报任务','/rcdjobconfig'),(3,0,'填报','/rcdjob'),(6,0,'权限管理','/sys'),(11,1,'填报指标体系','/rcdconfig/fldconfig'),(12,1,'数据字典','/rcdconfig/dictconfig'),(21,2,'填报任务维护','/rcdjobconfig/jobconfig'),(22,2,'填报组维护','/rcdjobconfig/unitconfig'),(23,2,'填报人维护','/rcdjobconfig/rcdusercg'),(24,2,'填报提醒维护','/rcdjobconfig/rcdnotice'),(31,3,'数据填报','/rcdjob/datareport'),(61,6,'功能查看','/sys/menu'),(62,6,'用户管理','/sys/user'),(63,6,'角色管理','/sys/role'),(64,6,'机构管理','/record/submitAUmanager'),(65,6,'行政机构管理','/record/administrative'),(66,6,'监管用户管理','/sys/supervisionUser'),(67,6,'短信发送设置','/sms/smsMain');
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
  `rcd_dt_dictcol` varchar(40) NOT NULL COMMENT '数据字典名称',
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_dict`
--

LOCK TABLES `rcd_dt_dict` WRITE;
/*!40000 ALTER TABLE `rcd_dt_dict` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rcd_dt_dict_content`
--

LOCK TABLES `rcd_dt_dict_content` WRITE;
/*!40000 ALTER TABLE `rcd_dt_dict_content` DISABLE KEYS */;
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
  `fld_id` int(11) NOT NULL COMMENT '指标id',
  `fld_name` varchar(45) NOT NULL COMMENT '指标名称',
  `fld_point` varchar(45) NOT NULL COMMENT '指标单位',
  `fld_data_type` varchar(45) NOT NULL COMMENT '指标数据类型\n0:字符串\r\n1:数字\r\n2:日期\r\n3:数据字典',
  `fld_type` int(11) NOT NULL COMMENT '指标类型\n0:通用指标\r\n1:突发指标',
  `fld_is_null` int(11) NOT NULL DEFAULT '0' COMMENT '0:可以\r\n1:不可以',
  `is_actived` int(11) NOT NULL DEFAULT '0',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_origin`
--

LOCK TABLES `sys_origin` WRITE;
/*!40000 ALTER TABLE `sys_origin` DISABLE KEYS */;
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
  `user_type` int(11) DEFAULT NULL COMMENT '1：填报用户\n2：监管用户\n0：审核用户\n3：系统用户',
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
INSERT INTO `user` VALUES (1,'duzhendi99',NULL,'2019-11-05',0,'2019-12-02 14:35:41','-1ef523c6b645a65441a91fa80df077c2','duzhendi99','123123','13716276523','duzhendi@163.com',NULL),(4,'duzhendi5',NULL,'2019-11-26',0,'2019-12-02 10:59:56','-1ef523c6b645a65441a91fa80df077c2','duzhendi5',NULL,'123456','123456@163',NULL),(5,'duzhendi10',NULL,'2019-11-26',0,'2019-12-02 11:02:47','-1ef523c6b645a65441a91fa80df077c2','duzhendi10',NULL,'18765456787','123456@163',NULL),(6,'duzhendi1',NULL,'2019-11-26',0,'2019-12-02 10:59:30','-1ef523c6b645a65441a91fa80df077c2','duzhendi1',NULL,'123456','123456@163',NULL),(14,'duzhendi7',NULL,'2019-11-27',0,'2019-12-02 11:04:00','-1ef523c6b645a65441a91fa80df077c2','duzhendi7',NULL,'13567653456','duzhendi111@163.com',NULL),(15,'duzhendi8',NULL,'2019-11-27',0,'2019-12-02 11:00:50','-1ef523c6b645a65441a91fa80df077c2','duzhendi8',NULL,'123456','123456@163',NULL),(17,'peipengjie',NULL,'2019-11-29',0,'2019-12-02 11:02:11','-1ef523c6b645a65441a91fa80df077c2','peipengjie',NULL,'13847589743','peipengjie@163.com',NULL),(18,'duzhendi',NULL,'2019-12-02',0,NULL,'-1ef523c6b645a65441a91fa80df077c2','duzhendi',NULL,'13294876012','13716189734@163.com',NULL),(19,'dududu',NULL,'2019-12-02',0,'2019-12-02 14:47:18','-1ef523c6b645a65441a91fa80df077c2','dududu',NULL,'134543234','duzhendi@163.com',NULL);
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
INSERT INTO `user_role` VALUES (1,'超级管理员');
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
INSERT INTO `user_role_assign` VALUES (1,1),(4,1),(5,1),(6,1),(14,1),(15,1),(17,1),(18,1),(19,1);
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
INSERT INTO `user_role_privilege` VALUES (1,1,NULL),(1,2,NULL),(1,3,NULL),(1,6,NULL),(1,11,NULL),(1,12,NULL),(1,21,NULL),(1,22,NULL),(1,23,NULL),(1,24,NULL),(1,31,NULL),(1,61,NULL),(1,62,NULL),(1,63,NULL),(1,64,NULL),(1,65,NULL),(1,66,NULL),(1,67,NULL);
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

-- Dump completed on 2020-01-09 16:34:51
