package com.datarecord.webapp.process.service.imp;

import com.datarecord.webapp.dataindex.bean.FldDataTypes;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.datarecord.webapp.process.entity.ReportJobData;
import com.datarecord.webapp.process.entity.SaveReportJobInfos;
import com.google.common.base.Strings;
import com.workbench.exception.runtime.WorkbenchRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("gridRecordProcessService")
public class GridRecordProcessService extends AbstractRecordProcessServiceImp {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDatas(SaveReportJobInfos reportJobInfo) {
        List<ReportJobData> updateDatas = reportJobInfo.getReportJobInfos();
        List<ReportJobData> newDatas = reportJobInfo.getNewReportJobInfos();
        List<ReportJobData> delDatas = reportJobInfo.getDelReportJobInfos();

        //删除
        if(delDatas!=null&&delDatas.size()>0){
            for (ReportJobData delData : delDatas) {
                recordProcessDao.deleteReportJobData(delData,reportJobInfo.getJob_id());
            }
        }
        //更新 重排colum_id
        Map<Integer,Integer> updateNewColumIds = new HashMap<>();
        Integer newColumIdTmp = 0;
        for (ReportJobData updateData : updateDatas) {
            if(!updateNewColumIds.containsKey(updateData.getColum_id())){
                if(updateNewColumIds.size()>0){
                    newColumIdTmp++;
                }
                updateNewColumIds.put(updateData.getColum_id(),newColumIdTmp);
            }
            recordProcessDao.updateReportJobData(updateData,newColumIdTmp,reportJobInfo.getJob_id());
        }

        //插入 新数据colum_id接更新后
        Map<Integer,Integer> addColumIds = new HashMap<>();
        if(newDatas!=null&&newDatas.size()>0){
            for (ReportJobData newData : newDatas) {
                if(!addColumIds.containsKey(newData.getColum_id())){
                    //有更新数据 当前columid 需要加1
                    if(updateNewColumIds.size()>0){
                        newColumIdTmp++;
                    }else{
                        if(newColumIdTmp==0){
                            if(addColumIds.size()>0)
                                newColumIdTmp++;
                        }else
                            newColumIdTmp++;
                    }

                    addColumIds.put(newData.getColum_id(),0);
                }
                newData.setColum_id(newColumIdTmp);

                recordProcessDao.createRcdReortJobData(newData,String.valueOf(reportJobInfo.getJob_id()));
            }
        }
    }

    @Override
    public Map<Integer, Map<Integer, String>> validateDatas(List<ReportJobData> reportJobDataList, String unitId) {

        Map<Integer, Map<Integer, String>> validateResultMap = super.validateDatas(reportJobDataList, unitId);

        return validateResultMap;

    }
}
