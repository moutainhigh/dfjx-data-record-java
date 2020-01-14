package reportinggroup.service;

import com.webapp.support.page.PageResult;
import reportinggroup.bean.RcdJobUnitFld;
import reportinggroup.bean.rcdJobConfig;

import java.util.List;

public interface ReportingGroupService {
    List<rcdJobConfig> leftrcdjobconfig();

    PageResult rcdjobunitconfiglist(int currPage, int pageSize, String job_id);

    void deletercdjobunitconfig(String job_unit_id);

    void rcdjobunitfld(String fld_id, String[] jobunitid);

    void rcdjobunitflddelete(String fld_id);

    List<RcdJobUnitFld> selectrcdjobunitfld();
}
