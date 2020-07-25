package com.datarecord.webapp.process.service.imp;

import com.datarecord.webapp.process.entity.ReportJobData;
import com.datarecord.webapp.process.entity.SaveReportJobInfos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("gridRecordProcessService")
public class GridRecordProcessService extends AbstractRecordProcessServiceImp {
    private Logger logger = LoggerFactory.getLogger(GridRecordProcessService.class);
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDatas(SaveReportJobInfos reportJobInfo) {
        List<ReportJobData> updateDatas = reportJobInfo.getReportJobInfos();
        List<ReportJobData> newDatas = reportJobInfo.getNewReportJobInfos();
        List<ReportJobData> delDatas = reportJobInfo.getDelReportJobInfos();
        Integer unitId = updateDatas.get(0).getUnit_id();
        //删除
        List<Integer> deleteColumIds = new ArrayList<>();
        if(delDatas!=null&&delDatas.size()>0){
            for (ReportJobData delData : delDatas) {
                deleteColumIds.add(delData.getColum_id());
                recordProcessDao.deleteReportJobData(delData,reportJobInfo.getJob_id());
            }
        }
        logger.debug("待删除的数据行数：{}",deleteColumIds.size());

        //更新 重排colum_id
        Map<Integer,Integer> updateNewColumIds = new HashMap<>();
        Integer newColumIdTmp = 0;
        Integer maxUpdateColumId = 0;
        for (ReportJobData updateData : updateDatas) {
            if(deleteColumIds.size()>0){
                Integer updateColumId = updateData.getColum_id();
                for (Integer deleteColumId : deleteColumIds) {
                    if(updateColumId>deleteColumId){
                        if(!updateNewColumIds.containsKey(updateData.getColum_id())){
                            updateNewColumIds.put(updateData.getColum_id(),updateColumId-1);
                        }
                    }
                }
            }
            if(updateNewColumIds.containsKey(updateData.getColum_id())){
                newColumIdTmp = updateNewColumIds.get(updateData.getColum_id());
            }else{
                newColumIdTmp = updateData.getColum_id();
            }
            if(updateData.getColum_id()>=maxUpdateColumId){
                maxUpdateColumId = updateData.getColum_id();
            }
            recordProcessDao.updateReportJobData(updateData,newColumIdTmp,reportJobInfo.getJob_id());
        }
        //所有大于当前数组中最大columid的项 columid前移
        if(deleteColumIds.size()>0){
            recordProcessDao.reOrderColumId(reportJobInfo.getJob_id(),
                    reportJobInfo.getReport_id(),
                    unitId,
                    maxUpdateColumId,deleteColumIds.size());
        }

        Integer maxColumId = recordProcessDao.getMaxColumId(reportJobInfo.getJob_id().toString(), reportJobInfo.getReport_id().toString(), unitId.toString());

        //插入 新数据colum_id接更新后
        Map<Integer,Integer> addColumIds = new HashMap<>();
        if(newDatas!=null&&newDatas.size()>0){
            for (ReportJobData newData : newDatas) {
                if(!addColumIds.containsKey(newData.getColum_id())){
                    maxColumId++;
                    addColumIds.put(newData.getColum_id(),maxColumId);
                    //有更新数据 当前columid 需要加1
//                    if(updateNewColumIds.size()>0){
//                        newColumIdTmp++;
//                    }else{
//                        if(newColumIdTmp==0){
//                            if(addColumIds.size()>0)
//                                newColumIdTmp++;
//                        }else
//                            newColumIdTmp++;
//                    }
//
//                    addColumIds.put(newData.getColum_id(),0);
                }
                newData.setColum_id(addColumIds.get(newData.getColum_id()));

                recordProcessDao.createRcdReortJobData(newData,String.valueOf(reportJobInfo.getJob_id()));
            }
        }
    }

    @Override
    public Map<Integer, Map<Integer, String>> validateDatas(List<ReportJobData> reportJobDataList, String unitId,String clientType) {

        Map<Integer, Map<Integer, String>> validateResultMap = super.validateDatas(reportJobDataList, unitId,clientType);

        return validateResultMap;

    }
}
