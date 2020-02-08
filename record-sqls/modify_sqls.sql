ALTER TABLE `user` 
CHANGE COLUMN `user_type` `user_type` INT(11) NULL DEFAULT '2' COMMENT '1：填报用户\n2：监管用户\n0：审核用户\n3：系统用户' ;
UPDATE `app_module` SET `module_url`='/sys/record/recordOriginManager' WHERE `module_id`='64';
DELETE FROM `user_role_privilege` WHERE `user_role_id`='1' and`module_id`='65';
DELETE FROM `user_role_privilege` WHERE `user_role_id`='1' and`module_id`='66';
DELETE FROM `user_role_privilege` WHERE `user_role_id`='1' and`module_id`='67';
