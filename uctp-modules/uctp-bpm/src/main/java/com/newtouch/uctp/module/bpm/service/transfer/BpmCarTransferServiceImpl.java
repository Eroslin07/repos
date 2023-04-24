package com.newtouch.uctp.module.bpm.service.transfer;

import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
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

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String createTransferBpm(Long carId, String procDefKey) {
        JSONObject carTransferInfoJson = carTransferApi.getTransferInfo(carId, procDefKey).getData();
        JSONObject carInfoJson = (ObjectUtil.isNull(carTransferInfoJson) || !carTransferInfoJson.containsKey("carInfo")) ? null : carTransferInfoJson.getJSONObject("carInfo");

        if (ObjectUtil.isNull(carInfoJson) || ObjectUtil.isNull(carInfoJson.get("id"))) {
            throw new RuntimeException("自动发起车辆过户流程失败，未获取到车辆ID");
        }

        Long tenantId = carInfoJson.getLongValue("tenantId");
        String updater = carInfoJson.getString("updater");
        TenantRespDTO tenantRespDTO = tenantApi.getTenant(tenantId).getCheckedData();
        AdminUserRespDTO adminUserRespDTO = adminUserApi.getUser(Long.valueOf(updater)).getCheckedData();
        DeptRespDTO deptRespDTO = deptApi.getDept(adminUserRespDTO.getDeptId()).getCheckedData();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("marketName", tenantRespDTO.getName());
        variables.put("merchantName", deptRespDTO.getName());
        variables.put("startUserId", adminUserRespDTO.getId());

        return null;
    }
}
