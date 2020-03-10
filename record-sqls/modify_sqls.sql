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
INSERT INTO `app_module` (`module_id`, `super_module_id`, `module_name`, `module_url`) VALUES ('42', '4', '任务定义审批', 'rcdflow/jobConfigReview');
ALTER TABLE `rcd_dt_fld` CHANGE COLUMN `fld_creater_origin` `fld_creater_origin` INT(11) NOT NULL DEFAULT 1 ;







