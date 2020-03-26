package com.workbench.authsyn.jinxin.batch;

import com.workbench.auth.user.service.UserService;
import com.workbench.authsyn.config.UserCenterProperties;
import com.workbench.authsyn.jinxin.entity.JinxinOrganization;
import com.workbench.authsyn.jinxin.entity.JinxinUser;
import com.workbench.authsyn.jinxin.service.JinxinAuthSynService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service("synchroization")
public class JinxinUserSynchronization {

    private Logger logger = LoggerFactory.getLogger(JinxinUserSynchronization.class);

    @Autowired
    private JinxinAuthSynService jinxinAuthSynService;

    @Autowired
    private UserCenterProperties userCenterProperties;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    private static final Integer DEFAULT_EACH_TIME = 60;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initialize(){
        //获取当天所有的待执行任务
        if(userCenterProperties.getEnable()){
            this.batchSyn();
        }
    }

    private void batchSyn(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                doSyn();
            }
        };

        ScheduledExecutorService exceutor = Executors.newSingleThreadScheduledExecutor();
        exceutor.scheduleAtFixedRate(
                runnable,
                1,
                userCenterProperties.getEachTime()!=null?userCenterProperties.getEachTime():DEFAULT_EACH_TIME,
                TimeUnit.MINUTES);
    }

    public void doSyn(){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态

        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logger.info("开始同步用户|机构信息");
            logger.info("开始获取用户信息:{}",format.format(new Date()));
            List<JinxinUser> jinxinUserList = jinxinAuthSynService.getUsersListFromJinxinUserCenter();
            logger.info("开始获取机构信息:{}",format.format(new Date()));
            List<JinxinOrganization> jinxiOriginList = jinxinAuthSynService.getOriginFromJinxinUserCenter();

            logger.info("清除旧用户数据:{}",format.format(new Date()));
            jinxinAuthSynService.trancatUsers();
            logger.info("清除旧用户与机构关联数据:{}",format.format(new Date()));
            jinxinAuthSynService.trancatUserOrigins();
            logger.info("清楚旧机构数据:{}",format.format(new Date()));
            jinxinAuthSynService.trancatOrigins();

            logger.info("保存用户数据:{}",format.format(new Date()));
            for (JinxinUser jinxinUser : jinxinUserList) {
                jinxinAuthSynService.saveUser(jinxinUser);
                List<JinxinOrganization> origins = jinxinUser.getOrganizations();
                if(origins!=null){
                    for (JinxinOrganization origin : origins) {
                        jinxinAuthSynService.saveUserOrigin(jinxinUser.getUserId(),origin.getOrganizationId());
                    }
                }

            }

            logger.info("保存机构数据:{}",format.format(new Date()));
            JinxinOrganization rootOrigin = new JinxinOrganization();
            for (JinxinOrganization jinxinOrganization : jinxiOriginList) {
                jinxinAuthSynService.saveOrigins(jinxinOrganization);
            }

            logger.info("用户|机构信息同步结束:{}",format.format(new Date()));

            transactionManager.commit(status);
        }catch(Exception e){
            e.printStackTrace();
            transactionManager.rollback(status);
        }finally {

        }

    }

}
