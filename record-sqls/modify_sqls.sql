ALTER TABLE `user` 
CHANGE COLUMN `user_type` `user_type` INT(11) NULL DEFAULT '2' COMMENT '1：填报用户\n2：监管用户\n0：审核用户\n3：系统用户' ;
UPDATE `app_module` SET `module_url`='/sys/record/recordOriginManager' WHERE `module_id`='64';
DELETE FROM `user_role_privilege` WHERE `user_role_id`='1' and`module_id`='65';
DELETE FROM `user_role_privilege` WHERE `user_role_id`='1' and`module_id`='66';
DELETE FROM `user_role_privilege` WHERE `user_role_id`='1' and`module_id`='67';
ALTER TABLE `rcd_dt_fld`
ADD COLUMN `fld_range` INT(11) NOT NULL DEFAULT 0 COMMENT '取值范围：0-所有、1-移动端、2-PC端' AFTER `is_actived`,
ADD COLUMN `fld_visible` INT(11) NOT NULL DEFAULT 0 COMMENT '可见范围：0-全部、1-移动端可见、2-PC端可见' AFTER `fld_range`;
ALTER TABLE `user_role` 
CHANGE COLUMN `user_role_id` `user_role_id` INT(11) NOT NULL AUTO_INCREMENT ;
ALTER TABLE `rcd_job_unit_config` ADD COLUMN `job_unit_cycle` INT(11) NULL COMMENT '填报周期:\n0.日报\n1.周报\n2.半月报\n3.月报\n4.季报\n5.年报' AFTER `job_unit_type` ;
CREATE TABLE `job_unit_record_interval` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `job_unit_id` INT NOT NULL,
  `job_unit_start` VARCHAR(45) NOT NULL COMMENT '填报区间起始日期/时间/周几',
  `job_unit_end` VARCHAR(45) NOT NULL COMMENT '填报区间截止日期/时间/周几',
  PRIMARY KEY (`id`));
ALTER TABLE `job_unit_record_interval`  RENAME TO `rcd_job_unit_interval` ;
ALTER TABLE `rcd_job_unit_interval` ADD COLUMN `job_id` INT NOT NULL AFTER `id`;
CREATE TABLE `rcd_job_unit_flow` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `job_id` INT NOT NULL,
  `unit_id` INT NOT NULL,
  `edit_after_sub` INT NOT NULL DEFAULT 1 COMMENT '填报数据提交后该填报组是否可由填报人继续修改\n0：可修改\n1：不可修改',
  `edit_reviewer` INT NOT NULL DEFAULT 0 COMMENT '报送数据是否可由审批人修改\n0：可修改\n1：不可修改',
  PRIMARY KEY (`id`));
ALTER TABLE `rcd_dt_fld` CHANGE COLUMN `is_actived` `is_actived` INT(11) NOT NULL DEFAULT '0' COMMENT '0:不启用\n1:启用' ;
ALTER TABLE `rcd_dt_fld`
ADD COLUMN `fld_status` VARCHAR(45) NULL COMMENT '指标状态：\n0：待审批\n1：审批通过\n2：审批驳回\n3：作废' AFTER `fld_visible`;
ALTER TABLE `rcd_job_config`
ADD COLUMN `job_creater` INT(11) NOT NULL COMMENT '任务创建人' AFTER `job_end_dt`,
ADD COLUMN `job_creater_origin` INT NOT NULL  COMMENT '任务创建人所属机构' AFTER `job_creater`;
INSERT INTO `app_module` (`module_id`, `super_module_id`, `module_name`, `module_url`) VALUES ('4', '0', '审批管理', '/rcdflow');
INSERT INTO `app_module` (`module_id`, `super_module_id`, `module_name`, `module_url`) VALUES ('41', '4', '指标定义审批', '/rcdflow/fldConfigReview');
INSERT INTO `app_module` (`module_id`, `super_module_id`, `module_name`, `module_url`) VALUES ('42', '4', '任务定义审批', '/rcdflow/jobConfigReview');
ALTER TABLE `rcd_dt_fld`
ADD COLUMN `fld_creater` INT(11) NOT NULL COMMENT '指标创建人' ,
ADD COLUMN `fld_creater_origin` INT NOT NULL  COMMENT '指标创建人所属机构' ;
ALTER TABLE `rcd_dt_fld` CHANGE COLUMN `fld_creater_origin` `fld_creater_origin` INT(11) NOT NULL DEFAULT 1 ;
ALTER TABLE `rcd_dt_fld` CHANGE COLUMN `fld_status` `fld_status` VARCHAR(45) NULL DEFAULT 0 COMMENT '指标状态：\n0：待审批\n1：审批通过\n2：审批驳回\n3：作废' ;
update rcd_dt_fld set fld_status = 0;
ALTER TABLE `rcd_dt_fld`
CHANGE COLUMN `fld_status` `fld_status` VARCHAR(45) NOT NULL DEFAULT '0' COMMENT '指标状态：\n0：待审批\n1：审批通过\n2：审批驳回\n3：作废' ;
ALTER TABLE `rcd_job_config`
CHANGE COLUMN `job_status` `job_status` INT(11) NOT NULL DEFAULT '0' COMMENT ' * 0:正常\n * 1:失效\n * 2:锁定\n * 3:软删除\n * 4:已发布\n * 5:发布中\n * 6:待审批\n * 7:审批通过\n * 8:审批驳回' ;

