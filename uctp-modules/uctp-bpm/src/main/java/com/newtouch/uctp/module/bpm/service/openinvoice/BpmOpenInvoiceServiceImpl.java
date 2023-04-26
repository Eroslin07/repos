package com.newtouch.uctp.module.bpm.service.openinvoice;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.tenant.core.util.TenantUtils;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDO;
import com.newtouch.uctp.module.bpm.dal.mysql.car.CarInfoMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.car.ContractMapper;
import com.newtouch.uctp.module.bpm.enums.definition.BpmDefTypeEnum;
import com.newtouch.uctp.module.bpm.service.task.BpmProcessInstanceService;
import com.newtouch.uctp.module.business.api.transfer.CarTransferApi;
import com.newtouch.uctp.module.system.api.dept.DeptApi;
import com.newtouch.uctp.module.system.api.dept.dto.DeptRespDTO;
import com.newtouch.uctp.module.system.api.tenant.TenantApi;
import com.newtouch.uctp.module.system.api.tenant.dto.TenantRespDTO;
import com.newtouch.uctp.module.system.api.user.AdminUserApi;
import com.newtouch.uctp.module.system.api.user.dto.AdminUserRespDTO;

/**
 * @author helong
 * @date 2023/4/26 11:06
 */
@Slf4j
@Service
public class BpmOpenInvoiceServiceImpl implements BpmOpenInvoiceService {
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
    public String createOpenInvoiceBpm(Long contractId, String procDefKey) {
        if (ObjectUtil.isNull(contractId) || !StringUtils.hasText(procDefKey)) {
            throw new IllegalArgumentException("合同号ID、流程业务标识不能为空。");
        }
        Map contractMap = MapUtil.toCamelCaseMap(contractMapper.findContractByContractIdToMap(contractId));
        if (CollectionUtils.isEmpty(contractMap)) {
            throw new IllegalArgumentException("未能通过合同号ID查询到车辆信息。");
        }
        Long carId = ObjectUtil.isNull(contractMap.get("carId")) ? null : Long.valueOf(contractMap.get("carId") + "");
        CarInfoDO carInfoDO = carInfoMapper.findCarInfoByCarId(carId);
        if (ObjectUtil.isNull(carInfoDO)) {
            throw new IllegalArgumentException("未能通过车辆ID查询到车辆信息。");
        }
        Long tenantId = carInfoDO.getTenantId();
        AtomicReference<String> formMainId = new AtomicReference<>("");
        TenantUtils.execute(tenantId, () -> {
            String updater = carInfoDO.getUpdater();
            WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), Long.valueOf(updater));
            JSONObject invoiceJson = new JSONObject();
            if (ObjectUtil.equals(BpmDefTypeEnum.MCKP.name(), procDefKey)) {
                invoiceJson = carTransferApi.getForwardInvoiceInfo(contractId).getCheckedData();
            } else if (ObjectUtil.equals(BpmDefTypeEnum.SCKP.name(), procDefKey)) {
                invoiceJson = carTransferApi.getReverseInvoiceInfo(contractId).getCheckedData();
            }

            TenantRespDTO tenantRespDTO = tenantApi.getTenant(tenantId).getCheckedData();
            AdminUserRespDTO adminUserRespDTO = adminUserApi.getUser(Long.valueOf(updater)).getCheckedData();
            DeptRespDTO deptRespDTO = deptApi.getDept(adminUserRespDTO.getDeptId()).getCheckedData();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("marketName", tenantRespDTO.getName());
            variables.put("merchantName", deptRespDTO.getName());
            variables.put("startUserId", adminUserRespDTO.getId());
            variables.put("contractCode", invoiceJson.getString("contractCode"));
            HashMap<String, Object> formDataJsonVariable = new HashMap<String, Object>();
            Map<String, Object> formMainDataObject = new HashMap<>();
            formMainDataObject.put("merchantId", deptRespDTO.getId());
            formMainDataObject.put("thirdId", carId);
            formMainDataObject.put("formDataJson", invoiceJson.getInnerMap());
            formDataJsonVariable.put("formMain", formMainDataObject);
            variables.put("formDataJson", formDataJsonVariable);

            formMainId.set(processInstanceService.createProcessInstanceByKey(adminUserRespDTO.getId(), procDefKey, variables));
        });

        return formMainId.get();
    }
}
