package com.newtouch.uctp.module.business.service.configuration;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.util.MyBatisUtils;
import com.newtouch.uctp.framework.security.core.LoginUser;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.*;
import com.newtouch.uctp.module.business.dal.mysql.configurationMapper.CostToMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * @ClassName CostToToServiceImpl
 * @Author: zhang
 * @Date 2023/5/14
 */
@Slf4j
@Transactional(readOnly = false)
@Service
public class CostToToServiceImpl implements CostToService {
    @Resource
    private CostToMapper costToMapper;

    /** 创建配置信息 */
    @Override
    public void createCost(CostRespVO reqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        Long tenantId = loginUser.getTenantId();
        reqVO.setTenantId(tenantId);
        String createdBy = String.valueOf(loginUser.getId());
        reqVO.setCreatedBy(createdBy);
        costToMapper.createCost(reqVO);
    }

    /** 获取税率配置分页 */
    @Override
    public PageResult<CostRespVO> getCostPage(CostReqVO pageVO) {
        Page<CostRespVO> page = MyBatisUtils.buildPage(pageVO);
        costToMapper.getCostPage(page, pageVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<CostRespVO> getCostAcquire(Integer type) {
        return costToMapper.getCostAcquire(type);
    }

    /** 修改税率配置 */
    @Override
    public void updateCost(CostUpdateReqVO reqVO) {
        costToMapper.updateCost(reqVO);
    }

    /** 获取税率配置 */
    @Override
    public CostRespVO getCost(Long id) {
        return costToMapper.getCost(id);
    }

    /** 导出税率配置 */
    @Override
    public List<CostExcelVO> exportCost(CostExportReqVO reqVO) {
        return costToMapper.listCost(reqVO);
    }
}