UPDATE `app_module` SET `module_id`='43', `super_module_id`='4' WHERE `module_id`='32';
update user_role_privilege set module_id = '43' where module_id = '32';

DROP TABLE IF EXISTS `rcd_job_interval`;
CREATE TABLE `rcd_job_interval` (
  `job_id` INT NOT NULL,
  `job_start` VARCHAR(45) NOT NULL,
  `job_end` VARCHAR(45) NOT NULL COMMENT '任务填报周期',
  PRIMARY KEY (`job_id`));

ALTER TABLE `rcd_job_config`
ADD COLUMN `job_cycle` INT(11) NOT NULL COMMENT '填报周期:\n0.日报\n1.周报\n2.月报\n3.季报\n4.年报' AFTER `job_creater_origin`;

ALTER TABLE `rcd_job_interval`
CHANGE COLUMN `job_end` `job_end` VARCHAR(45) NOT NULL COMMENT '任务填报周期' AFTER `job_id`,
ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`);

ALTER TABLE `rcd_job_interval`
CHANGE COLUMN `job_start` `job_start` VARCHAR(45) NOT NULL AFTER `job_id`;

ALTER TABLE `rcd_job_interval`
CHANGE COLUMN `job_start` `job_interval_start` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `job_end` `job_interval_end` VARCHAR(45) NOT NULL COMMENT '任务填报周期' ;

DROP TABLE IF EXISTS `rcd_job_flowlog`
CREATE TABLE `rcd_job_flowlog` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `job_id` INT NULL,
  `job_flow_status` INT NULL COMMENT '审批状态:通过还是驳回还是其他,值对应\nrcd_job_config.job_status',
  `job_flow_comment` VARCHAR(400) NULL COMMENT '审批处理备注',
  PRIMARY KEY (`id`));

ALTER TABLE `rcd_job_flowlog`
ADD COLUMN `job_flow_user` INT NULL COMMENT '审批人' AFTER `job_flow_comment`;

ALTER TABLE `rcd_job_flowlog`
ADD COLUMN `job_flow_date` TIMESTAMP NULL COMMENT '审批时间' AFTER `job_flow_user`;

update rcd_job_config set job_cycle = 2 where job_cycle!=2;

UPDATE `app_module` SET `module_url`='/rcdflow/dtRpReview' WHERE `module_id`='43';

ALTER TABLE `rcd_job_unit_interval`
ADD COLUMN `interval_status` INT NOT NULL COMMENT '0:有效\n1:无效' AFTER `job_unit_end`;

ALTER TABLE `rcd_person_config`
ADD COLUMN `config_status` INT NULL COMMENT '0:有效\n1:无效' AFTER `origin_id`;

ALTER TABLE `rcd_person_config`
CHANGE COLUMN `config_status` `config_status` INT(11) NULL DEFAULT 0 COMMENT '0:有效\n1:无效' ;
ALTER TABLE `rcd_job_unit_interval`
CHANGE COLUMN `interval_status` `interval_status` INT(11) NOT NULL DEFAULT 0 COMMENT '0:有效\n1:无效' ;
ALTER TABLE `rcd_job_person_assign`
ADD COLUMN `assign_status` INT NOT NULL DEFAULT 0 AFTER `job_id`;
ALTER TABLE `rcd_job_person_assign`
CHANGE COLUMN `assign_status` `assign_status` INT(11) NOT NULL DEFAULT '0' COMMENT '0:有效\n1:无效' ;

ALTER TABLE `rcd_dt_proj`
ADD COLUMN `proj_creater` INT NULL AFTER `is_actived`,
ADD COLUMN `proj_creater_origin` INT NULL AFTER `proj_creater`;
ALTER TABLE `rcd_dt_catg`
ADD COLUMN `catg_creater` INT NULL AFTER `is_actived`,
ADD COLUMN `catg_creater_origin` INT NULL AFTER `catg_creater`;
ALTER TABLE `rcd_dt_dict`
ADD COLUMN `dict_creater` INT NULL AFTER `dict_name`,
ADD COLUMN `dict_creater_origin` INT NULL AFTER `dict_creater`;

ALTER TABLE `user_origin_assign`
CHANGE COLUMN `user_id` `user_id` BIGINT NULL DEFAULT NULL ,
CHANGE COLUMN `origin_id` `origin_id` BIGINT NULL DEFAULT NULL ;

ALTER TABLE `sys_origin`
CHANGE COLUMN `origin_id` `origin_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '机构编号' ,
CHANGE COLUMN `parent_origin_id` `parent_origin_id` BIGINT NULL DEFAULT NULL COMMENT '上级机构编号' ;

