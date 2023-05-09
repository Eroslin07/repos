package com.newtouch.uctp.module.business.service.configuration;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.util.MyBatisUtils;
import com.newtouch.uctp.framework.security.core.LoginUser;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.*;
import com.newtouch.uctp.module.business.dal.mysql.configurationMapper.TaxMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @ClassName TaxConfigService
 * @Author: zhang
 * @Date 2023/5/6.
 */
@Slf4j
@Transactional(readOnly = false)
@Service
public class TaxConfigServiceImpl implements TaxConfigService{
    @Resource
    private TaxMapper taxMapper;

    /** 创建配置信息 */
    @Override
    public void createTax(TaxRespVO reqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        Long tenantId = loginUser.getTenantId();
        reqVO.setTenantId(tenantId);
        reqVO.setBusinessId(loginUser.getId());
        taxMapper.createTax(reqVO);
    }

    /** 获取税率配置分页 */
    @Override
    public PageResult<TaxRespVO> getTaxPage(TaxReqVO pageVO) {
        Page<TaxRespVO> page = MyBatisUtils.buildPage(pageVO);
        taxMapper.getTaxPage(page, pageVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    /** 修改税率配置 */
    @Override
    public void updateTax(TaxUpdateReqVO reqVO) {
        taxMapper.updateTax(reqVO);
    }

    /** 获取税率配置 */
    @Override
    public TaxRespVO getTax(Long id) {
        return taxMapper.getTax(id);
    }

    /** 导出税率配置 */
    @Override
    public List<TaxExcelVO> exportTax(TaxExportReqVO reqVO) {
        return taxMapper.listTax(reqVO);
    }
}
