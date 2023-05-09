package com.newtouch.uctp.module.business.service.configuration;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.*;

import java.util.List;

/**
 * @ClassName TaxConfigService
 * @Author: zhang
 * @Date 2023/5/6.
 */
public interface TaxConfigService {
    /** 创建配置信息 */
    public void createTax(TaxRespVO reqVO);

    /** 获取税率配置分页 */
    PageResult<TaxRespVO> getTaxPage(TaxReqVO pageVO);

    /** 修改税率配置 */
    public void updateTax(TaxUpdateReqVO reqVO);

    /** 获取税率配置 */
    public TaxRespVO getTax(Long id);

    /** 导出税率配置 */
    List<TaxExcelVO> exportTax(TaxExportReqVO reqVO);
}
