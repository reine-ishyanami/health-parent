package com.reine.service;

import java.util.Map;

/**
 * @author reine
 * @since 2022/5/3 12:52
 */
public interface ReportService {

    /**
     * 查询运营数据
     * @return
     */
    Map<String, Object> getBusinessReportData() throws Exception;

}
