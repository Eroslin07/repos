package com.newtouch.uctp.module.bpm.service.payfailed;

import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.newtouch.uctp.module.bpm.dal.mysql.car.CarInfoMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.car.ContractMapper;
import com.newtouch.uctp.module.bpm.service.task.BpmProcessInstanceService;
import com.newtouch.uctp.module.business.api.transfer.CarTransferApi;
import com.newtouch.uctp.module.system.api.dept.DeptApi;
import com.newtouch.uctp.module.system.api.tenant.TenantApi;
import com.newtouch.uctp.module.system.api.user.AdminUserApi;

/**
 * @author helong
 * @date 2023/4/28 18:24
 */
@Service
@Slf4j
@Validated
public class PayFailedServiceImpl implements PayFailedService {

    @Resource
    private CarTransferApi carTransferApi;
    @Resource
    private TenantApi tenantApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private ContractMapper contractMapper;

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String createBpm(Long contractId, String procDefKey, Map<String, Object> variables) {
        if (ObjectUtil.isNull(contractId) || !StringUtils.hasText(procDefKey)) {
            throw new IllegalArgumentException("合同号ID、流程业务标识不能为空。");
        }


        return null;
    }
}
