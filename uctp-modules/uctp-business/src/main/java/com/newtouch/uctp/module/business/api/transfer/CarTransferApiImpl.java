package com.newtouch.uctp.module.business.api.transfer;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.CarInfoService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

/**
 * @author helong
 * @date 2023/4/24 14:38
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class CarTransferApiImpl implements CarTransferApi {
    @Resource
    private CarInfoService carInfoService;

    @Override
    public CommonResult<JSONObject> getTransferInfo(Long carId) {
        return success((JSONObject) JSONObject.toJSON(carInfoService.getTransferInfo(carId)));
    }
}
