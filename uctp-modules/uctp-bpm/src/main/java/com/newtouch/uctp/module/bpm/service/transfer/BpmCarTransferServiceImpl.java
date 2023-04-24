package com.newtouch.uctp.module.bpm.service.transfer;

import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.module.business.api.transfer.CarTransferApi;

/**
 * @author helong
 * @date 2023/4/24 15:04
 */
@Slf4j
@Service
public class BpmCarTransferServiceImpl implements BpmCarTransferService {

    @Resource
    private CarTransferApi carTransferApi;

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String createTransferBpm(Long carId) {
        JSONObject carTransferInfoJson = carTransferApi.getTransferInfo(carId).getData();
        JSONObject carInfoJson = (ObjectUtil.isNull(carTransferInfoJson) || !carTransferInfoJson.containsKey("carInfo")) ? null : carTransferInfoJson.getJSONObject("carInfo");

        if (ObjectUtil.isNull(carInfoJson) || ObjectUtil.isNull(carInfoJson.get("id"))) {
            throw new RuntimeException("自动发起车辆过户流程失败，未获取到车辆ID");
        }

        Long tenantId = carInfoJson.getLongValue("tenantId");
        String updater = carInfoJson.getString("updater");



        return null;
    }
}
