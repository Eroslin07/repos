package com.newtouch.uctp.module.business.service.configuration;

import com.newtouch.uctp.framework.security.core.LoginUser;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.MakeInvoiceRespVO;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.MakeInvoiceUpdateReqVO;
import com.newtouch.uctp.module.business.dal.mysql.configurationMapper.MakeInvoiceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName MakeInvoiceServiceImpl
 * @Author: zhang
 * @Date 2023/4/24.
 */
@Slf4j
@Transactional(readOnly = false)
@Service
public class MakeInvoiceServiceImpl implements MakeInvoiceService{
    @Resource
    private MakeInvoiceMapper makeMapper;

    /** 新建配置信息 */
    @Override
    public void createMakeInvoice(MakeInvoiceRespVO reqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        Long tenantId = loginUser.getTenantId();
        reqVO.setTenantId(tenantId);
        makeMapper.createMakeInvoice(reqVO);
    }

    /** 编辑配置信息 */
    @Override
    public void updateMakeInvoice(MakeInvoiceUpdateReqVO reqVO) {
        makeMapper.updateMakeInvoice(reqVO);
    }

    /** 获取配置信息 */
    @Override
    public MakeInvoiceRespVO getMakeInvoice(Long tenantId) {
        return makeMapper.getMakeInvoice(tenantId);
    }
}
