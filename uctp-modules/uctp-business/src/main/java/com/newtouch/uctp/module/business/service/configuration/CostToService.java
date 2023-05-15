package com.newtouch.uctp.module.business.service.configuration;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.*;

import java.util.List;

/**
 * @ClassName CostToService
 * @Author: zhang
 * @Date 2023/5/13
 */
public interface CostToService {
    /** 创建配置信息 */
    public void createCost(CostRespVO reqVO);

    /** 获取税率配置分页 */
    PageResult<CostRespVO> getCostPage(CostReqVO pageVO);

    List<CostRespVO> getCostAcquire(Integer type);

    /** 修改税率配置 */
    public void updateCost(CostUpdateReqVO reqVO);

    /** 获取税率配置 */
    public CostRespVO getCost(Long id);

    /** 导出税率配置 */
    List<CostExcelVO> exportCost(CostExportReqVO reqVO);
}
