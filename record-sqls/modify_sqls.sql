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

