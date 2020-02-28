package com.datarecord.webapp.process.service;

import com.webapp.support.page.PageResult;
import org.springframework.web.bind.annotation.RequestParam;

public interface RecordProcessFlowService {

    PageResult pageJob(@RequestParam("currPage") String currPage, @RequestParam("pageSize") String pageSize, String reportStatus, String reportName, String reportOrigin);

}
