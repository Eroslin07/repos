package com.newtouch.uctp.module.bpm.service.payfailed;

import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.newtouch.uctp.framework.tenant.core.util.TenantUtils;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.ContractDO;
import com.newtouch.uctp.module.bpm.dal.mysql.car.CarInfoMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.car.ContractMapper;
import com.newtouch.uctp.module.bpm.enums.definition.BpmDefTypeEnum;
import com.newtouch.uctp.module.bpm.service.task.BpmProcessInstanceService;
import com.newtouch.uctp.module.business.api.carInfo.CarInfoApi;
import com.newtouch.uctp.module.business.api.transfer.CarTransferApi;
import com.newtouch.uctp.module.system.api.dept.DeptApi;
import com.newtouch.uctp.module.system.api.dept.dto.DeptRespDTO;
import com.newtouch.uctp.module.system.api.tenant.TenantApi;
import com.newtouch.uctp.module.system.api.tenant.dto.TenantRespDTO;
import com.newtouch.uctp.module.system.api.user.AdminUserApi;
import com.newtouch.uctp.module.system.api.user.dto.AdminUserRespDTO;

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
    @Resource
    private CarInfoApi carInfoApi;

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String createBpm(Long contractId, String procDefKey, Map<String, Object> variables) {
        if (ObjectUtil.isNull(contractId) || !StringUtils.hasText(procDefKey)) {
            throw new RuntimeException("合同号ID、流程业务标识不能为空。");
        }
        ContractDO contractDO = contractMapper.selectByContractIdAndContractType(contractId, 2);
        if (ObjectUtil.isNull(contractDO) || ObjectUtil.isNull(contractDO.getId())) {
            throw new RuntimeException("根据合同号ID查询车辆合同信息失败，无法发起收车支付失败流程。");
        }
        Long tenantId = contractDO.getTenantId();
        String creator = contractDO.getCreator();
        if (ObjectUtil.isNull(tenantId) || !StringUtils.hasText(creator)) {
            throw new RuntimeException("根据合同号ID获取合同创建人或对应租户信息失败，请检查合同信息。");
        }
        AtomicReference<String> formMainId = new AtomicReference<>("");
        TenantUtils.execute(tenantId, () -> {
            WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), Long.valueOf(creator));
            TenantRespDTO tenantRespDTO = tenantApi.getTenant(tenantId).getCheckedData();
            AdminUserRespDTO adminUserRespDTO = adminUserApi.getUser(Long.valueOf(creator)).getCheckedData();
            DeptRespDTO deptRespDTO = deptApi.getDept(adminUserRespDTO.getDeptId()).getCheckedData();
            variables.put("startUserId", adminUserRespDTO.getId());
            variables.put("marketName", tenantRespDTO.getName());
            variables.put("merchantName", deptRespDTO.getName());
            variables.put("contractCode", contractDO.getCode());

            formMainId.set(processInstanceService.createProcessInstanceByKey(adminUserRespDTO.getId(), procDefKey, variables));
        });

        return formMainId.get();
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String createBpm(Long contractId, String failReason) {
        // 合同类型：1-收车委托合同   2-收车合同  3-卖车委托合同  4-卖车合同
        ContractDO contractDO = contractMapper.selectByContractIdAndContractType(contractId, 2);
        if (ObjectUtil.isNull(contractDO) || ObjectUtil.isNull(contractDO.getCarId())) {
            throw new RuntimeException("获取（契约锁）收车合同【" + contractId + "】的基本信息失败。");
        }
        Long tenantId = contractDO.getTenantId();
        String creator = contractDO.getCreator();
        if (ObjectUtil.isNull(tenantId) || !StringUtils.hasText(creator)) {
            throw new RuntimeException("根据合同号ID获取合同创建人或对应租户信息失败，请检查合同信息。");
        }
        AtomicReference<String> formMainId = new AtomicReference<>("");
        TenantUtils.execute(tenantId, () -> {
            WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), Long.valueOf(creator));
            TenantRespDTO tenantRespDTO = tenantApi.getTenant(tenantId).getCheckedData();
            AdminUserRespDTO adminUserRespDTO = adminUserApi.getUser(Long.valueOf(creator)).getCheckedData();
            DeptRespDTO deptRespDTO = deptApi.getDept(adminUserRespDTO.getDeptId()).getCheckedData();
            Map<String, Object> formInfo = carInfoApi.getPayFailedCreateBpmInfo(contractId).getCheckedData();
            formInfo.put("failReason", failReason);
            
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("startUserId", adminUserRespDTO.getId());
            variables.put("marketName", tenantRespDTO.getName());
            variables.put("merchantName", deptRespDTO.getName());
            variables.put("contractCode", contractDO.getCode());
            HashMap<String, Object> formDataJsonVariable = new HashMap<String, Object>();
            Map<String, Object> formMainDataObject = new HashMap<>();
            formMainDataObject.put("merchantId", deptRespDTO.getId());
            formMainDataObject.put("thirdId", contractId);
            formMainDataObject.put("formDataJson", formInfo);
            formDataJsonVariable.put("formMain", formMainDataObject);
            variables.put("formDataJson", formDataJsonVariable);

            formMainId.set(processInstanceService.createProcessInstanceByKey(adminUserRespDTO.getId(), BpmDefTypeEnum.SKZH.name(), variables));
        });


        return formMainId.get();
    }
}
