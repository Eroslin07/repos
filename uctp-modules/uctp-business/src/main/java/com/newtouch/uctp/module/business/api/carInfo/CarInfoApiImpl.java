package com.newtouch.uctp.module.business.api.carInfo;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.CarInfoService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

/**
 * @author helong
 * @date 2023/5/10 13:55
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class CarInfoApiImpl implements CarInfoApi {

    @Resource
    private CarInfoService carInfoService;

    @Override
    public CommonResult<Boolean> updateBpmApproveInfo(Long carId, String bpmStatus, String bpmReason) {
        carInfoService.updateBpmApproveInfo(carId, bpmStatus, bpmReason);
        return success(true);
    }

    @Override
    public CommonResult<Boolean> updateCarStatus(Long carId, Integer salesStatus, Integer status, Integer statusThree, String bpmStatus, String bpmReason) {
        carInfoService.updateCarStatus(carId, salesStatus, status, statusThree, bpmStatus, bpmReason);
        return success(true);
    }
}