ALTER TABLE `user`
CHANGE COLUMN `user_id` `user_id` BIGINT NOT NULL ;

ALTER TABLE `rcd_dt_catg`
CHANGE COLUMN `catg_creater` `catg_creater` BIGINT NULL DEFAULT NULL ,
CHANGE COLUMN `catg_creater_origin` `catg_creater_origin` BIGINT NULL DEFAULT NULL ;

ALTER TABLE `rcd_dt_dict`
CHANGE COLUMN `dict_creater` `dict_creater` BIGINT NULL DEFAULT NULL ,
CHANGE COLUMN `dict_creater_origin` `dict_creater_origin` BIGINT NULL DEFAULT NULL ;

ALTER TABLE `rcd_dt_fld`
CHANGE COLUMN `fld_creater` `fld_creater` BIGINT NOT NULL ,
CHANGE COLUMN `fld_creater_origin` `fld_creater_origin` BIGINT NOT NULL DEFAULT '1' ;

ALTER TABLE `rcd_dt_proj`
CHANGE COLUMN `proj_creater` `proj_creater` BIGINT NULL DEFAULT NULL ,
CHANGE COLUMN `proj_creater_origin` `proj_creater_origin` BIGINT NULL DEFAULT NULL ;

ALTER TABLE `rcd_job_config`
CHANGE COLUMN `job_creater` `job_creater` BIGINT NOT NULL COMMENT '任务创建人' ,
CHANGE COLUMN `job_creater_origin` `job_creater_origin` BIGINT NOT NULL ;

UPDATE `user_role_assign` SET `user_id`='1' WHERE `user_id`='0' and`user_role_id`='2';
INSERT INTO `user_role_assign` (`user_id`, `user_role_id`) VALUES ('8', '2');

ALTER TABLE `rcd_person_config`
CHANGE COLUMN `user_id` `user_id` BIGINT NOT NULL ,
CHANGE COLUMN `origin_id` `origin_id` BIGINT NOT NULL ;

ALTER TABLE `rcd_report_job`
CHANGE COLUMN `record_user_id` `record_user_id` BIGINT NULL DEFAULT NULL ,
CHANGE COLUMN `record_origin_id` `record_origin_id` BIGINT NULL DEFAULT NULL ;

ALTER TABLE `rcd_dt_fld`
CHANGE COLUMN `fld_data_type` `fld_data_type` VARCHAR(45) NOT NULL COMMENT '指标数据类型\n0:字符串\r\n1:数字\r\n2:日期\r\n3:数据字典\n4.坐标\n5.图片\n6.附件' ;

ALTER TABLE `rcd_job_person_assign`
CHANGE COLUMN `user_id` `user_id` BIGINT NOT NULL ;

ALTER TABLE `rcd_job_flowlog`
CHANGE COLUMN `job_flow_user` `job_flow_user` BIGINT NULL DEFAULT NULL COMMENT '审批人' ;

