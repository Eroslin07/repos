package com.newtouch.uctp.module.bpm.service.transfer;

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

import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.tenant.core.util.TenantUtils;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDO;
import com.newtouch.uctp.module.bpm.dal.mysql.car.CarInfoMapper;
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
 * @date 2023/4/24 15:04
 */
@Slf4j
@Service
public class BpmCarTransferServiceImpl implements BpmCarTransferService {

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

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String createTransferBpm(Long carId, String procDefKey) {
        if (ObjectUtil.isNull(carId) || !StringUtils.hasText(procDefKey)) {
            throw new IllegalArgumentException("车辆ID、流程业务标识不能为空。");
        }
        CarInfoDO carInfoDO = carInfoMapper.findCarInfoByCarId(carId);
        if (ObjectUtil.isNull(carInfoDO)) {
            throw new IllegalArgumentException("未能通过车辆ID查询到车辆信息。");
        }
        Long tenantId = carInfoDO.getTenantId();
        AtomicReference<String> formMainId = new AtomicReference<>("");
        TenantUtils.execute(tenantId, () -> {
            String updater = carInfoDO.getUpdater();
            WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), Long.valueOf(updater));
            JSONObject carTransferInfoJson = carTransferApi.getTransferInfo(carId, procDefKey).getCheckedData();
            TenantRespDTO tenantRespDTO = tenantApi.getTenant(tenantId).getCheckedData();
            AdminUserRespDTO adminUserRespDTO = adminUserApi.getUser(Long.valueOf(updater)).getCheckedData();
            DeptRespDTO deptRespDTO = deptApi.getDept(adminUserRespDTO.getDeptId()).getCheckedData();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("marketName", tenantRespDTO.getName());
            variables.put("merchantName", deptRespDTO.getName());
            variables.put("startUserId", adminUserRespDTO.getId());
            variables.put("contractCode", carTransferInfoJson.getString("contractCode"));
            HashMap<String, Object> formDataJsonVariable = new HashMap<String, Object>();
            Map<String, Object> formMainDataObject = new HashMap<>();
            formMainDataObject.put("merchantId", deptRespDTO.getId());
            formMainDataObject.put("thirdId", carId);
            formMainDataObject.put("formDataJson", carTransferInfoJson.getInnerMap());
            formDataJsonVariable.put("formMain", formMainDataObject);
            variables.put("formDataJson", formDataJsonVariable);

            formMainId.set(processInstanceService.createProcessInstanceByKey(adminUserRespDTO.getId(), procDefKey, variables));
        });

        return formMainId.get();
    }
}
