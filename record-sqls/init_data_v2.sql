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
-- Dumping data for table `app_module`
--

LOCK TABLES `app_module` WRITE;
/*!40000 ALTER TABLE `app_module` DISABLE KEYS */;
INSERT INTO `app_module` VALUES (1,0,'填报指标','/rcdconfig'),(2,0,'填报任务','/rcdjobconfig'),(3,0,'填报','/rcdjob'),(4,0,'审批管理','/rcdflow'),(6,0,'权限管理','/sys'),(11,1,'填报指标体系','/rcdconfig/fldconfig'),(12,1,'数据字典','/rcdconfig/dictconfig'),(21,2,'填报任务维护','/rcdjobconfig/jobconfig'),(22,2,'填报组维护','/rcdjobconfig/unitconfig'),(23,2,'填报人维护','/rcdjobconfig/rcdusercg'),(24,2,'填报提醒维护','/rcdjobconfig/rcdnotice'),(31,3,'数据填报','/rcdjob/datareport'),(42,4,'任务定义审批','/rcdflow/jobConfigReview'),(43,4,'填报数据审核','/rcdflow/dtRpReview'),(61,6,'功能查看','/sys/menu'),(62,6,'用户管理','/sys/user'),(63,6,'角色管理','/sys/role'),(64,6,'机构管理','/sys/record/recordOriginManager');
/*!40000 ALTER TABLE `app_module` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-31 14:32:20