ALTER TABLE `sys_origin`
CHANGE COLUMN `origin_id` `origin_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '机构编号' ,
CHANGE COLUMN `parent_origin_id` `parent_origin_id` BIGINT NULL DEFAULT NULL COMMENT '上级机构编号' ,
CHANGE COLUMN `create_user` `create_user` BIGINT NULL DEFAULT NULL COMMENT '创建人' ;

CREATE TABLE `rcd_person_group` (
  `group_id` INT NOT NULL AUTO_INCREMENT COMMENT '填报组id',
  `group_name` VARCHAR(200) NOT NULL COMMENT '填报用户组名称',
  `group_active` INT NOT NULL COMMENT '填报组是否启用',
  PRIMARY KEY (`group_id`));

ALTER TABLE `rcd_person_config`
ADD COLUMN `group_id` INT(11) NULL COMMENT '所属填报组id' AFTER `config_status`;

ALTER TABLE `rcd_person_config`
CHANGE COLUMN `origin_id` `origin_id` BIGINT(20) NULL ;

CREATE TABLE `rcd_job_person_group_log` (
  `job_id` INT NOT NULL,
  `group_id` INT NULL,
  `group_name` VARCHAR(200) NULL,
  `job_make_date` DATE NULL,
  PRIMARY KEY (`job_id`));

CREATE TABLE `rcd_reportfile_log` (
`log_id` INT NOT NULL AUTO_INCREMENT,
`report_id` INT NOT NULL,
`job_id` INT NOT NULL,
`start_time` TIMESTAMP NULL,
`end_time` TIMESTAMP NULL,
`log_status` INT NOT NULL COMMENT '0:生成中\n1:生成完毕\n2:生成失败',
`log_user` BIGINT NOT NULL,
PRIMARY KEY (`log_id`));

ALTER TABLE `rcd_reportfile_log`
ADD COLUMN `comment` VARCHAR(500) NULL AFTER `log_user`;

ALTER TABLE `rcd_reportfile_log`
ADD COLUMN `file_path` VARCHAR(200) NOT NULL AFTER `comment`;

ALTER TABLE `rcd_reportfile_log`
CHANGE COLUMN `file_path` `file_path` VARCHAR(200) NULL ;

ALTER TABLE `rcd_reportfile_log`
ADD COLUMN `log_type` INT NOT NULL DEFAULT 1 COMMENT '0:单独填报任务中填报组合导出\n1:任选填报任务中部分填报指标导出' AFTER `file_path`;

CREATE TABLE `rcd_imp_template_log` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `job_id` INT NOT NULL,
  `template_file_path` VARCHAR(500) NOT NULL,
  `template_create_date` DATE NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `rcd_imp_template_unit_log` (
  `id` INT NOT NULL,
  `job_id` INT NOT NULL,
  `unit_id` INT NOT NULL,
  `unit_order` INT NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `rcd_imp_template_fld_log` (
  `id` INT NOT NULL,
  `job_id` INT NOT NULL,
  `unit_id` INT NOT NULL,
  `fld_id` INT NOT NULL,
  `fld_order` INT NOT NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `rcd_imp_template_log`
DROP COLUMN `id`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`job_id`);

ALTER TABLE `rcd_imp_template_unit_log`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `rcd_imp_template_fld_log`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `rcd_report_data_job5`
ADD COLUMN `data_status` INT NULL COMMENT '0:填报中\n1:已提交\n2:未提交' AFTER `record_data`;

update rcd_person_group set group_active='1';

ALTER TABLE `rcd_job_person_group_log`
CHANGE COLUMN `group_id` `group_id` INT(11) NOT NULL ,
ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`);

ALTER TABLE `rcd_person_group`
ADD COLUMN `group_creater` INT NULL AFTER `group_active`;

ALTER TABLE `rcd_reportfile_log`
CHANGE COLUMN `log_type` `log_type` INT(11) NOT NULL DEFAULT '1' COMMENT '0:单独填报任务中填报组合导出\n1:任选填报任务中部分填报指标导出\n2:整个任务数据导出' ;

ALTER TABLE `rcd_person_group`
CHANGE COLUMN `group_creater` `group_creater` BIGINT NULL DEFAULT NULL ;

ALTER TABLE `rcd_reportfile_log`
CHANGE COLUMN `log_type` `log_type` INT(11) NOT NULL DEFAULT '1' COMMENT '0:单独填报任务中填报组合导出\n1:任选填报任务中部分填报指标导出\n2:整个任务数据导出\n3:用户上传文件' ;

CREATE TABLE `rcd_sms_templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_id` varchar(45) DEFAULT NULL,
  `template_name` varchar(45) DEFAULT NULL,
  `template_content` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


CREATE TABLE `report_sms_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_name` varchar(100) DEFAULT NULL COMMENT '配置名称',
  `report_defined_id` int(11) DEFAULT NULL COMMENT '填报任务定义id',
  `sms_template_id` varchar(40) DEFAULT NULL COMMENT '短信模板ID',
  `send_date` date DEFAULT NULL COMMENT '发送日期',
  `send_time` timestamp NULL DEFAULT NULL COMMENT '发送时间',
  `pre_days` int(11) DEFAULT NULL,
  `cross_holiday` char(1) DEFAULT NULL COMMENT 'Y:略过假期 N:不略过',
  `send_status` char(1) DEFAULT '0' COMMENT '0:待发送\n1:发送中\n2:已发送\n3:发送失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `report_sms_config`
CHANGE COLUMN `pre_days` `distance_days` INT(11) NULL DEFAULT NULL ;
ALTER TABLE `report_sms_config`
ADD COLUMN `distance_type` INT(11) NULL AFTER `send_status`;
ALTER TABLE `report_sms_config`
ADD COLUMN `config_status` INT(11) NULL AFTER `distance_type`;
ALTER TABLE `report_sms_config`
CHANGE COLUMN `config_status` `config_status` INT(11) NULL DEFAULT 0 ;
INSERT INTO `rcd_sms_templates` (template_id, `template_name`, `template_content`)
VALUES ( '1', '填报周期开始前提醒', '您好，您有一个填报任务[${jobName}]需要填写，请您在以下填报日期进行填写：[${reportDate}]');
