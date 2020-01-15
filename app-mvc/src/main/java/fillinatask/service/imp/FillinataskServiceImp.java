package fillinatask.service.imp;

import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import datadictionary.service.imp.DataDictionaryServiceImp;
import fillinatask.bean.Fillinatask;
import fillinatask.bean.RcdJobUnitConfig;
import fillinatask.dao.FillinataskDao;
import fillinatask.service.FillinataskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fillinataskService")
public class FillinataskServiceImp implements FillinataskService {

    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private FillinataskDao fillinataskDao;

    @Override
    public PageResult rcdjobconfiglist(int currPage, int pageSize, String job_name, String job_status) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        Page<Fillinatask> contactPageDatas = fillinataskDao.rcdjobconfiglist(currPage, pageSize,job_name,job_status);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    public void insertrcdjobconfig(String job_name, String job_start_dt, String job_end_dt) {
        fillinataskDao.insertrcdjobconfig(job_name,job_start_dt,job_end_dt);
    }

    @Override
    public void insertrcdjobpersonassign(String job_id, String[] userid) {
        for (int i = 0; i<userid.length;i++){
            fillinataskDao.insertrcdjobpersonassign(job_id,userid[i]);
        }
    }

    @Override
    public void deletercdjobpersonassign(String job_id) {
        fillinataskDao.deletercdjobpersonassign(job_id);
    }

    @Override
    public void updatercdjobconfig(String job_id,String job_name, String job_start_dt, String job_end_dt) {
        fillinataskDao.updatercdjobconfig(job_id,job_name,job_start_dt,job_end_dt);
    }

    @Override
    public List<RcdJobUnitConfig> selectRcdJobUnitConfig(String job_id) {
        return fillinataskDao.selectRcdJobUnitConfig(job_id);
    }

    @Override
    public List<RcdJobUnitConfig> selectRcdJobUnitConfigyi(String job_id) {
        return fillinataskDao.selectRcdJobUnitConfigyi(job_id);
    }

    @Override
    public void updateRcdJobUnitConfigyi(String[] jobunitid) {
        for(int i=0 ; i<jobunitid.length;i++){
            fillinataskDao.updateRcdJobUnitConfigyi(jobunitid[i]);
        }
    }
}
